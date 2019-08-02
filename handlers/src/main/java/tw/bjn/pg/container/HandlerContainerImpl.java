package tw.bjn.pg.container;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import tw.bjn.pg.annotations.LineEventHandler;
import tw.bjn.pg.handlers.EventHandler;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;


@Slf4j
@Component
public class HandlerContainerImpl implements HandlerContainer {

    private List<EventHandler<Event>> handlers;
    final private ApplicationContext applicationContext;

    @Autowired
    HandlerContainerImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        log.debug("Start to collect handlers.");
        handlers = new ArrayList<>();
        Map<String,Object> mapHandlers = applicationContext.getBeansWithAnnotation(LineEventHandler.class);
        mapHandlers.forEach((beanName, beanObj)->{
           if (!EventHandler.class.isInstance(beanObj)) {
                log.warn("({}) is not a EventHandler class. ignored", beanObj.getClass().getName());
                return;
            }
            Type type = findParameterizedType(beanObj, 0);
            if (!isAssignableFrom(Event.class, type)) {
                log.error("Only support '({})' handler but get ({})", Event.class.getName(), type.getTypeName());
                return;
            }
            handlers.add((EventHandler<Event>) beanObj);
            log.info("Add event handler. ({})", type.getTypeName());
        });
        if (handlers.isEmpty()) {
            log.error("Fatal error there's no available handler.");
            throw new NoSuchElementException("No available handler.");
        }
    }

    private boolean isAssignableFrom(Class<?> a, Type b) {
        if (a == null || b == null)
            return false;
        if (b instanceof Class && a.isAssignableFrom((Class<?>) b))
            return true;
        return (b instanceof ParameterizedType && a.isAssignableFrom((Class<?>) ((ParameterizedType) b).getRawType()));
    }

    private Type findParameterizedType(Object instance, int parameterIndex) {
        Class<?> subClass = instance.getClass();
        ParameterizedType parameterizedType = (ParameterizedType) subClass.getGenericSuperclass();
        if (parameterizedType != null) {
            Type[] types = parameterizedType.getActualTypeArguments();
            if (types.length > parameterIndex)
                return types[parameterIndex];
        }
        log.error("Cannot get parameterized type from ({})", instance);
        return null;
    }

    public Collection<EventHandler<Event>> getHandlers() {
        return handlers;
    }

    public EventHandler<Event> findSuitableHandler(Event event) {
        // TODO: need a smarter way to classify various event
        return handlers.stream()
                .filter(h -> !h.getClass().getAnnotation(LineEventHandler.class).fallback())
                .filter(h-> {
                    // must refactor
                        Type handleType = findParameterizedType(h, 0);
                        if (!isAssignableFrom(event.getClass(), handleType))
                            return false;

                        if (event instanceof MessageEvent) { // wtf, really?
                            if (handleType instanceof ParameterizedType) {
                                ParameterizedType pt = (ParameterizedType) handleType;
                                Type nestedGenericType = pt.getActualTypeArguments()[0];
//                                if (isAssignableFrom((Class)nestedGenericType, ((MessageEvent) event).getMessage().getClass())) // work
//                                    return true;
                                for (Method method :event.getClass().getDeclaredMethods()) { // find type from instance's getter
                                    Class type = method.getReturnType();
                                    if (type.isAssignableFrom((Class) nestedGenericType)) {
                                        try {
                                            Object target = method.invoke(event);
                                            if (target.getClass() == nestedGenericType)
                                                return true;
                                        } catch (Exception e) {
                                            log.error("get method failed", e);
                                        }
                                    }
                                }
                                return false;
                            }
                        }

                        return true;
                })
//                .sorted() // TODO: if we get may indicators...
                .findFirst()
                .orElseGet(() -> handlers.stream()
                        .filter( h -> h.getClass().getAnnotation(LineEventHandler.class).fallback() )
                        .findFirst().orElse(null));
    }
}

package tw.bjn.pg.event.handlers;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.linecorp.bot.model.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import tw.bjn.pg.annotations.LineEventHandler;
import tw.bjn.pg.interfaces.event.EventHandler;
import tw.bjn.pg.interfaces.event.HandlerContainer;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class HandlerContainerImpl implements HandlerContainer {

    private Map<String, EventHandler<Event>> handlers;
    final private ApplicationContext applicationContext;

    @Autowired
    HandlerContainerImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        handlers = new HashMap<>();
        Map<String,Object> mapHandlers = applicationContext.getBeansWithAnnotation(LineEventHandler.class);
        mapHandlers.forEach((beanName, beanObj)->{
            LineEventHandler type = beanObj.getClass().getAnnotation(LineEventHandler.class);
            handlers.put(type.value(), (EventHandler<Event>) beanObj);
            log.info("Add ({}) event handler.", type);
        });
    }

    public EventHandler<Event> findSuitableHandler(Event event) {
        return findSuitableHandler(event.getClass().getAnnotation(JsonTypeName.class));
    }

    private EventHandler<Event> findSuitableHandler(JsonTypeName type) {
        // TODO: need a smarter way to classify various event
        if( type == null || !handlers.containsKey(type.value()) )
            return handlers.get("default");
        return handlers.get(type.value());
    }

    public Map<String, EventHandler<Event>> getHandlers() {
        return handlers;
    }
}

package tw.bjn.pg.utils;

import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;

public class YamlReader<T> {

    public T parse(String path) throws IOException {
        Yaml yaml = new Yaml();
        return yaml.load(new ClassPathResource(path).getInputStream());
    }
}

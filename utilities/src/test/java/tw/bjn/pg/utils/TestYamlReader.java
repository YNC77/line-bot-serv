package tw.bjn.pg.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class TestYamlReader {

    @Test
    public void testYamlReader() throws IOException {
        List<String> quotations = new YamlReader<List<String>>().parse("quotations.yml");
        System.out.println(quotations);
        Assert.assertFalse(quotations.isEmpty());
    }
}

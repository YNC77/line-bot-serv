package tw.bjn.pg.ptt;

import org.junit.Test;

public class PttTest {

    @Test
    public void test() {
        Ptt p = new Ptt();
        p.init();
        PttResult result = p.getLatest();
        System.out.println(result);
    }
}

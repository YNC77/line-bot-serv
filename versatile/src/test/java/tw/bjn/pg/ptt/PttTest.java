package tw.bjn.pg.ptt;

import org.junit.Test;

public class PttTest {

    @Test
    public void test() {
        Ptt p = new Ptt();
        p.init();
        p.getLatest();
    }
}

package tw.bjn.pg.ptt;

import org.junit.Test;

public class PttTest {

    @Test
    public void test() {
        Ptt p = new Ptt();
        p.init();
        PttResult result = p.fetchPttBoard("Gossiping", "");
        System.out.println(result);
    }

    @Test
    public void getHotBoards() {
        Ptt p = new Ptt();
        p.init();
        PttResult result = p.fetchHotBoards();
        System.out.println(result);
    }
}

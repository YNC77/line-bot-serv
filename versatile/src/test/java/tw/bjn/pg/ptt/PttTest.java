package tw.bjn.pg.ptt;

import org.junit.Test;
import tw.bjn.pg.ptt.client.PttClientFactory;
import tw.bjn.pg.ptt.model.PttResult;

public class PttTest {

    @Test
    public void test() {
        PttClientFactory pttClientFactory = new PttClientFactory();
        pttClientFactory.init();
        Ptt p = new Ptt(pttClientFactory);
        p.init();
        PttResult result = p.fetchPttBoard("Gossiping");
        System.out.println(result);
    }

    @Test
    public void getHotBoards() {
        PttClientFactory pttClientFactory = new PttClientFactory();
        pttClientFactory.init();
        Ptt p = new Ptt(pttClientFactory);
        p.init();
        PttResult result = p.fetchHotBoards();
        System.out.println(result);
    }

    @Test
    public void searchBoard() {
        PttClientFactory pttClientFactory = new PttClientFactory();
        pttClientFactory.init();
        Ptt p = new Ptt(pttClientFactory);
        p.init();
        PttResult result = p.searchPttBoard("Gossiping", "test");
        System.out.println(result);
    }
}

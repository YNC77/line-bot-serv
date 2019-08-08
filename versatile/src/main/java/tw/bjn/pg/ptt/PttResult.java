package tw.bjn.pg.ptt;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PttResult {
    List<PttItem> pttItemList;
    String oldestUrl;
    String prevPageUrl;
    String nextPageUrl;
    String latestUrl;
}

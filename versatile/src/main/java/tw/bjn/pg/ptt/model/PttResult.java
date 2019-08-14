package tw.bjn.pg.ptt.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Builder
public class PttResult {
    // TODO: separate different result
    private List<PttBoardItem> pttBoardItems;
    private List<PttPostItem> pttItemList;
    private String board;
    private String oldestUrl;
    private String prevPageUrl;
    private String nextPageUrl;
    private String latestUrl;

    private static final Pattern p = Pattern.compile("index([0-9]*).html$");

    public String getIndexFromUrl(String url) {
        if (url == null || url.isEmpty())
            return "";

        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    public String getNextIndex() {
        return getIndexFromUrl(nextPageUrl);
    }

    public String getPrevIndex() {
        return getIndexFromUrl(prevPageUrl);
    }
}

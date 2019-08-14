package tw.bjn.pg.ptt.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PttPostItem {
    String title;
    String url;
    String author;
    String date;
    String reply;
}

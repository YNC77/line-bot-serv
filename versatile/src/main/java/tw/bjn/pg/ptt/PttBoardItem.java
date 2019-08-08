package tw.bjn.pg.ptt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PttBoardItem {
    private String name;
    private String popularity;
    private String clazz;
    private String title;
}


package tw.bjn.pg.calculator.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Token {
    private TokenType type;
    private String value;
}
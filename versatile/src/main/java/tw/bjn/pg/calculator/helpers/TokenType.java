package tw.bjn.pg.calculator.helpers;

import java.util.Arrays;
import java.util.function.BiFunction;

public enum TokenType {
    ADD('+', (a,b) -> a + b),
    SUB('-', (a,b) -> a - b),
    MUL('*', (a,b) -> a * b),
    DIV('/', (a,b) -> a / b),
    INTEGER,
    LEFT_PARAM('('),
    RIGHT_PARAM(')'),
    EOF
    ;

    Character value = null;
    BiFunction<Integer,Integer,Integer> operator = null;

    TokenType() { }

    TokenType(Character value) {
        this.value = value;
    }

    TokenType(Character value, BiFunction<Integer,Integer,Integer> operator) {
        this.value = value;
        this.operator = operator;
    }

    boolean match(char c) {
        return this.value.equals(c);
    }

    static TokenType getSymbol(char c) {
        return Arrays.stream(values())
                .filter(tokenType -> tokenType.value != null)
                .filter(tokenType -> tokenType.match(c))
                .findFirst().orElse(null);
    }
}
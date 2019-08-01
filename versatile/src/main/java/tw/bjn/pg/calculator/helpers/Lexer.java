package tw.bjn.pg.calculator.helpers;

import tw.bjn.pg.calculator.CalcException;

public class Lexer {

    private String context;
    private int idx;

    public Lexer(String context) {
        this.context = context;
        this.idx = 0;
    }

    public Token nextToken() {
        while (!isEnd() && currentChar() == ' ') forward();

        if (isEnd())
            return new Token(TokenType.EOF, null);

        TokenType type = TokenType.getSymbol(currentChar());
        if (type != null) {
            forward();
            return new Token(type, String.valueOf(type.value));
        }

        StringBuilder integer = new StringBuilder();
        while (!isEnd() && Character.isDigit(currentChar())) {
            integer.append(currentChar());
            forward();
        }
        if (integer.length() > 0)
            return new Token(TokenType.INTEGER, integer.toString());

        throw new CalcException("Unknown character - "+ currentChar());
    }

    private boolean forward() {
        ++idx;
        return isEnd();
    }

    private boolean isEnd() {
        return idx >= context.length();
    }

    private char currentChar() {
        return context.charAt(idx);
    }
}
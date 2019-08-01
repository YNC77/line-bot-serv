package tw.bjn.pg.calculator.helpers;

import tw.bjn.pg.calculator.CalcException;

public class Parser {

    private Lexer lexer;
    private Token currToken;
    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.currToken = lexer.nextToken();
    }

    public int parse() {
        if (TokenType.EOF.equals(currToken.getType()))
            throw new CalcException("empty string");

        int sum = addOrSub();
        return sum;
    }

    private void nextToken() {
        currToken = lexer.nextToken();
    }

    private int addOrSub() { // A + B, A - B
        int sum = mulOrDiv();
        while (TokenType.ADD.equals(currToken.getType()) || TokenType.SUB.equals(currToken.getType())) {
            Token token = currToken;
            currToken = lexer.nextToken();
            sum = token.getType().operator.apply(sum, mulOrDiv());
        }
        return sum;
    }

    private int mulOrDiv() { // A * B, A / B  , prior to add/sub
        int sum = integer();
        while (TokenType.MUL.equals(currToken.getType()) || TokenType.DIV.equals(currToken.getType())) {
            Token token = currToken;
            currToken = lexer.nextToken();
            sum = token.getType().operator.apply(sum, integer());
        }
        return sum;
    }

    private int integer() { // number, ()
        int sum = 0; // give default 0, so we can handle a case like: "-3 + 7" which has '-' at the beginning
        if (TokenType.INTEGER.equals(currToken.getType())) {
            sum = Integer.valueOf(currToken.getValue());
            nextToken();
        } else if (TokenType.LEFT_PARAM.equals(currToken.getType())) {
            nextToken();
            sum = addOrSub();
            if (!TokenType.RIGHT_PARAM.equals(currToken.getType()))
                throw new CalcException("expect ')' but get: " + currToken.getValue());
            nextToken();
        }
        return sum;
    }
}

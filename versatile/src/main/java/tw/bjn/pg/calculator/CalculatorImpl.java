package tw.bjn.pg.calculator;

import org.springframework.stereotype.Component;
import tw.bjn.pg.calculator.helpers.Parser;
import tw.bjn.pg.calculator.helpers.Lexer;

@Component
public class CalculatorImpl implements Calculator {
    @Override
    public int calc(String expression) {
        Lexer lexer = new Lexer(expression);
        Parser parser = new Parser(lexer);
        return parser.parse();
    }
}
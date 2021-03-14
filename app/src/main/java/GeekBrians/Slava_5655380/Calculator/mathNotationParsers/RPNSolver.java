package GeekBrians.Slava_5655380.Calculator.mathNotationParsers;

import java.math.BigDecimal;
import java.util.Stack;

import GeekBrians.Slava_5655380.Calculator.BinaryOperation;
import GeekBrians.Slava_5655380.Calculator.Operation;

public class RPNSolver {

    BinaryOperation calculator;

    private final String VARIABLE = "var";

    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
        } catch (Exception e) {
            if (token.equals(VARIABLE)) {
                return true;
            }
            return false;
        }
        return true;
    }

    public Stack<String> stackRPN = new Stack<String>();

    public Stack<String> resultStack = new Stack<String>();

    public RPNSolver(BinaryOperation calculator, Stack<String> stackRPN) {
        this.calculator = calculator;
        this.stackRPN = stackRPN;
    }

    public String solve() {
        while (!stackRPN.empty()) {
            String token = stackRPN.pop();
            if (isNumber(token))
                resultStack.push(token);
            else {
                String rightOperand;
                String leftOperand;
                try {
                    rightOperand = resultStack.pop();
                    leftOperand = resultStack.pop();
                } catch (java.util.EmptyStackException e){
                    throw new MissingTokenException();
                }
                if (token.equals("+")) {
                    resultStack.push(String.valueOf(calculator.binaryOperation(leftOperand, rightOperand, Operation.ADD)));
                } else if (token.equals("-")) {
                    resultStack.push(String.valueOf(calculator.binaryOperation(leftOperand, rightOperand, Operation.SUB)));
                } else if (token.equals("/")) {
                    resultStack.push(String.valueOf(calculator.binaryOperation(leftOperand, rightOperand, Operation.DIV)));
                } else if (token.equals("*")) {
                    resultStack.push(String.valueOf(calculator.binaryOperation(leftOperand, rightOperand, Operation.MULT)));
                }
            }
        }
        return resultStack.pop();
    }
}
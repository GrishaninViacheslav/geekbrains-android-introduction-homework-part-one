package GeekBrians.Slava_5655380.Calculator.MathNotationParsers;

import java.text.ParseException;
import java.util.Collections;
import java.util.Stack;
import java.util.StringTokenizer;

import static GeekBrians.Slava_5655380.TextUtils.StringAnalyzingUtil.*;

/**
 * Класс реализующий алгоритм сортировочной станции.
 * Исходный код получен по ссылке:
 * https://www.pvsm.ru/java/3665
 * <p>
 * Модификации оригинального исходного кода:
 * Удаленна реализация парсинга комплексных чисел;
 * Удаленно не используемое поле stackAnswer;
 * Добавлен геттер stackRPN;
 * Реализованно выбрасывание исключения ParseException;
 */
public class InfixToRPNConverter {

    // list of available functions
    private final String[] FUNCTIONS = {"abs", "acos", "arg", "asin", "atan", "conj", "cos", "cosh", "exp", "imag", "log", "neg", "pow", "real", "sin", "sinh", "sqrt", "tan", "tanh"};
    // list of available operators
    private final String OPERATORS = "+-*/";
    // separator of arguments
    private final String SEPARATOR = ",";
    // variable token
    private final String VARIABLE = "var";
    // temporary stack that holds operators, functions and brackets
    private Stack<String> stackOperations = new Stack<String>();
    // stack for holding expression converted to reversed polish notation
    private Stack<String> stackRPN = new Stack<String>();
    // stack for holding the calculations result

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

    private boolean isFunction(String token) {
        for (String item : FUNCTIONS) {
            if (item.equals(token)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSeparator(String token) {
        return token.equals(SEPARATOR);
    }

    private boolean isOpenBracket(String token) {
        return token.equals("(");
    }

    private boolean isCloseBracket(String token) {
        return token.equals(")");
    }

    private boolean isOperator(String token) {
        return OPERATORS.contains(token);
    }

    private byte getPrecedence(String token) {
        if (token.equals("+") || token.equals("-")) {
            return 1;
        }
        return 2;
    }

    public Stack<String> getStackRPN() {
        Stack<String> stackRPNCopy = (Stack<String>) stackRPN.clone();
        return stackRPNCopy;
    }

    public void parse(String expression) throws ParseException {
        if (expression.length() == 0) {
            throw new ParseException("выражение не может быть пустым.", 0);
        }
        if (countEntries(expression, "(") > countEntries(expression, ")")) {
            throw new ParseException("открывающих скобок больше чем закрывающих.", expression.lastIndexOf(')'));
        }
        if (countEntries(expression, "(") < countEntries(expression, ")")) {
            throw new ParseException("открывающих скобок меньше чем закрывающих.", expression.lastIndexOf('('));
        }

        // cleaning stacks
        stackOperations.clear();
        stackRPN.clear();

        // make some preparations
        expression = expression.replace(" ", "").replace("(-", "(0-")
                .replace(",-", ",0-");
        if (expression.charAt(0) == '-') {
            expression = "0" + expression;
        }
        // splitting input string into tokens
        StringTokenizer stringTokenizer = new StringTokenizer(expression,
                OPERATORS + SEPARATOR + "()", true);

        // loop for handling each token - shunting-yard algorithm
        int tokenIndex = 0;
        while (stringTokenizer.hasMoreTokens()) {
            tokenIndex++;
            String token = stringTokenizer.nextToken();
            if (isSeparator(token)) {
                while (!stackOperations.empty()
                        && !isOpenBracket(stackOperations.lastElement())) {
                    stackRPN.push(stackOperations.pop());
                }
            } else if (isOpenBracket(token)) {
                stackOperations.push(token);
            } else if (isCloseBracket(token)) {
                while (!stackOperations.empty()
                        && !isOpenBracket(stackOperations.lastElement())) {
                    stackRPN.push(stackOperations.pop());
                }
                stackOperations.pop();
                if (!stackOperations.empty()
                        && isFunction(stackOperations.lastElement())) {
                    stackRPN.push(stackOperations.pop());
                }
            } else if (isNumber(token)) {
                stackRPN.push(token);
            } else if (isOperator(token)) {
                while (!stackOperations.empty()
                        && isOperator(stackOperations.lastElement())
                        && getPrecedence(token) <= getPrecedence(stackOperations
                        .lastElement())) {
                    stackRPN.push(stackOperations.pop());
                }
                stackOperations.push(token);
            } else if (isFunction(token)) {
                stackOperations.push(token);
            } else {
                throw new ParseException("использован неизвестный токен.", tokenIndex);
            }
        }
        while (!stackOperations.empty()) {
            stackRPN.push(stackOperations.pop());
        }

        // reverse stack
        Collections.reverse(stackRPN);
    }
}
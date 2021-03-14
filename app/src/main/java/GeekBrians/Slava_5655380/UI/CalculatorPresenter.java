package GeekBrians.Slava_5655380.UI;

import android.text.TextUtils;
import android.view.inputmethod.InputConnection;

import java.text.ParseException;

import GeekBrians.Slava_5655380.Calculator.BinaryOperation;
import GeekBrians.Slava_5655380.Calculator.Calculator;
import GeekBrians.Slava_5655380.Calculator.MathNotationParsers.InfixToRPNConverter;
import GeekBrians.Slava_5655380.Calculator.MathNotationParsers.RPNSolver;
import GeekBrians.Slava_5655380.TextUtils.InfixNotationFormat;
import GeekBrians.Slava_5655380.TextUtils.InputConnectionTextFormat;
import GeekBrians.Slava_5655380.TextUtils.RegexpPatterns;

import static GeekBrians.Slava_5655380.TextUtils.StringAnalyzingUtil.*;

public class CalculatorPresenter {

    private InputConnection display;
    private BinaryOperation calculator;
    private char digitsSeparator;
    private InputConnectionTextFormat textFormat;

    private void commitText(String text) {
        display.commitText(String.valueOf(text), 1);
        textFormat.format(display);
    }

    private int getCursorPosition(InputConnection display) {
        String currentText = extractText(display);
        return display.getTextBeforeCursor(currentText.length(), 0).length();
    }

    public CalculatorPresenter(InputConnection display, Calculator calculator, byte numberOfDigitsToSeparate) {
        this.display = display;
        this.calculator = calculator;
        digitsSeparator = ' ';
        textFormat = new InfixNotationFormat(digitsSeparator, numberOfDigitsToSeparate);
    }

    public void keyClearPressed() {
        textFormat.clear(display);
    }

    public void keyPointPressed() {
        String currentText = extractText(display);
        if (currentText.length() == 0) {
            commitText("0.");
            return;
        }
        char currSymb = currentText.charAt(getCursorPosition(display) - 1);
        if (currSymb == digitsSeparator) {
            currSymb = currentText.charAt(getCursorPosition(display) - 2);
        }
        if (currSymb == ')') {
            commitText("*0.");
            return;
        }
        if (currSymb == '(' || currSymb == '+' || currSymb == '-' || currSymb == '*' || currSymb == '/') {
            commitText("0.");
            return;
        }
        if (currSymb == '.') {
            return;
        }
        commitText(".");
    }

    public void keyParenthesesPressed() {

        int cursorPosition = getCursorPosition(display);
        int shift = 1;
        String currText = extractText(display);
        char prevSymb = ((cursorPosition - shift < 0) ? 0 : currText.charAt(cursorPosition - shift));

        while (prevSymb == '(' || prevSymb == ')') {
            shift++;
            prevSymb = ((cursorPosition - shift < 0) ? 0 : currText.charAt(cursorPosition - shift));
        }
        if (cursorPosition - shift < 0 || isStringContains(String.valueOf(prevSymb), RegexpPatterns.MATH_OPERATION)) {
            commitText("(");
            return;
        }
        if (countEntries(currText, "(") == countEntries(currText, ")") && isStringContains(String.valueOf(prevSymb), RegexpPatterns.RATIONAL_NUMBER_DIGIT)) {
            commitText("*(");
            return;
        }

        commitText(")");
        return;
    }

    public void symbKeyPressed(char symb) {
        commitText(String.valueOf(symb));
    }

    public void keyResultPressed() throws ParseException {
        String expression = extractText(display);
        expression = expression.replaceAll(String.valueOf(digitsSeparator), "");
        InfixToRPNConverter infixToRPNConverter = new InfixToRPNConverter();
        infixToRPNConverter.parse(expression);
        RPNSolver rpnSolver = new RPNSolver(calculator, infixToRPNConverter.getStackRPN());
        String result = String.valueOf(rpnSolver.solve());
        keyClearPressed();
        display.commitText(result, 1);
        textFormat.format(display);
    }

    public void keyInversionPressed() {
        commitText("(-");
    }

    public void keyBackspacePressed() {
        String currentText = extractText(display);
        if (getCursorPosition(display) - 1 >= 0 && currentText.charAt(getCursorPosition(display) - 1) == digitsSeparator)
            display.setSelection(getCursorPosition(display) - 1, getCursorPosition(display) - 1);
        if (TextUtils.isEmpty(display.getSelectedText(0))) {
            display.deleteSurroundingText(1, 0);
        } else {
            display.commitText("", 1);
        }
        textFormat.format(display);
    }
}
package GeekBrians.Slava_5655380.UI;

import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import GeekBrians.Slava_5655380.Calculator.BinaryOperation;
import GeekBrians.Slava_5655380.Calculator.Calculator;
import GeekBrians.Slava_5655380.Calculator.mathNotationParsers.InfixToRPNConverter;
import GeekBrians.Slava_5655380.Calculator.mathNotationParsers.RPNSolver;

public class CalculatorPresenter {

    private InputConnection display;
    private BinaryOperation calculator;
    private char digitsSeparator;
    private final byte NUMBER_OF_DIGITS_TO_SEPARATE;

    public CalculatorPresenter(InputConnection display, Calculator calculator) {
        this.display = display;
        this.calculator = calculator;
        digitsSeparator = ' ';
        NUMBER_OF_DIGITS_TO_SEPARATE = 3;
    }

    public void keyClearPressed() {
        parenthesesToClose = 0;
        display.commitText("", 1); // снимает выделение если оно есть
        CharSequence currentText = display.getExtractedText(new ExtractedTextRequest(), 0).text;
        CharSequence beforCursorText = display.getTextBeforeCursor(currentText.length(), 0);
        CharSequence afterCursorText = display.getTextAfterCursor(currentText.length(), 0);
        display.deleteSurroundingText(beforCursorText.length(), afterCursorText.length());
    }

    private int parenthesesToClose = 0;

    // TODO: REFACTOR: Вынести patterns в свойства чтобы их каждый раз не перекомпилировать
    public void keyParenthesesPressed() {
        int cursorPosition = getCursorPosition(display);
        int shift = 1;
        char prevSymb = ((cursorPosition - shift < 0) ? 0 : display.getExtractedText(new ExtractedTextRequest(), 0).text.toString().charAt(cursorPosition - shift));

        while (prevSymb == '(' || prevSymb == ')') {
            shift++;
            prevSymb = ((cursorPosition - shift < 0) ? 0 : display.getExtractedText(new ExtractedTextRequest(), 0).text.toString().charAt(cursorPosition - shift));
        }
        if (cursorPosition - shift < 0 || Pattern.compile("[a-z]|[A-Z]|\\+|\\-|\\*|\\/|\\%").matcher(String.valueOf(prevSymb)).find()) {
            parenthesesToClose++;
            commitSymb('(');
            return;
        }
        if (parenthesesToClose == 0 && Pattern.compile("\\d|\\.").matcher(String.valueOf(prevSymb)).find()) {
            parenthesesToClose++;
            commitSymb('*');
            commitSymb('(');
            return;
        }

        parenthesesToClose--;
        commitSymb(')');
        return;
    }

    public void symbKeyPressed(char symb) {
        commitSymb(String.valueOf(symb).charAt(0));
    }

    public void keyResultPressed() throws ParseException {
        String expression = display.getExtractedText(new ExtractedTextRequest(), 0).text.toString();
        expression = expression.replaceAll(String.valueOf(digitsSeparator), "");
        InfixToRPNConverter infixToRPNConverter = new InfixToRPNConverter();
        infixToRPNConverter.parse(expression);
        RPNSolver rpnSolver = new RPNSolver(calculator, infixToRPNConverter.getStackRPN());
        keyClearPressed();
        display.commitText(String.valueOf(rpnSolver.solve()), 1);
        formatText(display);
    }

    public void keyBackspacePressed() {
        // TODO: обраюотать удаление пробелов(разделителей) и знаков
        if (TextUtils.isEmpty(display.getSelectedText(0))) {
            display.deleteSurroundingText(1, 0);
        } else {
            display.commitText("", 1);
        }
    }

    private void commitSymb(char symb) {
        display.commitText(String.valueOf(symb), 1);
        formatText(display);
    }

    private void formatText(InputConnection display) {
        int lastCursorPosition = getCursorPosition(display);
        String srcText = display.getExtractedText(new ExtractedTextRequest(), 0).text.toString();
        int lastNumberOfSeparators = countEntries(srcText, String.valueOf(digitsSeparator));
        String formatedText = insertSeparators(srcText);
        keyClearPressed();
        display.commitText(formatedText, 1); // TODO: REFACTOR: использовать параметр newCursorPosition метода .commitText вместо метода .setSelection
        int currNumberOfSeparators = countEntries(formatedText, String.valueOf(digitsSeparator));
        display.setSelection(lastCursorPosition + (currNumberOfSeparators - lastNumberOfSeparators), lastCursorPosition + (currNumberOfSeparators - lastNumberOfSeparators));
    }

    // TODO: REFACTOR: заменить все display.getExtractedText(new ExtractedTextRequest(), 0).text на extractedText(displat)
    private String extractedText(InputConnection display) {
        return display.getExtractedText(new ExtractedTextRequest(), 0).text.toString();
    }

    // TODO: REFACTOR: заменить все Pattern.compile(regexp).matcher(src).find() на isStringContains(src, pattern)
    private boolean isStringContains(String src, Pattern pattern) {
        return pattern.matcher(src).find();
    }

    private int getCursorPosition(InputConnection display) {
        CharSequence currentText = display.getExtractedText(new ExtractedTextRequest(), 0).text;
        return display.getTextBeforeCursor(currentText.length(), 0).length();
    }

    private String insertSeparators(String srcText) {
        srcText = srcText.replaceAll(String.valueOf(digitsSeparator), "");
        for (int i = srcText.length(); i > NUMBER_OF_DIGITS_TO_SEPARATE; i--) {
            if (Pattern.compile("\\D").matcher(srcText.substring(i - NUMBER_OF_DIGITS_TO_SEPARATE, i)).find())
                continue;
            if (Pattern.compile("\\D").matcher(srcText.substring(i - NUMBER_OF_DIGITS_TO_SEPARATE - 1, i - NUMBER_OF_DIGITS_TO_SEPARATE)).find())
                continue;
            srcText = srcText.substring(0, i - NUMBER_OF_DIGITS_TO_SEPARATE) + digitsSeparator + srcText.substring(i - NUMBER_OF_DIGITS_TO_SEPARATE, srcText.length());
        }
        return srcText;
    }

    public static int countEntries(String src, String value) {
        return src.length() - src.replace(value, "").length();
    }
}

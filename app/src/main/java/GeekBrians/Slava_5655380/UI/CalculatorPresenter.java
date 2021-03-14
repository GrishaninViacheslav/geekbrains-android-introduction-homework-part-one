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
        display.commitText("", 1); // снимает выделение если оно есть
        CharSequence currentText = display.getExtractedText(new ExtractedTextRequest(), 0).text;
        CharSequence beforCursorText = display.getTextBeforeCursor(currentText.length(), 0);
        CharSequence afterCursorText = display.getTextAfterCursor(currentText.length(), 0);
        display.deleteSurroundingText(beforCursorText.length(), afterCursorText.length());
    }

    public void keyPointPressed() {
        CharSequence currentText = display.getExtractedText(new ExtractedTextRequest(), 0).text;
        if (currentText.length() == 0) {
            commitSymb('0');
            commitSymb('.');
            return;
        }
        char currSymb = currentText.charAt(getCursorPosition(display) - 1);
        if (currSymb == digitsSeparator) {
            currSymb = currentText.charAt(getCursorPosition(display) - 2);
        }
        if (currSymb == ')') {
            commitSymb('*');
            commitSymb('0');
            commitSymb('.');
            return;
        }
        if (currSymb == '(' || currSymb == '+' || currSymb == '-' || currSymb == '*' || currSymb == '/') {
            commitSymb('0');
            commitSymb('.');
            return;
        }
        if (currSymb == '.') {
            return;
        }
        commitSymb('.');
    }

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
            commitSymb('(');
            return;
        }
        if (countEntries(display.getExtractedText(new ExtractedTextRequest(), 0).text.toString(), "(") == countEntries(display.getExtractedText(new ExtractedTextRequest(), 0).text.toString(), ")") && Pattern.compile("\\d|\\.").matcher(String.valueOf(prevSymb)).find()) {
            commitSymb('*');
            commitSymb('(');
            return;
        }

        commitSymb(')');
        return;
    }

    public void keyInversionPressed() {
        CharSequence currentText = display.getExtractedText(new ExtractedTextRequest(), 0).text;

        // ((5 000 000 + 1.2 555))
        // ((5 000 000 + 1.2 555))+

        // Поле пусто
        if (currentText.length() == 0) {
            commitSymb('(');
            commitSymb('-');
            return;
        }

        // Курсор находится после числа
        if (getCursorPosition(display) - 2 >= 0) {
            char prevSymb = currentText.charAt(getCursorPosition(display) - 2);
            boolean isPrevSymbNumber = Pattern.compile("\\d").matcher(String.valueOf(prevSymb)).find();
            if (isPrevSymbNumber) {
                int shift;
                for (shift = 0; getCursorPosition(display) - 2 - shift >= 0 && isPrevSymbNumber; shift++) {
                    prevSymb = currentText.charAt(getCursorPosition(display) - 2 - shift);
                    isPrevSymbNumber = Pattern.compile("\\d").matcher(String.valueOf(prevSymb)).find();
                }
                int cursorPosition = getCursorPosition(display);
                display.setSelection(cursorPosition - shift, cursorPosition - shift);
                commitSymb('(');
                commitSymb('-');
                return;
            }
        }
        // Курсор находится внутри числа перед пробелом
        // Курсор находится внутри числа после пробела
        // Курсор находится внутри числа перед точкой
        // Курсор находится внутри числа после точки
        // Курсор находится внутри числа между цифрами


        // Те же ситации но инверсия уже есть

        // Курсор находится после знака
        // Курсор находится перед числом
        // Курсор находится перед (
        // Курсор находится после (
        commitSymb('(');
        commitSymb('-');
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

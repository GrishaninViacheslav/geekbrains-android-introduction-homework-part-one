package GeekBrians.Slava_5655380.UI;

import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

import java.text.ParseException;
import java.util.regex.Pattern;

import GeekBrians.Slava_5655380.Calculator.BigDecimalBinaryOperation;
import GeekBrians.Slava_5655380.Calculator.Calculator;
import GeekBrians.Slava_5655380.Calculator.mathNotationParsers.InfixToRPNConverter;
import GeekBrians.Slava_5655380.Calculator.mathNotationParsers.RPNSolver;

public class CalculatorPresenter {

    private InputConnection display;
    private BigDecimalBinaryOperation calculator;
    private String displayValue;
    private char digitsSeparator;
    private byte currDigitsBeforeSeparator;
    private final byte NUMBER_OF_DIGITS_TO_SEPARATE;

    public CalculatorPresenter(InputConnection display, Calculator calculator) {
        this.display = display;
        this.calculator = calculator;
        displayValue = "";
        digitsSeparator = ' ';
        NUMBER_OF_DIGITS_TO_SEPARATE = 3;
        currDigitsBeforeSeparator = NUMBER_OF_DIGITS_TO_SEPARATE;
    }

    public void numKeyPressed(byte keyNumber) {
        commitSymb(String.valueOf(keyNumber).charAt(0));
    }

    public void keyClearPressed() {
        display.commitText("", 1); // снимает выделение если оно есть
        CharSequence currentText = display.getExtractedText(new ExtractedTextRequest(), 0).text;
        CharSequence beforCursorText = display.getTextBeforeCursor(currentText.length(), 0);
        CharSequence afterCursorText = display.getTextAfterCursor(currentText.length(), 0);
        display.deleteSurroundingText(beforCursorText.length(), afterCursorText.length());
    }

    // TODO: REFACTOR: Можно объеденить с numKeyPressed в общий метод symbKeyPressed()
    public void operatorKeyPressed(char operatorSymb){
        commitSymb(String.valueOf(operatorSymb).charAt(0));
    }

    public void keyResultPressed(){
        String expression = display.getExtractedText(new ExtractedTextRequest(), 0).text.toString();
        expression = expression.replaceAll(String.valueOf(digitsSeparator), "");
        InfixToRPNConverter infixToRPNConverter = new InfixToRPNConverter();
        try {
            infixToRPNConverter.parse(expression);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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



    // TODO: REFACTOR: Вынести эти методы в класс InputConnectionFormater
    private void formatText(InputConnection display) {
        int lastCursorPosition = getCursorPosition(display);
        String srcText = display.getExtractedText(new ExtractedTextRequest(), 0).text.toString();
        int lastNumberOfSeparators = countEntries(srcText, String.valueOf(digitsSeparator));
        String formatedText = insertSeparators(srcText);
        keyClearPressed();
        display.commitText(formatedText, 1); // TODO: REFACTOR: использовать параметр newCursorPosition метода .commitText вместо метода .setSelection
        int currNumberOfSeparators = countEntries(formatedText, String.valueOf(digitsSeparator));
        if(lastNumberOfSeparators != currNumberOfSeparators){
            display.setSelection(lastCursorPosition + 1, lastCursorPosition + 1);
        }
        else
            display.setSelection(lastCursorPosition, lastCursorPosition);
    }

    private int getCursorPosition(InputConnection display) {
        CharSequence currentText = display.getExtractedText(new ExtractedTextRequest(), 0).text;
        return display.getTextBeforeCursor(currentText.length(), 0).length();
    }

    private String insertSeparators(String srcText) {
        srcText =  srcText.replaceAll(String.valueOf(digitsSeparator), "");
        for (int i = srcText.length(); i > NUMBER_OF_DIGITS_TO_SEPARATE; i--) {
            if(Pattern.compile("\\D").matcher(srcText.substring(i - NUMBER_OF_DIGITS_TO_SEPARATE, i)).find())
                continue;
            if(Pattern.compile("\\D").matcher(srcText.substring(i - NUMBER_OF_DIGITS_TO_SEPARATE - 1, i - NUMBER_OF_DIGITS_TO_SEPARATE)).find())
                continue;
            srcText = srcText.substring(0, i - NUMBER_OF_DIGITS_TO_SEPARATE) + digitsSeparator + srcText.substring(i - NUMBER_OF_DIGITS_TO_SEPARATE, srcText.length());
        }
        return srcText;
    }

    private int countEntries(String src, String value){
        return src.length() - src.replace(value, "").length();
    }
}

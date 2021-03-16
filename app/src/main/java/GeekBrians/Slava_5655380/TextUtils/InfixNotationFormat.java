package GeekBrians.Slava_5655380.TextUtils;

import android.view.inputmethod.InputConnection;

import static GeekBrians.Slava_5655380.TextUtils.StringAnalyzingUtil.countEntries;
import static GeekBrians.Slava_5655380.TextUtils.StringAnalyzingUtil.extractText;
import static GeekBrians.Slava_5655380.TextUtils.StringAnalyzingUtil.isStringContains;

public class InfixNotationFormat implements InputConnectionTextFormat {
    private char digitsSeparator;
    private int NUMBER_OF_DIGITS_TO_SEPARATE;

    private String insertSeparators(String srcText) {
        srcText = srcText.replaceAll(String.valueOf(digitsSeparator), "");
        for (int i = srcText.length(); i > NUMBER_OF_DIGITS_TO_SEPARATE; i--) {
            if (
                    isStringContains(srcText.substring(i - NUMBER_OF_DIGITS_TO_SEPARATE, i), RegexpPatterns.NON_DIGIT)
                    || isStringContains(srcText.substring(i - NUMBER_OF_DIGITS_TO_SEPARATE - 1, i - NUMBER_OF_DIGITS_TO_SEPARATE), RegexpPatterns.NON_DIGIT)
            )
                continue;
            srcText = srcText.substring(0, i - NUMBER_OF_DIGITS_TO_SEPARATE) + digitsSeparator + srcText.substring(i - NUMBER_OF_DIGITS_TO_SEPARATE, srcText.length());
        }
        return srcText;
    }

    public InfixNotationFormat(char digitsSeparator, int NUMBER_OF_DIGITS_TO_SEPARATE) {
        this.digitsSeparator = digitsSeparator;
        this.NUMBER_OF_DIGITS_TO_SEPARATE = NUMBER_OF_DIGITS_TO_SEPARATE;
    }

    @Override
    public void format(InputConnection display) {
        int lastCursorPosition = display.getTextBeforeCursor(extractText(display).length(), 0).length();
        String srcText = extractText(display);
        int lastNumberOfSeparators = countEntries(srcText, String.valueOf(digitsSeparator));
        String formatedText = insertSeparators(srcText);
        clear(display);
        display.commitText(formatedText, 1);
        int currNumberOfSeparators = countEntries(formatedText, String.valueOf(digitsSeparator));
        display.setSelection(lastCursorPosition + (currNumberOfSeparators - lastNumberOfSeparators), lastCursorPosition + (currNumberOfSeparators - lastNumberOfSeparators));
    }

    @Override
    public void clear(InputConnection display) {
        display.commitText("", 1); // снимает выделение если оно есть
        String currentText = extractText(display);
        CharSequence beforCursorText = display.getTextBeforeCursor(currentText.length(), 0);
        CharSequence afterCursorText = display.getTextAfterCursor(currentText.length(), 0);
        display.deleteSurroundingText(beforCursorText.length(), afterCursorText.length());
    }
}
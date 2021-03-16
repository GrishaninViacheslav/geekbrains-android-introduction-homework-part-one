package GeekBrians.Slava_5655380.TextUtils;

import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

import java.util.regex.Pattern;

public class StringAnalyzingUtil {
    public static int countEntries(String src, String value) {
        return src.length() - src.replace(value, "").length();
    }

    public static String extractText(InputConnection display) {
        return display.getExtractedText(new ExtractedTextRequest(), 0).text.toString();
    }

    public static boolean isStringContains(String src, Pattern pattern) {
        return pattern.matcher(src).find();
    }
}

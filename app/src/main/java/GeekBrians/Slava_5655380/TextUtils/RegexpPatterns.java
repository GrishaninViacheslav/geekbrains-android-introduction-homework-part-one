package GeekBrians.Slava_5655380.TextUtils;

import java.util.regex.Pattern;

public class RegexpPatterns {
    public static final Pattern MATH_OPERATION = Pattern.compile("[a-z]|[A-Z]|\\+|\\-|\\*|\\/|\\%");
    public static final Pattern RATIONAL_NUMBER_DIGIT = Pattern.compile("\\d|\\.|\\,");
    public static final Pattern NON_DIGIT = Pattern.compile("\\D");
}

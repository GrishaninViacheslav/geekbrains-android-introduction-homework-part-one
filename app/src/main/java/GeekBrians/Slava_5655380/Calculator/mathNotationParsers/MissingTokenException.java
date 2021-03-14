package GeekBrians.Slava_5655380.Calculator.mathNotationParsers;

public class MissingTokenException extends RuntimeException {
    public MissingTokenException() {
        super("в выражении недостаёт информации.");
    }
}

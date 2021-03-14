package GeekBrians.Slava_5655380.Calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Calculator implements BinaryOperation {
    private int precision = 13;
    private RoundingMode roundingMode = RoundingMode.HALF_UP;

    @Override
    public String binaryOperation(String leftOperand, String rightOperand, Operation operation) {
        BigDecimal leftOperandBD = new BigDecimal(leftOperand,  new MathContext(precision));
        BigDecimal rightOperandBD = new BigDecimal(rightOperand,  new MathContext(precision));
        switch (operation) {
            case ADD:
                return stripTrailingZerosFixed(leftOperandBD.add(rightOperandBD).setScale(precision, roundingMode)).toPlainString();
            case SUB:
                return stripTrailingZerosFixed(leftOperandBD.subtract(rightOperandBD).setScale(precision, roundingMode)).toPlainString();
            case MULT:
                return stripTrailingZerosFixed(leftOperandBD.multiply(rightOperandBD).setScale(precision, roundingMode)).toPlainString();
            case DIV:
                return stripTrailingZerosFixed(leftOperandBD.divide(rightOperandBD, precision, roundingMode)).toPlainString();
        }
        return "NaN";
    }

    // https://bugs.java.com/bugdatabase/view_bug.do?bug_id=6480539
    private BigDecimal stripTrailingZerosFixed(BigDecimal bigDecimal){
        BigDecimal zero = new BigDecimal("0");
        if ( bigDecimal.compareTo(zero) == 0 ) {
            return zero;
        } else {
            return bigDecimal.stripTrailingZeros();
        }
    }
}

package GeekBrians.Slava_5655380.Calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Calculator implements BinaryOperation {
    private int precision = 16;
    private RoundingMode roundingMode = RoundingMode.HALF_UP;

    @Override
    public String binaryOperation(String leftOperand, String rightOperand, Operation operation) {
        BigDecimal leftOperandBD = new BigDecimal(leftOperand,  new MathContext(precision));
        BigDecimal rightOperandBD = new BigDecimal(rightOperand,  new MathContext(precision));
        switch (operation) {
            case ADD:
                return leftOperandBD.add(rightOperandBD).setScale(precision, roundingMode).stripTrailingZeros().toPlainString();
            case SUB:
                return rightOperandBD.subtract(leftOperandBD).setScale(precision, roundingMode).stripTrailingZeros().toPlainString(); // TODO: разобраться почему операнды пришлось поменять местами
            case MULT:
                return leftOperandBD.multiply(rightOperandBD).setScale(precision, roundingMode).stripTrailingZeros().toPlainString();
            case DIV:
                return rightOperandBD.divide(leftOperandBD, precision, roundingMode).stripTrailingZeros().toPlainString(); // TODO: разобраться почему операнды пришлось поменять местами
        }
        return "NaN";
    }
}

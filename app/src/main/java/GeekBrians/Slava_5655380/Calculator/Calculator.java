package GeekBrians.Slava_5655380.Calculator;

import java.math.BigDecimal;

public class Calculator implements BigDecimalBinaryOperation {
    @Override
    public String binaryOperation(BigDecimal leftOperand, BigDecimal rightOperand, Operation operation) {
        switch (operation){
            case ADD:
                return leftOperand.add(rightOperand).toString();
            case SUB:
                return leftOperand.subtract(rightOperand).toString();
            case MULT:
                return leftOperand.multiply(rightOperand).toString();
            case DIV:
                return leftOperand.divide(rightOperand).toString();
        }
        return "NaN";
    }
}

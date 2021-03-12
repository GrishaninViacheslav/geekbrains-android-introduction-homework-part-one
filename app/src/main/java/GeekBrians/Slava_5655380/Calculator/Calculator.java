package GeekBrians.Slava_5655380.Calculator;

import java.math.BigDecimal;

public class Calculator implements BigDecimalBinaryOperation {
    @Override
    public double binaryOperation(BigDecimal leftArg, BigDecimal rightArg, Operation operation) {
        switch (operation){
            case ADD:
                return leftArg.add(rightArg).doubleValue();
        }
        return 0;
    }
}

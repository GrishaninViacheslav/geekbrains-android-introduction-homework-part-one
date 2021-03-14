package GeekBrians.Slava_5655380.UI;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;

import GeekBrians.Slava_5655380.Calculator.Calculator;
import GeekBrians.Slava_5655380.Calculator.MathNotationParsers.MissingTokenException;
import GeekBrians.Slava_5655380.R;

public class MainActivity extends AppCompatActivity {
    private EditText calculatorDispaly;
    private CalculatorPresenter calculatorPresenter;

    private void setOnClickEvent(@IdRes int id, Runnable func) {
        findViewById(id).setOnClickListener((View v) -> {
            func.run();
        });

    }

    private void setSymbButtonEvent(@IdRes int id, char symb) {
        findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorPresenter.symbKeyPressed(symb);
            }
        });
    }

    private void initButtonsListeners() {
        setOnClickEvent(R.id.button_point, calculatorPresenter::keyPointPressed);
        setOnClickEvent(R.id.button_parentheses, calculatorPresenter::keyParenthesesPressed);
        setOnClickEvent(R.id.button_clear, calculatorPresenter::keyClearPressed);
        setOnClickEvent(R.id.button_inversion, calculatorPresenter::keyInversionPressed);
        setOnClickEvent(R.id.button_backspace, calculatorPresenter::keyBackspacePressed);

        setSymbButtonEvent(R.id.button_0, Character.forDigit((byte) 0, 10));
        setSymbButtonEvent(R.id.button_1, Character.forDigit((byte) 1, 10));
        setSymbButtonEvent(R.id.button_2, Character.forDigit((byte) 2, 10));
        setSymbButtonEvent(R.id.button_3, Character.forDigit((byte) 3, 10));
        setSymbButtonEvent(R.id.button_4, Character.forDigit((byte) 4, 10));
        setSymbButtonEvent(R.id.button_5, Character.forDigit((byte) 5, 10));
        setSymbButtonEvent(R.id.button_6, Character.forDigit((byte) 6, 10));
        setSymbButtonEvent(R.id.button_7, Character.forDigit((byte) 7, 10));
        setSymbButtonEvent(R.id.button_8, Character.forDigit((byte) 8, 10));
        setSymbButtonEvent(R.id.button_9, Character.forDigit((byte) 9, 10));
        setSymbButtonEvent(R.id.button_addition, '+');
        setSymbButtonEvent(R.id.button_subtraction, '-');
        setSymbButtonEvent(R.id.button_multiplication, '*');
        setSymbButtonEvent(R.id.button_division, '/');

        findViewById(R.id.button_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    calculatorPresenter.keyResultPressed();
                } catch (ParseException | MissingTokenException e) {
                    Toast.makeText(getApplicationContext(), "Использован недопустимый формат: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (ArithmeticException e) {
                    Toast.makeText(getApplicationContext(), "Нельзя делить на ноль.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calculatorDispaly = findViewById(R.id.display);
        calculatorDispaly.setShowSoftInputOnFocus(false);
        calculatorPresenter = new CalculatorPresenter(calculatorDispaly.onCreateInputConnection(new EditorInfo()), new Calculator(), (byte) 3);
        initButtonsListeners();
    }
}
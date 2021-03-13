package GeekBrians.Slava_5655380.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;

import GeekBrians.Slava_5655380.Calculator.Calculator;
import GeekBrians.Slava_5655380.R;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "[MainActivity]";
    private EditText calculatorDispaly;
    CalculatorPresenter calculatorPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calculatorDispaly = findViewById(R.id.display);
        calculatorDispaly.setShowSoftInputOnFocus(false);
        calculatorPresenter = new CalculatorPresenter(calculatorDispaly.onCreateInputConnection(new EditorInfo()), new Calculator());
        initButtonsListeners();
    }

    // TODO: REFACTOR: Вынести реализацию событий в класс CalculatorDispaly, который посылает сообщение displayResult

    public void initButtonsListeners() {
        findViewById(R.id.button_0).setOnClickListener(makeSymbButtonListener(Character.forDigit((byte) 0, 10)));
        findViewById(R.id.button_1).setOnClickListener(makeSymbButtonListener(Character.forDigit((byte) 1, 10)));
        findViewById(R.id.button_2).setOnClickListener(makeSymbButtonListener(Character.forDigit((byte) 2, 10)));
        findViewById(R.id.button_3).setOnClickListener(makeSymbButtonListener(Character.forDigit((byte) 3, 10)));
        findViewById(R.id.button_4).setOnClickListener(makeSymbButtonListener(Character.forDigit((byte) 4, 10)));
        findViewById(R.id.button_5).setOnClickListener(makeSymbButtonListener(Character.forDigit((byte) 5, 10)));
        findViewById(R.id.button_6).setOnClickListener(makeSymbButtonListener(Character.forDigit((byte) 6, 10)));
        findViewById(R.id.button_7).setOnClickListener(makeSymbButtonListener(Character.forDigit((byte) 7, 10)));
        findViewById(R.id.button_8).setOnClickListener(makeSymbButtonListener(Character.forDigit((byte) 8, 10)));
        findViewById(R.id.button_9).setOnClickListener(makeSymbButtonListener(Character.forDigit((byte) 9, 10)));

        findViewById(R.id.button_addition).setOnClickListener(makeSymbButtonListener('+'));
        findViewById(R.id.button_subtraction).setOnClickListener(makeSymbButtonListener('-'));
        findViewById(R.id.button_multiplication).setOnClickListener(makeSymbButtonListener('*'));
        findViewById(R.id.button_division).setOnClickListener(makeSymbButtonListener('/'));

        findViewById(R.id.button_parentheses).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorPresenter.keyParenthesesPressed();
            }
        });


        findViewById(R.id.button_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorPresenter.keyClearPressed();
            }
        });

        findViewById(R.id.button_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    calculatorPresenter.keyResultPressed();
                } catch (ParseException e) {
                    Toast.makeText(getApplicationContext(), "Использован недопустимый формат: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private View.OnClickListener makeSymbButtonListener(char symb) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorPresenter.symbKeyPressed(symb);
            }
        };
    }


    public void log(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, message);
    }
}
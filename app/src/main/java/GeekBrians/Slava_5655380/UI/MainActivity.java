package GeekBrians.Slava_5655380.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

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
        calculatorPresenter = new CalculatorPresenter( calculatorDispaly.onCreateInputConnection(new EditorInfo()), new Calculator());
        initButtonsListeners();
    }

    // TODO: REFACTOR: Вынести реализацию событий в класс CalculatorDispaly, который посылает сообщение displayResult

    public void initButtonsListeners() {
        findViewById(R.id.button_0).setOnClickListener(makeNumButtonListener((byte) 0));
        findViewById(R.id.button_1).setOnClickListener(makeNumButtonListener((byte) 1));
        findViewById(R.id.button_2).setOnClickListener(makeNumButtonListener((byte) 2));
        findViewById(R.id.button_3).setOnClickListener(makeNumButtonListener((byte) 3));
        findViewById(R.id.button_4).setOnClickListener(makeNumButtonListener((byte) 4));
        findViewById(R.id.button_5).setOnClickListener(makeNumButtonListener((byte) 5));
        findViewById(R.id.button_6).setOnClickListener(makeNumButtonListener((byte) 6));
        findViewById(R.id.button_7).setOnClickListener(makeNumButtonListener((byte) 7));
        findViewById(R.id.button_8).setOnClickListener(makeNumButtonListener((byte) 8));
        findViewById(R.id.button_9).setOnClickListener(makeNumButtonListener((byte) 9));


        findViewById(R.id.button_clear).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                calculatorPresenter.keyClearPressed();
            }
        });
    }

    private View.OnClickListener makeNumButtonListener(byte keyNum){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorPresenter.numKeyPressed(keyNum);
            }
        };
    }


    public void log(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, message);
    }
}
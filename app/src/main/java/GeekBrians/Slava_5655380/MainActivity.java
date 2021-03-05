package GeekBrians.Slava_5655380;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

import org.w3c.dom.ls.LSOutput;

public class MainActivity extends AppCompatActivity {

    private ToggleButton task1Button,
            task2Button,
            task3Button;

    private void setTask1Activity() {
        setContentView(R.layout.task_1_activity);
        setListeners();
        task1Button.setChecked(true);
        task2Button.setChecked(false);
        task3Button.setChecked(false);
    }

    private void setTask2Activity() {
        setContentView(R.layout.task_2_activity);
        setListeners();
        task1Button.setChecked(false);
        task2Button.setChecked(true);
        task3Button.setChecked(false);

    }

    private void setTask3Activity() {
        setContentView(R.layout.task_3_activity);
        setListeners();
        task1Button.setChecked(false);
        task2Button.setChecked(false);
        task3Button.setChecked(true);
    }

    private void setListeners() {
        task1Button = findViewById(R.id.task_1_button);
        task2Button = findViewById(R.id.task_2_button);
        task3Button = findViewById(R.id.task_3_button);
        task1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTask1Activity();
            }
        });

        task2Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setTask2Activity();
            }
        });

        task3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTask3Activity();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTask1Activity();
    }
}
package GeekBrians.Slava_5655380;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private ToggleButton task1Button,
            task2Button,
            task3Button;

    private void setTask1Activity() {
        setContentView(R.layout.task_1_activity);
        setHeader(true, false, false);
        setPushMeButton();
        setSwitchers();
    }

    private void setTask2Activity() {
        pushCounter = 0;
        setContentView(R.layout.task_2_activity);
        setHeader(false, true, false);
    }

    private void setTask3Activity() {
        pushCounter = 0;
        setContentView(R.layout.task_3_activity);
        setHeader(false, false, true);
    }

    private void setHeader(boolean task1ButtonState, boolean task2ButtonState, boolean task3ButtonState) {
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
        task1Button.setChecked(task1ButtonState);
        task2Button.setChecked(task2ButtonState);
        task3Button.setChecked(task3ButtonState);
    }

    private int pushCounter = 0;

    private void setPushMeButton() {
        Button pushMeButton = findViewById(R.id.push_me_button);
        pushMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pushCounter < 240)
                    pushCounter++;
                setTitle();
            }
        });
    }

    private void setTitle() {
        TextView title = findViewById(R.id.title_text);
        TextView subtitle = findViewById(R.id.subtitle_text);
        CheckBox useNameCheckBox = findViewById(R.id.use_name_checkbox);
        EditText nameField = findViewById(R.id.name_field);
        switch (pushCounter) {
            case 1:
                title.setTextSize(30);
                title.setText(R.string.title_string);
            case 2:
                title.setText(R.string.title_string_1);
                break;
            case 3:
                title.setText(R.string.title_string_2);
                break;
            case 10:
                title.setText(R.string.title_string_3);
                break;
            case 30:
                title.setText(R.string.title_string_4);
                break;
            case 60:
                title.setText(R.string.title_string_5);
                break;
            case 120:
                title.setText(R.string.title_string_6);
                break;
            case 240:
                title.setText(R.string.title_string_7);
                break;
        }
        if (useNameCheckBox.isChecked())
            subtitle.setText(nameField.getText());
        else
            subtitle.setText(R.string.subtitle_default_string);
    }

    private void setSwitchers() {
        Switch switch1 = findViewById(R.id.switch_1);
        Switch switch2 = findViewById(R.id.switch_2);
        ToggleButton statementToggle = findViewById(R.id.satement_toggle);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(statementToggle.isChecked())
                    switch2.setChecked(!isChecked);
                else
                    switch2.setChecked(isChecked);
            }
        });
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(statementToggle.isChecked())
                    switch1.setChecked(!isChecked);
                else
                    switch1.setChecked(isChecked);
            }
        });
        statementToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    switch1.setChecked(!switch2.isChecked());
                else
                    switch1.setChecked(switch2.isChecked());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTask1Activity();
    }
}
package GeekBrians.Slava_5655380.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import GeekBrians.Slava_5655380.R;

public class SettingsActivity extends AppCompatActivity {

    private static final String THEME_ID_BUNDLE_KEY = "THEME_ID";
    private int currTheme = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            currTheme = savedInstanceState.getInt(THEME_ID_BUNDLE_KEY);
        } else {
            currTheme = getIntent().getIntExtra(THEME_ID_BUNDLE_KEY, currTheme);
        }
        switch (currTheme) {
            case 0:
                setTheme(R.style.Theme_Geekbrainsandroidintroductionhomework);
                break;
            case 1:
                setTheme(R.style.Theme_WhiteStyle);
                break;
        }
        setContentView(R.layout.activity_settings);
        findViewById(R.id.toggle_theme_original).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currTheme = 0;
                recreate();
            }
        });
        findViewById(R.id.toggle_theme_white).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currTheme = 1;
                Log.d("[fuck]", "toggle_theme_white currTheme: " + currTheme);
                recreate();
            }
        });
        findViewById(R.id.button_settings_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentResult = new Intent();
                Log.d("[fuck]", "button_settings_back currTheme: " + currTheme);
                intentResult.putExtra(THEME_ID_BUNDLE_KEY, currTheme);
                setResult(RESULT_OK, intentResult);
                finish();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle instanceState) {
        super.onSaveInstanceState(instanceState);
        instanceState.putInt(THEME_ID_BUNDLE_KEY, currTheme);
    }
}
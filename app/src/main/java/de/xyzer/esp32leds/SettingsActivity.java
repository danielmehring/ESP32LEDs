package de.xyzer.esp32leds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class SettingsActivity extends AppCompatActivity {

    TextInputEditText textInput, textInputPassword;
    TextView displayAdress, displayPassword;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String ADDRESS = "adress";
    public static final String PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        textInput = findViewById(R.id.textInput);
        textInputPassword = findViewById(R.id.textInputPassword);
        displayAdress = findViewById(R.id.savedIP);
        displayPassword = findViewById(R.id.savedPassword);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        displayAdress.setText(sharedPreferences.getString(ADDRESS, "0.0.0.0"));
        displayPassword.setText(sharedPreferences.getString(PASSWORD, "end"));
    }

    public void saveIP(View view) {
        if(!textInput.getText().toString().matches("")) {
            displayAdress.setText(textInput.getText());
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(ADDRESS, textInput.getText().toString());
            editor.apply();
            MainActivity.httpAdress = textInput.getText().toString();
        }

        if(!textInputPassword.getText().toString().matches("")) {
            displayPassword.setText(textInputPassword.getText());
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(PASSWORD, textInputPassword.getText().toString());
            editor.apply();
            MainActivity.password = textInputPassword.getText().toString();
        }
    }
}
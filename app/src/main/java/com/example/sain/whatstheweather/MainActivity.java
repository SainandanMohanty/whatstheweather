package com.example.sain.whatstheweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    String apiKey = BuildConfig.ApiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        EditText editText = findViewById(R.id.editText);
        String city = editText.getText().toString();

        String json = null;

        DownloadTask downloadTask = new DownloadTask();
        try {
            json = downloadTask.execute(String.format(Locale.getDefault(),
                    "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", city, apiKey)).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView textView = findViewById(R.id.textView);
        TextView textView1 = findViewById(R.id.textView2);

        if (json == null) {
            textView.setText("No results :(");
            textView1.setText("Try a different keyword");
        }

        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);

            String main = weather.getString("main");
            String description = weather.getString("description");

            textView.setText(main);
            textView1.setText(description);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

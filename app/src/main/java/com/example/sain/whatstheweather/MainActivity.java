package com.example.sain.whatstheweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
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

        if (city.equals("")) {
            addView("Search field empty", "Enter the name of a city");
            return;
        }

        String json = null;

        DownloadTask downloadTask = new DownloadTask();
        try {
            json = downloadTask.execute(String.format(Locale.getDefault(),
                    "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", city, apiKey)).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (json == null) {
            addView("No results :(", "Try a different keyword");
            return;
        }

        TableRow tableRow = findViewById(R.id.tableRow);
        tableRow.removeAllViews();
        TableRow tableRow1 = findViewById(R.id.tableRow2);
        tableRow1.removeAllViews();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("weather");
            JSONObject weather;

            for (int i = 0; i < jsonArray.length(); i++) {
                weather = jsonArray.getJSONObject(i);
                addView(weather.getString("main"), weather.getString("description"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addView(String primary, String secondary) {
        TableRow tableRow = findViewById(R.id.tableRow);
        TableRow tableRow1 = findViewById(R.id.tableRow2);

        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1);

        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setText(primary);
        textView.setTextAppearance(this, android.R.style.TextAppearance_Material_Medium);
        tableRow.addView(textView, layoutParams);

        TextView textView1 = new TextView(this);
        textView1.setGravity(Gravity.CENTER);
        textView1.setText(secondary);
        tableRow1.addView(textView1, layoutParams);
    }
}

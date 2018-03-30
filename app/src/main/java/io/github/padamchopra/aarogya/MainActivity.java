package io.github.padamchopra.aarogya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SharedPreferences sharedPreferences = getSharedPreferences("Aarogya", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("firsttime", true)) {
            startActivity(new Intent(MainActivity.this, Profile.class));
        }
        loadReminders();
        if (sharedPreferences.getInt("totalreminders", 0) > 0) {
            checkForExpiries();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_reminder) {
            startActivity(new Intent(MainActivity.this, reminder.class));
        } else if (item.getItemId() == R.id.action_drop_points) {
            startActivity(new Intent(MainActivity.this,drop_points.class));
        } else if (item.getItemId() == R.id.action_profile) {
            startActivity(new Intent(MainActivity.this, Profile.class));
        } else if (item.getItemId() == R.id.action_about) {
            startActivity(new Intent(MainActivity.this, about.class));
        } else if (item.getItemId() == R.id.action_home) {
            Toast.makeText(this, "Already on home page", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadReminders() {
        SharedPreferences sharedPreferences = getSharedPreferences("Aarogya", Context.MODE_PRIVATE);
        if (sharedPreferences.getInt("totalreminders", 0) == 0) {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.home_active_display);
            linearLayout.setVisibility(View.GONE);
            linearLayout = (LinearLayout) findViewById(R.id.home_no_active_display);
            linearLayout.setVisibility(View.VISIBLE);
            TextView textView = (TextView) findViewById(R.id.home_total_reminders_tv);
            textView.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) findViewById(R.id.home_total_reminders_tv);
            int total_reminders = sharedPreferences.getInt("remindersexhausted", 0);
            String toDisplay = "Reminded you about " + total_reminders + " expiries till date!";
            textView.setText(toDisplay);

            String[] medicineNames = sharedPreferences.getString("medicinenames", "").split("newmed");
            String[] expiryDates = sharedPreferences.getString("medicineexpiries", "").split(" ");
            ListView listView = (ListView) findViewById(R.id.home_active_reminder_listview);
            List<Map<String, String>> data = new ArrayList<Map<String, String>>();
            for (int i = medicineNames.length - 1; i >= 0; i--) {
                Map<String, String> datum = new HashMap<String, String>();
                datum.put("title", medicineNames[i]);
                datum.put("expiry-date", expiryDates[i]);
                data.add(datum);
            }
            SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[]{"title", "expiry-date"}, new int[]{android.R.id.text1, android.R.id.text2});
            listView.setAdapter(adapter);
        }
    }

    public void checkForExpiries() {
        SharedPreferences sharedPreferences = getSharedPreferences("Aarogya", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String[] medicineNames = sharedPreferences.getString("medicinenames", "").split("newmed");
        String[] expiryDates = sharedPreferences.getString("medicineexpiries", "").split(" ");
        String meds = "";
        String dates = "";
        int exhausted = 0;
        for (int i = 0; i < expiryDates.length; i++) {
            if (checkDateifBefore(expiryDates[i].split("/"))) {
                String medicineName = medicineNames[i];
                Toast.makeText(this, "Removed expired medicine: " + medicineName, Toast.LENGTH_LONG).show();
                exhausted++;
            } else {
                if (meds.length() == 0) {
                    meds += medicineNames[i];
                    dates += expiryDates[i];
                } else {
                    meds += "newmed" + medicineNames[i];
                    dates += " " + expiryDates[i];
                }
            }
        }
        editor.putString("medicinenames", meds);
        editor.putString("medicineexpiries", dates);
        editor.putInt("remindersexhausted", sharedPreferences.getInt("remindersexhausted", 0) + exhausted);
        editor.putInt("totalreminders", sharedPreferences.getInt("totalreminders", 10) - exhausted);
        editor.apply();
        loadReminders();
    }

    public boolean checkDateifBefore(String[] expiryDate) {
        String[] todayDate = new SimpleDateFormat("dd/MM/yy",Locale.ENGLISH).format(new Date()).split("/");
        int year = Integer.parseInt(todayDate[2]);
        int month = Integer.parseInt(todayDate[1]);
        int date = Integer.parseInt(todayDate[0]);
        if (year == Integer.parseInt(expiryDate[2])) {
            if (month == Integer.parseInt(expiryDate[1])) {
                if (date >= Integer.parseInt(expiryDate[0])) {
                    return true;
                } else {
                    return false;
                }
            } else if (month > Integer.parseInt(expiryDate[1])) {
                return true;
            } else {
                return false;
            }
        } else if (year > Integer.parseInt(expiryDate[2])) {
            return true;
        } else {
            return false;
        }
    }
}

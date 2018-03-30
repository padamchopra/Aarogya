package io.github.padamchopra.aarogya;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class reminder extends AppCompatActivity {

    private String expiryDate;
    private Calendar myCalendar;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mProgress = new ProgressDialog(this);
        myCalendar = Calendar.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("Aarogya", Context.MODE_PRIVATE);
        String user_full_name = sharedPreferences.getString("userfirstname","random") + " " + sharedPreferences.getString("userlastname","random");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Reminders").child(user_full_name).push();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_reminder) {
            Toast.makeText(this, "Already on the reminder page", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.action_drop_points) {
            startActivity(new Intent(reminder.this,drop_points.class));
            finish();
        } else if (item.getItemId() == R.id.action_profile) {
            startActivity(new Intent(reminder.this, Profile.class));
            finish();
        } else if (item.getItemId() == R.id.action_about) {
            startActivity(new Intent(reminder.this, about.class));
            finish();
        } else if (item.getItemId() == R.id.action_home) {
            startActivity(new Intent(reminder.this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setExpiryDate(View view) {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
                expiryDate = sdf.format(myCalendar.getTime());
                Button datePickerBtn = (Button) findViewById(R.id.reminder_set_date_btn);
                datePickerBtn.setBackgroundColor(getResources().getColor(R.color.success));
                datePickerBtn.setText(expiryDate);
            }
        };
        new DatePickerDialog(reminder.this, date, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void createReminder(View view){
        mProgress.setMessage("Setting reminder");
        mProgress.show();
        EditText med_name_et = (EditText) findViewById(R.id.reminder_med_name_et);
        String medicine = med_name_et.getText().toString();
        EditText manufacture_name_et = (EditText) findViewById(R.id.reminder_manufacturer_name_et);
        String manufacture = manufacture_name_et.getText().toString();
        if(TextUtils.isEmpty(manufacture)){
            manufacture = "notset";
        }
        if(!TextUtils.isEmpty(medicine) && !TextUtils.isEmpty(expiryDate)){
            mDatabase.child("Name").setValue(medicine);
            mDatabase.child("Manufacturer").setValue(manufacture);
            mDatabase.child("Expiry").setValue(expiryDate);
            mProgress.dismiss();
            SharedPreferences sharedPreferences = getSharedPreferences("Aarogya",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("totalreminders",sharedPreferences.getInt("totalreminders",0)+1);
            String medicinenames = sharedPreferences.getString("medicinenames","");
            String medicineexpiries = sharedPreferences.getString("medicineexpiries","");
            if(medicinenames.equals("")){
                medicinenames = medicine;
                medicineexpiries = expiryDate;
            }else{
                medicinenames += "newmed" + medicine;
                medicineexpiries += " " + expiryDate;
            }
            editor.putString("medicinenames",medicinenames);
            editor.putString("medicineexpiries",medicineexpiries);
            editor.apply();
            Toast.makeText(this, "Reminder added successfully", Toast.LENGTH_SHORT).show();
            mProgress.dismiss();


            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 17);
            calendar.set(Calendar.MINUTE, 00);
            calendar.set(Calendar.SECOND, 0);
            Intent intent1 = new Intent(reminder.this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(reminder.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) reminder.this.getSystemService(reminder.this.ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

            startActivity(new Intent(reminder.this,MainActivity.class));
            finish();
        }else{
            Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
            mProgress.dismiss();
        }
    }
}

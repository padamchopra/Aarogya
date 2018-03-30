package io.github.padamchopra.aarogya;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Profile extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationClient;
    private ProgressDialog mProgress;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mProgress = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        EditText firstname_et = (EditText) findViewById(R.id.profile_first_name_et);
        EditText lastname_et = (EditText) findViewById(R.id.profile_last_name_et);
        EditText reference_et = (EditText) findViewById(R.id.profile_reference_et);
        SharedPreferences sharedPreferences = getSharedPreferences("Aarogya",Context.MODE_PRIVATE);
        firstname_et.setText(sharedPreferences.getString("userfirstname",""));
        lastname_et.setText(sharedPreferences.getString("userlastname",""));
        reference_et.setText(sharedPreferences.getString("userreference",""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        SharedPreferences sharedPreferences = getSharedPreferences("Aarogya", Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("firsttime",true)){
            Toast.makeText(this,"Please fill in the details to proceed",Toast.LENGTH_LONG).show();
        }else{
            if(item.getItemId() == R.id.action_add_reminder){
                startActivity(new Intent(Profile.this,reminder.class));
                finish();
            } else if(item.getItemId() == R.id.action_drop_points){
                startActivity(new Intent(Profile.this,drop_points.class));
                finish();
            } else if(item.getItemId() == R.id.action_profile){
                Toast.makeText(this,"Already on profile page",Toast.LENGTH_SHORT).show();
            } else if(item.getItemId() == R.id.action_about){
                startActivity(new Intent(Profile.this,about.class));
                finish();
            } else if(item.getItemId() == R.id.action_home){
                startActivity(new Intent(Profile.this,MainActivity.class));
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }




    public void saveDetails(View view){
        mProgress.setMessage("Saving ...");
        mProgress.show();
        EditText firstname_et = (EditText) findViewById(R.id.profile_first_name_et);
        EditText lastname_et = (EditText) findViewById(R.id.profile_last_name_et);
        EditText reference_et = (EditText) findViewById(R.id.profile_reference_et);
        String firstname = firstname_et.getText().toString();
        String lastname = lastname_et.getText().toString();
        String reference = reference_et.getText().toString();

        if(!TextUtils.isEmpty(firstname) && !TextUtils.isEmpty(lastname)){
            if(TextUtils.isEmpty(reference)){
                reference = "notset";
            }
            SharedPreferences sharedPreferences = getSharedPreferences("Aarogya",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if(sharedPreferences.getBoolean("firsttime",true)){
                editor.putBoolean("firsttime",false);
                editor.apply();
            }
            editor.putString("userfirstname",firstname);
            editor.putString("userlastname",lastname);
            editor.putString("userreference",reference);
            editor.apply();
            String fullname = firstname + " " + lastname;
            mDatabase.child(fullname).child("Name").setValue(fullname);
            mDatabase.child(fullname).child("Reference").setValue(reference);
            String timeStamp = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            mDatabase.child(fullname).child("UpdationTime").setValue(timeStamp);
            Toast.makeText(Profile.this,"Details Updated",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Profile.this,MainActivity.class));
            finish();
        } else {
          Toast.makeText(this,"Fill your first and last name to proceed",Toast.LENGTH_LONG).show();
        }
        mProgress.dismiss();
    }
}

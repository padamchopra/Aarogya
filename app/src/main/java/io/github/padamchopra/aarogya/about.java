package io.github.padamchopra.aarogya;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class about extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.action_add_reminder){
            startActivity(new Intent(about.this,reminder.class));
            finish();
        } else if(item.getItemId() == R.id.action_drop_points){
            startActivity(new Intent(about.this,drop_points.class));
            finish();
        } else if(item.getItemId() == R.id.action_profile){
            startActivity(new Intent(about.this,Profile.class));
            finish();
        } else if(item.getItemId() == R.id.action_about){
            Toast.makeText(this,"Already on about page",Toast.LENGTH_SHORT).show();
        } else if(item.getItemId() == R.id.action_home){
            startActivity(new Intent(about.this,MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void redirectToPadam(View view){
        redirect("https://padamchopra.github.io/");
    }

    public void redirectToFB(View view){
        redirect("https://www.facebook.com/aarogya.aisg46/");
    }

    private void redirect(String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}

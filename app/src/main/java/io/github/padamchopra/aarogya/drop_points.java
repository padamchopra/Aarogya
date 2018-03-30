package io.github.padamchopra.aarogya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class drop_points extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_points);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toast.makeText(this,"Drop your expired medicines at our collection boxes",Toast.LENGTH_LONG).show();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_maps_fragment);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(28.4548187,77.032949));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);

        googleMap.moveCamera(center);
        googleMap.moveCamera(zoom);

        MarkerOptions samvit = new MarkerOptions();
        samvit.position(new LatLng(28.4349991, 77.032949));
        samvit.title("Samvit Healthcare");
        googleMap.addMarker(samvit);

        MarkerOptions deep = new MarkerOptions();
        deep.position(new LatLng(28.4039104,77.2988629));
        deep.title("Deep Medical Centre");
        googleMap.addMarker(deep);

        MarkerOptions womenpolice = new MarkerOptions();
        womenpolice.position(new LatLng(28.4440573,77.0443758));
        womenpolice.title("Women Police Station");
        googleMap.addMarker(womenpolice);

        MarkerOptions southcity = new MarkerOptions();
        southcity.position(new LatLng(28.4178668,77.0469072));
        southcity.title("South City 2 RWA Office");
        googleMap.addMarker(southcity);

        MarkerOptions uppal = new MarkerOptions();
        uppal.position(new LatLng(28.4087443,77.0428338));
        uppal.title("Uppal Southend RWA Office");
        googleMap.addMarker(uppal);

        MarkerOptions belaire = new MarkerOptions();
        belaire.position(new LatLng(28.4488617,77.0993634));
        belaire.title("The Belaire by DLF");
        googleMap.addMarker(belaire);

        MarkerOptions vipul = new MarkerOptions();
        vipul.position(new LatLng(28.4164724,77.0340053));
        vipul.title("Vipul Greens");
        googleMap.addMarker(vipul);

        MarkerOptions mcg = new MarkerOptions();
        mcg.position(new LatLng(28.4295858,77.0003406));
        mcg.title("MCG Office Gurgaon");
        googleMap.addMarker(mcg);

        MarkerOptions civil = new MarkerOptions();
        civil.position(new LatLng(28.4454371,77.0080662));
        civil.title("Civil Hospital Gurgaon");
        googleMap.addMarker(civil);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_reminder) {
            startActivity(new Intent(drop_points.this, reminder.class));
            finish();
        } else if (item.getItemId() == R.id.action_drop_points) {
            Toast.makeText(this, "Already on drop points page", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.action_profile) {
            startActivity(new Intent(drop_points.this, Profile.class));
            finish();
        } else if (item.getItemId() == R.id.action_about) {
            startActivity(new Intent(drop_points.this, about.class));
            finish();
        } else if (item.getItemId() == R.id.action_home) {
            startActivity(new Intent(drop_points.this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

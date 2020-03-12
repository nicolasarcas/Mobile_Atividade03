package com.example.gps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button btnAtivar;
    private Button btnDesativar;
    private Button btnIniciar;
    private Button btnTerminar;
    private Button btnPermisao;

    private FloatingActionButton floatingActionButton;

    private EditText txtDistancia;
    private EditText txtTempo;
    private EditText txtProcurar;

    private static final int GPS_REQUEST_CODE = 1001;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private void configurarGPS(){
        locationManager =
                (LocationManager)
                        getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAtivar = findViewById(R.id.btnAtivar);
        btnDesativar = findViewById(R.id.btnDesativar);
        btnIniciar = findViewById(R.id.btnIniciar);
        btnTerminar = findViewById(R.id.btnTerminar);
        btnPermisao = findViewById(R.id.btnPermisao);

        floatingActionButton = findViewById(R.id.floatingActionButton);

        txtDistancia = findViewById(R.id.txtDistancia);
        txtTempo = findViewById(R.id.txtTempo);
        txtProcurar = findViewById(R.id.txtProcurar);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(
                        String.format(
                                Locale.getDefault(),
                                ""
                        )
                );
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        uri
                );
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

        btnDesativar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.removeUpdates(locationListener);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }
}

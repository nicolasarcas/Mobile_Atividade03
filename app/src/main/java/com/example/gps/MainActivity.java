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
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private double lat;
    private double longi;
    private double distancia=0f;

    private Button btnAtivar;
    private Button btnDesativar;
    private Button btnIniciar;
    private Button btnTerminar;
    private Button btnPermisao;

    private FloatingActionButton floatingActionButton;

    private EditText txtDistancia;
    private EditText txtProcurar;

    private Chronometer chronometer;

    private static final int GPS_REQUEST_CODE = 1001;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private Location inicio;

    private boolean localizacao=false;
    private boolean permissao=false;
    private boolean percurso=false;
    private boolean comeco=false;

    private void configurarGPS(){
        locationManager =
                (LocationManager)
                        getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                longi = location.getLongitude();

                if (percurso){
                    if (comeco){
                        inicio = location;
                        comeco = false;
                    }
                    distancia += inicio.distanceTo(location);

                    String textoDistancia = String.format(Locale.getDefault(),"%.2f",distancia);
                    inicio = location;
                    txtDistancia.setText(textoDistancia);
                }
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
        txtProcurar = findViewById(R.id.txtProcurar);

        chronometer = findViewById(R.id.chronometer);

        configurarGPS();

        btnPermisao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity.this, getString(R.string.jaPermitido),Toast.LENGTH_SHORT).show();
                }
                else{

                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            },
                            GPS_REQUEST_CODE //DIFERENCIA A QUE PEDIDO FOI DADA A RESPOSTA
                    );

                    permissao= true;
                    Toast.makeText(MainActivity.this, getString(R.string.permissaoOK),Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnAtivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(permissao){
                    if(!localizacao){

                        if(ActivityCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED){ //SE A PERMISSÃO FOI DADA, ATIVA LOCALIZAÇÃO

                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    1000, //tempo minimo desde a ultima atualização
                                    5, //distancia minima desde a ultima atualização da location
                                    locationListener);
                        }
                        localizacao=true;
                        Toast.makeText(MainActivity.this, getString(R.string.ativarGPS),Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, getString(R.string.jaEstaAtivado),Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, getString(R.string.pedirPermissao),Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnDesativar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(permissao){
                    if(!percurso){
                        if(localizacao){
                            locationManager.removeUpdates(locationListener);
                            Toast.makeText(MainActivity.this, getString(R.string.desativarGPS),Toast.LENGTH_SHORT).show();
                            localizacao=false;
                        }
                        else{
                            Toast.makeText(MainActivity.this,getString(R.string.jaEstaDesativado),Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this,getString(R.string.emPercurso),Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, getString(R.string.pedirPermissao),Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(permissao){
                    if(localizacao){
                        if (!percurso){
                            //falta aqui]
                            percurso = true;
                            comeco = true;
                            chronometer.setBase(SystemClock.elapsedRealtime());//colocando um tempo base para iniciar o cronometro
                            chronometer.start();

                            Toast.makeText(MainActivity.this, getString(R.string.percursoIniciado),Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this, getString(R.string.percursoJaIniciado),Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this, getString(R.string.jaEstaDesativado),Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, getString(R.string.pedirPermissao),Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnTerminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (permissao){
                    if(localizacao){
                        if(percurso){
                            //falta aqui
                            chronometer.stop();
                            Toast.makeText(MainActivity.this,txtDistancia.getText()+" em "+chronometer.getText() + "\n" +
                                    getString(R.string.percursoFinalizado),Toast.LENGTH_SHORT).show();
                            percurso = false;
                            chronometer.setText("00:00");
                            txtDistancia.setText("0");
                        }
                        else{
                            Toast.makeText(MainActivity.this, getString(R.string.percursoJaFinalizado),Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this, getString(R.string.jaEstaDesativado),Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, getString(R.string.pedirPermissao),Toast.LENGTH_SHORT).show();
                }
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(
                        String.format(
                                Locale.getDefault(),
                                "geo:%f,%f?q="+txtProcurar.getText(),
                                lat,
                                longi
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == GPS_REQUEST_CODE){
            //resposta pelo GPS
            if(grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED){

                }
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1000,
                        1,
                        locationListener);
            }
            else{
                Toast.makeText(
                        this, getString(R.string.no_gps_no_app),
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}

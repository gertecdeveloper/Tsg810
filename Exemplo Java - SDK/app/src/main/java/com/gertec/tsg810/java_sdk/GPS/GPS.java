package com.gertec.tsg810.java_sdk.GPS;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.gertec.tsg810.java_sdk.R;

import java.util.Date;
import java.util.List;

public class GPS extends AppCompatActivity implements View.OnClickListener {

    public static final int LOCATION_CODE = 301;
    public static final int MSG_STOP_LOCATION = 0x1;
    long minTime = 2000;
    private LocationManager mLocationManager;
    private String mProvider = null;
    private TextView mLocation = null;
    private Criteria mCriteria;

    public LocationListener mLocationListener = new LocationListener() {
        // Esta função é acionada quando o status de disponível, temporariamente indisponível e
        // nenhum serviço é alterado diretamente.
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        // Esta função é acionada quando o Provedor está habilitado, como o GPS está ligado
        @Override
        public void onProviderEnabled(@NonNull String provider) {
        }

        //  Esta função é acionada quando está desabilitada, como quando o GPS é desligado.
        @Override
        public void onProviderDisabled(@NonNull String provider) {
        }

        //Esta função é acionada quando as coordenadas mudam, caso o Provedor passe nas mesmas
        // coordenadas não será acionado.
        @SuppressLint("SetTextI18n")
        @Override
        public void onLocationChanged(@NonNull Location location) {
            // Exibe a longitude e a latitude da localização geográfica
            if (mLocation != null) {
                mLocation.setText("Local alterado para:\n \nLatitude: " + location.getLatitude()
                        + "\nLongitude: " + location.getLongitude()
                        + "\nProvedor: " + mProvider
                        + "\nTempo: " + new Date());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gps);

        //ativa o modo full screen
        ativarFullScreen();

        //inicia o servico de localizacao
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        List<String> providers = mLocationManager.getProviders(true);
        for (String provider : providers) {
            Log.d(TAG, "onCreate: =" + provider);
        }

        mCriteria = createFineCriteria();
        setupViews();
    }

    public void setupViews() {
        mLocation = findViewById(R.id.location);
        Button mGpsButton = findViewById(R.id.btgps);
        mGpsButton.setOnClickListener(this);
        Button mNetworkButton = findViewById(R.id.btninternet);
        mNetworkButton.setOnClickListener(this);
    }

    @SuppressLint({"SetTextI18n", "MissingSuperCall"})
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                try {
                    mProvider = mLocationManager.getBestProvider(mCriteria, true);
                    mLocationManager.requestLocationUpdates(mProvider, minTime, 1, mLocationListener);

                    // Solicita atualizações da localização
                    mLocation.setText("onRequestPermissionsResult: Solicitar atualizações de localização, " + "\nProvedor: " + mProvider);
                    Location location = mLocationManager.getLastKnownLocation(mProvider);
                    if (location != null) {
                        if (mLocation != null) {

                            //Exibe o ultimo local caso a localização tenha mudado
                            mLocation.setText("onRequestPermissionsResult: " + "\nUltimo local conhecido: \nLatitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude() + "\nProvedor: " + mProvider);
                        }
                    } else {
                        if (mLocation != null) {

                            //Mensagem de "não foi possivel localizar"
                            mLocation.setText("onRequestPermissionsResult: \nNão foi possivel localizar " + "usando " + mProvider + " como provedor");
                        }
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            } else {
                if (mLocation != null) {
                    mLocation.setText("Solicitar permissões de localização.");
                }
                finish();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        String buttonName = "";

        //Declaração dos botões dentro do textView
        if (v.getId() == R.id.btgps) {
            mProvider = LocationManager.GPS_PROVIDER;
            buttonName = "Via GPS\n";
        } else if (v.getId() == R.id.btninternet) {
            mProvider = LocationManager.NETWORK_PROVIDER;
            buttonName = "Via Internet\n";
        }

        // Obtenha permissões (se as permissões não estiverem ativadas, uma caixa de diálogo
        // aparecerá perguntando se as permissões devem ser ativadas)
        if (ContextCompat.checkSelfPermission(GPS.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(GPS.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GPS.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
        } else {
            mLocationManager.removeUpdates(mLocationListener);
            mLocationManager.requestLocationUpdates(mProvider, minTime, 1, mLocationListener);
            if (mLocation != null) {
                mLocation.setText(buttonName + "Solicitar atualizações de localização, \nProvedor: " + mProvider);
                Location location = mLocationManager.getLastKnownLocation(mProvider);
                if (location != null) {
                    mLocation.setText(buttonName + "\nUltimo local conhecido: \nLatitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude() + "\nProvedor: " + mProvider);
                } else {
                    mLocation.setText(buttonName + "\nNão é possível localizar usando " + mProvider + " como provedor.");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocation();
    }

    public void stopLocation() {
        try {
            mLocationManager.removeUpdates(mLocationListener);
            Log.d(TAG, "stopLocation: removeUpdates");
        } catch (Exception e) {
            Log.e(TAG, "stopLocation: e=" + e);
        }
    }

    //Critérios
    public Criteria createFineCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // Alta precisão
        criteria.setAltitudeRequired(false); // Contém informações de altura
        criteria.setBearingRequired(false); // Contém informações de orientação
        criteria.setCostAllowed(false); // Permitir pagamento
        criteria.setSpeedRequired(false); // Contém informações de velocidade
        criteria.setPowerRequirement(Criteria.POWER_HIGH); // Consumo de energia
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_LOW);
        return criteria;
    }


    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_STOP_LOCATION) {
                Log.d(TAG, "Localicação interrompida.");
                stopLocation();
            }
        }
    }

    private void ativarFullScreen() {
        // Ativa o modo full screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
        }

        View decorView = getWindow().getDecorView();
        WindowInsetsControllerCompat controller = ViewCompat.getWindowInsetsController(decorView);

        if (controller != null) {
            // Oculta as barras de sistema
            controller.hide(WindowInsetsCompat.Type.systemBars());
            // Permite que as barras apareçam com swipe
            controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }
    }
}
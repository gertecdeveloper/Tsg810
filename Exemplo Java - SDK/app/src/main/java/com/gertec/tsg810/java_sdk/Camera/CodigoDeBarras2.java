package com.gertec.tsg810.java_sdk.Camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.gertec.tsg810.java_sdk.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class CodigoDeBarras2 extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    static final String ACESSO_NEGADO = "Permissão negada à câmera";

    private boolean flash = false;

    private ViewGroup contentFrame;

    private ZXingScannerView mScannerView;

    private TextView txtLeitura;

    private Button btnFlash;

    int StartCameraFlag = 0;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_codigo_de_barras2);

        txtLeitura = findViewById(R.id.textLeitura);
        btnFlash = findViewById(R.id.btnStart);

        contentFrame = (ViewGroup) findViewById(R.id.frameLayout2);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);

        //ativar full screen
        ativarFullScreen();

        ActivityCompat.requestPermissions(CodigoDeBarras2.this,
                new String[]{Manifest.permission.CAMERA},
                1);


        //botao do flash
        btnFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flash) {
                    mScannerView.setFlash(false);
                    flash = false;
                    btnFlash.setText("Flash - Desligado");
                } else {
                    mScannerView.setFlash(true);
                    flash = true;
                    btnFlash.setText("Flash - Ligado");
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mScannerView != null)
            mScannerView.stopCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(CodigoDeBarras2.this, ACESSO_NEGADO, Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void handleResult(Result result) {
        //Aciona o beep
        MediaPlayer mp;
        mp = MediaPlayer.create(getApplicationContext(), R.raw.beep);// the song is a filename which i have pasted inside a folder **raw** created under the **res** folder.//
        mp.start();

        //retorno do codigo
        AlertDialog alertDialog = new AlertDialog.Builder(CodigoDeBarras2.this).create();
        alertDialog.setTitle("Código " + result.getBarcodeFormat());
        alertDialog.setMessage(result.getBarcodeFormat() + ": " + result.getText());
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startCamera();
                    }
                });
        alertDialog.show();

        txtLeitura.setText(result.getBarcodeFormat() + ": " + result.getText());
        startCamera();
    }

    protected void startCamera() {
        contentFrame.setVisibility(View.VISIBLE);
        mScannerView.setResultHandler(this);
        mScannerView.setAutoFocus(true);

        if (Build.MODEL.equals("TSG810")) {
            mScannerView.startCamera();
        } else {
            if (StartCameraFlag == 0) {
                mScannerView.startCamera();
                StartCameraFlag = 1;
            } else {
                mScannerView.resumeCameraPreview(this);
            }

        }
    }

    protected void stopCamera() {
        mScannerView.stopCamera();
        contentFrame.setVisibility(View.INVISIBLE);
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
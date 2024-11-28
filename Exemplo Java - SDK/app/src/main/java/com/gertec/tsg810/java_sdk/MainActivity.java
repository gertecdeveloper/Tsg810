package com.gertec.tsg810.java_sdk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.gertec.tsg810.java_sdk.Camera.CodigoDeBarras;
import com.gertec.tsg810.java_sdk.Camera.CodigoDeBarras2;
import com.gertec.tsg810.java_sdk.GPS.GPS;
import com.gertec.tsg810.java_sdk.Impressora.Impressora;
import com.gertec.tsg810.java_sdk.LeituraMifare.NfcExemplo;

public class MainActivity extends AppCompatActivity {

    private Button btnPrinter, btnScanner, btnCamera, btnGPS, btnMifare, infor;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ativarFullScreen();

        btnPrinter = findViewById(R.id.btnImpressora);
        btnScanner = findViewById(R.id.btnScanner);
        btnCamera = findViewById(R.id.btnCamera);
        btnGPS = findViewById(R.id.btGPS);
        btnMifare = findViewById(R.id.btnMifare);
        infor = findViewById(R.id.inforstatus);

        // Ação para abrir o diálogo ao clicar no botão "Infor"
        infor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Constrói o AlertDialog
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Informações")
                        .setMessage("Equipamento: TSG810\nSDK: TSG810-Printer.jar\nLinguagem: Java\nVersão do app: 1.0.0")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); // Fecha o diálogo ao clicar em OK
                            }
                        }).create();

                // Exibe o AlertDialog
                alertDialog.show();
            }
        });

        //botão para a tela scanner zxing
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, CodigoDeBarras.class);
                startActivity(intent);
            }
        });

        btnScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, CodigoDeBarras2.class);
                startActivity(intent);
            }
        });

        //botao para a tela de impressao
        btnPrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, Impressora.class);
                startActivity(intent);
            }
        });

        //botão para a tela mifare
        btnMifare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, NfcExemplo.class);
                startActivity(intent);
            }
        });

        //botão para a tela GPS
        btnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, GPS.class);
                startActivity(intent);
            }
        });

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
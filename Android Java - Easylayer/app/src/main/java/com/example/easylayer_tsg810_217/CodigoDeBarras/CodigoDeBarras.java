package com.example.easylayer_tsg810_217.CodigoDeBarras;

import static br.com.gertec.easylayer.codescanner.CodeScanner.ALL_CODE_TYPES;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easylayer_tsg810_217.R;

import br.com.gertec.easylayer.codescanner.CodeScanner;
import br.com.gertec.easylayer.zxing.google.zxing.integration.android.IntentIntegrator;
import br.com.gertec.easylayer.zxing.google.zxing.integration.android.IntentResult;

public class CodigoDeBarras extends AppCompatActivity {

    private TextView outputView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo_de_barras);

        outputView = findViewById(R.id.outputView);

        //Botão de scan
        Button btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CodeScanner codeScanner = CodeScanner.getInstance();
                codeScanner.scanCode(CodigoDeBarras.this, ALL_CODE_TYPES);
            }
        });
    }

    //Rertorno do código lido
    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String content = result.getContents();
            String codeType = result.getFormatName();
            if (content != null && codeType != null) {
                //O resultado sera exibido no outupView
                outputView.setText(" Tipo de Código: " + codeType + "\n Conteúdo: " + content);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

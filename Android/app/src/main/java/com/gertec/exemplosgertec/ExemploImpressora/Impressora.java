package com.gertec.exemplosgertec.ExemploImpressora;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import com.gertec.exemplosgertec.MainActivity;
import com.gertec.exemplosgertec.R;

import java.io.IOException;


public class Impressora extends AppCompatActivity implements View.OnClickListener {
    private TextView printConnnected;
    private TextView status;
    private EditText mTextBox;
    private ImageButton btnBold;
    private ImageButton btnUnderline;
    private ImageButton btnAlignLeft;
    private ImageButton btnAlignRight;
    private ImageButton btnAlignCenter;
    private Spinner spSize;
    private Button btnPrintText;

    private Spinner spHeight;
    private Spinner spWidth;
    private Spinner spType;
    private Button btnPrintBarCode;

    private Button btnPrintImageDefault;
    private Button btnPrintAllFunctions;

    private Printer printer;

    private boolean bold = false;
    private boolean underline = false;
    private boolean alignLeft = false;
    private boolean alignCenter = false;
    private boolean alignRight = false;

    PrintConfig config;
    ConnectBluetooth connectBluetooth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.impressora);

        initComponents();
        setOnClick();

        connectBluetooth.findBT();
        try {
            connectBluetooth.openBT();
            printer = new Printer(connectBluetooth.getOutputStream(), connectBluetooth.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        printConnnected.setText(connectBluetooth.findBT());
//        initThread();
    }

    public void setOnClick(){
        btnBold.setOnClickListener(this);
        btnUnderline.setOnClickListener(this);
        btnAlignLeft.setOnClickListener(this);
        btnAlignCenter.setOnClickListener(this);
        btnAlignRight.setOnClickListener(this);
        btnPrintText.setOnClickListener(this);
        btnPrintBarCode.setOnClickListener(this);
        btnPrintImageDefault.setOnClickListener(this);
        btnPrintAllFunctions.setOnClickListener(this);
    }


    public void initComponents(){
        status = findViewById(R.id.status);
        btnPrintText = findViewById(R.id.btn_print_text);
        printConnnected = findViewById(R.id.print_connected);
        mTextBox = findViewById(R.id.my_text_box);
        btnBold = findViewById(R.id.btn_bold);
        btnUnderline = findViewById(R.id.btn_underlined);
        btnAlignLeft = findViewById(R.id.btn_align_left);
        btnAlignRight = findViewById(R.id.btn_align_right);
        btnAlignCenter = findViewById(R.id.btn_align_center);
        spSize = findViewById(R.id.spSize);
        spHeight = findViewById(R.id.spHeight);
        spWidth = findViewById(R.id.spWidth);
        spType = findViewById(R.id.spBarType);
        btnPrintBarCode = findViewById(R.id.btn_print_bar_code);
        btnPrintImageDefault = findViewById(R.id.btn_print_image_default);
        btnPrintAllFunctions = findViewById(R.id.btn_print_all_functions);
        connectBluetooth = new ConnectBluetooth();
        config = new PrintConfig("",10,0,2);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bold:
                try {
                    if (bold) {
                        resetFormat();
                    } else {
                        resetFormat();
                        btnBold.setBackgroundResource(R.drawable.shape_btn_selected);
                        btnBold.setImageResource(R.drawable.ic_bold_white);
                        bold = true;
                        config.setFormatacaoDaLetra(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_underlined:
                try {
                    if (underline) {
                        resetFormat();
                    } else {
                        resetFormat();
                        btnUnderline.setBackgroundResource(R.drawable.shape_btn_selected);
                        btnUnderline.setImageResource(R.drawable.ic_underlined_white);
                        underline = true;
                        config.setFormatacaoDaLetra(2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_align_left:
                try {
                    if (!alignLeft) {
                        resetAlinhamento();
                        btnAlignLeft.setBackgroundResource(R.drawable.shape_btn_selected);
                        btnAlignLeft.setImageResource(R.drawable.ic_align_left_white);
                        alignLeft = true;
                        config.setAlinhamentoDoTexto(1);
                    } else {
                        resetAlinhamento();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_align_center:
                try {
                    if (!alignCenter) {
                        resetAlinhamento();
                        btnAlignCenter.setBackgroundResource(R.drawable.shape_btn_selected);
                        btnAlignCenter.setImageResource(R.drawable.ic_align_center_white);
                        alignCenter = true;
                        config.setAlinhamentoDoTexto(2);
                    } else {
                        resetAlinhamento();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_align_right:
                try {
                    if (!alignRight) {
                        resetAlinhamento();
                        btnAlignRight.setBackgroundResource(R.drawable.shape_btn_selected);
                        btnAlignRight.setImageResource(R.drawable.ic_align_right_white);
                        alignRight = true;
                        config.setAlinhamentoDoTexto(3);

                    } else {
                        resetAlinhamento();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_print_text:
                try {
                    int tamanhoDaFonte = Integer.parseInt(
                            spSize.getItemAtPosition(spSize.getSelectedItemPosition()).toString());

                    config.setTexto(mTextBox.getText().toString());
                    config.setTamanhoDaLetra(tamanhoDaFonte);
                    printer.imprimiTexto(config);
                    printer.sendData("\n\n\n");
                    Toast.makeText(getApplicationContext(), "Imprimindo", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_print_bar_code:
                try{
                    config.setTamanhoCodigoDeBarras(Integer.parseInt(
                            spHeight.getItemAtPosition(spHeight.getSelectedItemPosition()).toString()));

                    config.setLarguraCodigoDeBarras(Integer.parseInt(
                            spWidth.getItemAtPosition(spWidth.getSelectedItemPosition()).toString()));

                    String tipoCodigoDeBarras =
                            spType.getItemAtPosition(spType.getSelectedItemPosition()).toString();

                    typeBarcode(tipoCodigoDeBarras);
                    Toast.makeText(getApplicationContext(), "Imprimindo", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_print_image_default:
                try{
                    printer.imprimeCupom();
                    Toast.makeText(getApplicationContext(), "Imprimindo", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_print_all_functions:
                    imprimirTodasAsFuncoes();
                break;
        }
    }

    public void typeBarcode(String BarcodeType) throws IOException {
        int type;
        switch (BarcodeType.toUpperCase()){
            case "EAN_13":
                type = 67;
                printer.imprimeCodigoDeBarras("123456789123",type,config.getTamanhoCodigoDeBarras(),
                        config.getLarguraCodigoDeBarras(),0,0);
                break;
            case "EAN_8":
                type = 68;
                printer.imprimeCodigoDeBarras("12345678",type,config.getTamanhoCodigoDeBarras(),
                        config.getLarguraCodigoDeBarras(),0,0);
                break;
            case "CODE_128":
                type = 73;
                printer.imprimeCodigoDeBarras("CODE128",type,config.getTamanhoCodigoDeBarras(),
                        config.getLarguraCodigoDeBarras(),0,0);
                break;
            case "PDF_417":
                printer.printPdf417("https://www.gertec.com.br/",5,
                        5,30,90,50);
                break;
            case "QR_CODE":
                printer.qrcode(5,"https://www.gertec.com.br/");
                break;
        }
    }

    public void resetAlinhamento(){
        config.setAlinhamentoDoTexto(2);
        btnAlignLeft.setBackgroundResource(R.drawable.ic_align_left);
        btnAlignLeft.setImageResource(R.drawable.ic_align_left);
        alignLeft = false;

        btnAlignCenter.setBackgroundResource(R.drawable.ic_align_center);
        btnAlignCenter.setImageResource(R.drawable.ic_align_center);
        alignCenter = false;

        btnAlignRight.setBackgroundResource(R.drawable.ic_align_right);
        btnAlignRight.setImageResource(R.drawable.ic_align_right);
        alignRight = false;
    }

    public void resetFormat(){
        config.setFormatacaoDaLetra(0);
        btnBold.setBackgroundResource(R.drawable.ic_bold);
        btnBold.setImageResource(R.drawable.ic_bold);
        bold = false;

        btnUnderline.setBackgroundResource(R.drawable.ic_underlined);
        btnUnderline.setImageResource(R.drawable.ic_underlined);
        underline = false;
    }

    public void imprimirTodasAsFuncoes(){
        try{
            Toast.makeText(getApplicationContext(), "Imprimindo", Toast.LENGTH_LONG).show();

            config.setTamanhoDaLetra(0);

            config.setTexto("-----Texto Normal-----\n");
            printer.imprimiTexto(config);


            config.setTexto("-----Texto em Negrito-----\n");
            config.setFormatacaoDaLetra(1);
            printer.imprimiTexto(config);


            config.setTexto("-----Texto Sublinhado-----\n\n");
            config.setFormatacaoDaLetra(2);
            printer.imprimiTexto(config);


            config.setFormatacaoDaLetra(0);

            config.setTexto("Texto Alinhado a Esquerda");
            config.setAlinhamentoDoTexto(1);
            printer.imprimiTexto(config);
            config.setTexto("\n----------\n");
            config.setAlinhamentoDoTexto(2);
            printer.imprimiTexto(config);

            config.setTexto("Texto Alinhado no centro");
            config.setAlinhamentoDoTexto(2);
            printer.imprimiTexto(config);
            printer.sendData("\n----------\n");

            config.setTexto("Texto Alinhado a Direita");
            config.setAlinhamentoDoTexto(3);
            printer.imprimiTexto(config);
            config.setTexto("\n----------\n");
            config.setAlinhamentoDoTexto(2);
            printer.imprimiTexto(config);


            config.setTamanhoCodigoDeBarras(160);

            config.setLarguraCodigoDeBarras(240);

            //EAN_13
            config.setTexto("\n-----EAN_13-----\n\n");
            config.setAlinhamentoDoTexto(2);
            printer.imprimiTexto(config);
            printer.imprimeCodigoDeBarras("123456789123",67,config.getTamanhoCodigoDeBarras(),
                    config.getLarguraCodigoDeBarras(),0,0);

            //EAN_8
            config.setTexto("\n-----EAN_8-----\n\n");
            printer.imprimiTexto(config);
            printer.imprimeCodigoDeBarras("12345678",68,config.getTamanhoCodigoDeBarras(),
                    config.getLarguraCodigoDeBarras(),0,0);

            //CODE_128
            config.setTexto("\n-----CODE_128-----\n\n");
            printer.imprimiTexto(config);
            printer.imprimeCodigoDeBarras("CODE128",73,config.getTamanhoCodigoDeBarras(),
                    config.getLarguraCodigoDeBarras(),0,0);

            //PDF_417
            config.setTexto("\n-----PDF_417-----\n");
            printer.imprimiTexto(config);
            printer.printPdf417("https://www.gertec.com.br/",5,
                    5,30,90,50);

            //QR_CODE
            config.setTexto("\n-----QR_CODE-----\n");
            printer.imprimiTexto(config);
            printer.qrcode(8,"https://www.gertec.com.br/");

            printer.imprimeCupom();

            Toast.makeText(getApplicationContext(), "Imprimindo", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    void initThread(){
        MyRunnable threadStatus = new MyRunnable();
        new Thread(threadStatus).start();
    }

    class MyRunnable implements Runnable {

        @Override
        public void run() {
            final int teste = 0;
            while (true){


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String printStatus = null;
                        try {
                            printStatus = String.valueOf(printer.printerStatus());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        status.setText(printStatus);
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
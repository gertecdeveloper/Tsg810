package com.gertec.exemplosgertec.ExemploImpressora;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import com.ftpos.library.smartpos.device.Device;
import com.ftpos.library.smartpos.servicemanager.OnServiceConnectCallback;
import com.gertec.exemplosgertec.MainActivity;
import com.gertec.exemplosgertec.R;

import java.io.IOException;


public class Impressora extends AppCompatActivity implements View.OnClickListener {
    private TextView printConnnected;
    private TextView status;
    private Button btnPrintText;
    private Button btnPrintImageDefault;
    private Button btnPrintAllFunctions;
    private FTPrntr mPrinter = null;
    private Device device;
    private Context mContext;
    private com.ftpos.library.smartpos.servicemanager.ServiceManager service;
    private EditText edTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.impressora);

        initComponents();
        setOnClick();

        mContext = getApplicationContext();
        service.bindPosServer(this, new OnServiceConnectCallback() {
            @Override
            public void onSuccess() {
                mPrinter.getInstance(mContext);
                device = Device.getInstance(mContext);
                Log.e("binding", "Success");
                //Toast.makeText(mContext,"getPaperUsage is:"+mFTPrinter.getUsedPaperLenManage(),Toast.LENGTH_SHORT).show();
                Log.e("printer","getPaperUsage: "+mPrinter.getUsedPaperLenManage());
            }

            @Override
            public void onFail(int var1) {
                Log.e("binding", "onFail");
            }
        });

    }

    public void setOnClick(){
        btnPrintText.setOnClickListener(this);
        btnPrintImageDefault.setOnClickListener(this);
        btnPrintAllFunctions.setOnClickListener(this);
    }


    public void initComponents(){
        status = findViewById(R.id.status);
        edTxt = findViewById(R.id.my_text_box);
        btnPrintText = findViewById(R.id.btn_print_text);
        printConnnected = findViewById(R.id.print_connected);
        btnPrintImageDefault = findViewById(R.id.btn_print_image_default);
        btnPrintAllFunctions = findViewById(R.id.btn_print_all_functions);
        mPrinter = new FTPrntr();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_print_text:
                try {
                        String text = null;
                        if (edTxt.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Digite um texto", Toast.LENGTH_SHORT).show();
                        }else{
                            text = edTxt.getText().toString();
                            imprimeTexto(text);
                            Toast.makeText(getApplicationContext(), "Imprimindo", Toast.LENGTH_SHORT).show();
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_print_image_default:
                try{
                    imprimeCupom();
                    Toast.makeText(getApplicationContext(), "Imprimindo", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_print_all_functions:
                    Bitmap bitmapx = BitmapFactory.decodeResource(getResources(),R.drawable.android_gertec);
                    Bitmap bitmapx1 = BitmapFactory.decodeResource(getResources(),R.drawable.img);
                    //imprimirTodasAsFuncoes(bitmapx);
                    imprimirTodasAsFuncoes(bitmapx1);
                break;
        }
    }

    /*public void typeBarcode(String BarcodeType) throws IOException {
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
    }*/


    public void imprimirTodasAsFuncoes(Bitmap bitmap){
        int ret;
        //ret = printer.getStatus(sta);
        ret = mPrinter.getStatus();
        if(ret != 0){
            Log.e("F100_test","Print getStatus error：0x"+int2HexStr(ret));
            return ;
        }
        Log.e("F100_test","Print getStatus success:");

        boolean bHavePaper = mPrinter.isHavePaper();
        if(!bHavePaper) {
            //setTextOnUiTread("Printer no paper!");
            Log.e("PRNT","Printer no paper");
            return ;
        }
        ret = mPrinter.open();
        mPrinter.startCaching();
        ret = mPrinter.printBmp(bitmap);
        if( ret != 0)
        {
            Log.e("Error printImg - "," "+ret);
        }
        else
        {
            Log.e("printImg - ","Success:"+ret);
        }
        mPrinter.print();
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
                        printStatus = String.valueOf(mPrinter.getStatus());
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

    public void imprimeCupom(){
        int ret;
        ret = mPrinter.getStatus();
        if(ret != 0){
            Log.e("F100_test","Print getStatus error：0x"+int2HexStr(ret));
            return ;
        }
        Log.e("F100_test","Print getStatus success:");

        boolean bHavePaper = mPrinter.isHavePaper();
        if(!bHavePaper) {
            //setTextOnUiTread("Printer no paper!");
            Log.e("PRNT","Printer no paper");
            return ;
        }
        int temperature =mPrinter.getTemperature();
        Log.e("PRNT","Temperature is:"+temperature);
        ret = mPrinter.open();
        mPrinter.startCaching();
        //Criando bitmap de uma imagem salva na pasta Drawble do projeto
        Bitmap bitmapx = BitmapFactory.decodeResource(getResources(),R.drawable.gertec_2);
        //Função para impressão de BITMAP - mFTPrinter.printBmp(Bitmap bitmapx)
        ret = mPrinter.printBmp(bitmapx);
        ret = mPrinter.setGray(5);

        mPrinter.setSpace(0, 0);
        mPrinter.setMargin(0, 0);

        Bundle bundle1 = new Bundle();
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format",  Typeface.BOLD);
        bundle1.putInt("size", 32);
        mPrinter.setFont(bundle1);
        mPrinter.setAlignStyle(2);
        mPrinter.setSpace(0, 0);
        mPrinter.printStr("\nGertec do Brasil");
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format",  Typeface.NORMAL);
        bundle1.putInt("size", 22);
        mPrinter.setFont(bundle1);

        mPrinter.printStr("\n\nAv Jabaguara, 3060 - Mirandopolis\n");
        mPrinter.printStr("Sao Paulo - SP - CEP: 04046-500\n");
        mPrinter.printStr("CNPJ: 03.654.119/0001-76\n");
        mPrinter.printStr("IE: 286.502.952.112\n");
        mPrinter.setAlignStyle(1);
        mPrinter.printStr("_________________________________\n");
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format",  Typeface.BOLD);
        bundle1.putInt("size", 25);
        mPrinter.setFont(bundle1);
        mPrinter.setAlignStyle(2);
        mPrinter.printStr("Cupom Fiscal Eletronico\n");
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format",  Typeface.NORMAL);
        bundle1.putInt("size", 22);
        mPrinter.setFont(bundle1);
        mPrinter.setAlignStyle(1);
        mPrinter.printStr("\nProduto       Quant. V. UN. Valor\n");
        mPrinter.printStr("Pessego         2     5.00  10.00\n");
        mPrinter.printStr("Iogurte Moran   5    12.00  60.00\n");
        mPrinter.printStr("Bolo de fruta   10  10.00  100.00\n");
        mPrinter.printStr("Morango fresc   10    5.00  50.00\n");
        mPrinter.printStr("_________________________________\n");
        mPrinter.setAlignStyle(4);
        mPrinter.printStr("Total: R$220.00\n");
        mPrinter.setAlignStyle(1);
        mPrinter.printStr("_________________________________\n");
        mPrinter.printStr("N: 0000000116\n");
        mPrinter.printStr("Serie: 65      20/01/2023\n");
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format",  Typeface.NORMAL);
        bundle1.putInt("size", 22);
        mPrinter.setFont(bundle1);
        mPrinter.printStr("Consulte pela chave de acesso no site\n");
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format",  Typeface.NORMAL);
        bundle1.putInt("size", 22);
        mPrinter.setFont(bundle1);
        mPrinter.printStr("http://www.nfce.fazenda.sp.gov.br\n");
        mPrinter.setAlignStyle(2);
        mPrinter.printStr("Chave de acesso:\n");
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format",  Typeface.NORMAL);
        bundle1.putInt("size", 18);
        mPrinter.setFont(bundle1);
        mPrinter.setAlignStyle(1);
        mPrinter.printStr("00000111112222233333444455556666777788889999\n");
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format",  Typeface.NORMAL);
        bundle1.putInt("size", 22);
        mPrinter.setFont(bundle1);
        mPrinter.setAlignStyle(1);
        mPrinter.printStr("_________________________________\n");
        mPrinter.setAlignStyle(2);
        mPrinter.printStr("Consultor não identificado\n");
        mPrinter.printStr("CPF: 000.000.000-00\n\n");
        byte[] qr = new byte[]{0x0D, 0x0A, 0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x48, 0x30, 0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x43, 0x03, 0x1D, 0x28, 0x6B, 0x04, 0x00, 0x31, 0x41, 0x32, 0x00, 0x1D, 0x28, 0x6B, 0x17, 0x00, 0x31, 0x50, 0x30, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x51, 0x30, 0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x52, 0x30, 0x0D, 0x0A};
        mPrinter.QrCode(qr, 1);
        mPrinter.printStr("Código de barras\n");
        byte[] qr1 = new byte[]{0x0D, 0x0A, 0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x48, 0x30, 0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x43, 0x03, 0x1D, 0x28, 0x6B, 0x04, 0x00, 0x31, 0x41, 0x32, 0x00, 0x1D, 0x28, 0x6B, 0x17, 0x00, 0x31, 0x50, 0x30, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x51, 0x30, 0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x52, 0x30, 0x0D, 0x0A};
        mPrinter.QrCode(qr1, 2);
        mPrinter.printStr("PDF417\n");
        byte[] qr2 = new byte[]{0x0D, 0x0A, 0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x48, 0x30, 0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x43, 0x03, 0x1D, 0x28, 0x6B, 0x04, 0x00, 0x31, 0x41, 0x32, 0x00, 0x1D, 0x28, 0x6B, 0x17, 0x00, 0x31, 0x50, 0x30, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x51, 0x30, 0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x52, 0x30, 0x0D, 0x0A};
        mPrinter.QrCode(qr2, 0);
        mPrinter.printStr("QR Code\n\n\n");
        mPrinter.print();

    }

    public static String int2HexStr(int i) {

        byte[] b = new byte[4];
        b[0] = (byte) ((i >> 24) & 0xFF);
        b[1] = (byte) ((i >> 16) & 0xFF);
        b[2] = (byte) ((i >> 8) & 0xFF);
        b[3] = (byte) (i & 0xFF);
        return byte2HexStr(b, 4);
    }

    public static String byte2HexStr(byte[] src, int len) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        int n = len;
        if (len > src.length)
            n = src.length;

        for (int i = 0; i < n; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public void imprimeTexto(String txt){
        int ret;
        ret = mPrinter.getStatus();
        if(ret != 0){
            Log.e("F100_test","Print getStatus error：0x"+int2HexStr(ret));
            return ;
        }
        Log.e("F100_test","Print getStatus success:");

        boolean bHavePaper = mPrinter.isHavePaper();
        if(!bHavePaper) {
            //setTextOnUiTread("Printer no paper!");
            Log.e("PRNT","Printer no paper");
            return ;
        }
        int temperature =mPrinter.getTemperature();
        Log.e("PRNT","Temperature is:"+temperature);
        ret = mPrinter.open();
        mPrinter.startCaching();
        ret = mPrinter.setGray(3);

        mPrinter.setSpace(0, 0);
        mPrinter.setMargin(0, 0);

        Bundle bundle1 = new Bundle();
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format",  Typeface.NORMAL);
        bundle1.putInt("size", 22);
        mPrinter.setFont(bundle1);
        mPrinter.setAlignStyle(1);
        mPrinter.printStr("Normal "+txt+"\n");
        //Negrito
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format",  Typeface.BOLD);
        bundle1.putInt("size", 22);
        mPrinter.setFont(bundle1);
        mPrinter.setAlignStyle(1);
        mPrinter.printStr("Negrito: "+txt+"\n");
        //Italico
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format",  Typeface.ITALIC);
        bundle1.putInt("size", 22);
        mPrinter.setFont(bundle1);
        mPrinter.setAlignStyle(1);
        mPrinter.printStr("Itálico: "+txt+"\n");
        //Negrito e Itálico
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format",  Typeface.BOLD_ITALIC);
        bundle1.putInt("size", 22);
        mPrinter.setFont(bundle1);
        mPrinter.setAlignStyle(1);
        mPrinter.printStr("Negrito e itálico: "+txt+"\n");
        //Centralizado
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format",  Typeface.NORMAL);
        bundle1.putInt("size", 22);
        mPrinter.setFont(bundle1);
        mPrinter.setAlignStyle(2);
        mPrinter.printStr("Centralizado: "+txt+"\n");
        //Direita
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format",  Typeface.NORMAL);
        bundle1.putInt("size", 22);
        mPrinter.setFont(bundle1);
        mPrinter.setAlignStyle(4);
        mPrinter.printStr("Direita: "+txt+"\n\n\n");
        mPrinter.print();
    }
}
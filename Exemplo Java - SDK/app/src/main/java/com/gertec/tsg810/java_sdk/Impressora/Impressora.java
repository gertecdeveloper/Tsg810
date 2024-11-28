package com.gertec.tsg810.java_sdk.Impressora;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.ftpos.library.smartpos.device.Device;
import com.ftpos.library.smartpos.servicemanager.OnServiceConnectCallback;
import com.gertec.tsg810.java_sdk.R;

public class Impressora extends AppCompatActivity {

    private TextView status;
    private Button btnPrintText, btnPrintImageDefault, btnPrintCupom, btnPrintBarcode;
    private FTPrntr mPrinter = null;
    private Device device;
    private Context mContext;
    private com.ftpos.library.smartpos.servicemanager.ServiceManager service;
    private EditText edTxt;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_impressora);

        ativarFullScreen();
        initComponents();

        //inicia a Thread status
        MyRunnable threadStatus = new MyRunnable();
        new Thread(threadStatus).start();


        //inicia a conexao com a impressora
        mContext = getApplicationContext();
        service.bindPosServer(this, new OnServiceConnectCallback() {
            @Override
            public void onSuccess() {
                mPrinter.getInstance(mContext);
                device = Device.getInstance(mContext);
                Log.e("binding", "Success");
                Log.e("printer", "getPaperUsage: " + mPrinter.getUsedPaperLenManage());
            }

            @Override
            public void onFail(int var1) {
                Log.e("binding", "onFail");
            }
        });

        //imprimir texto digitado
        btnPrintText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String text = null;
                    if (edTxt.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Digite um texto", Toast.LENGTH_SHORT).show();
                    } else {
                        if (mPrinter.isHavePaper()) {
                            text = edTxt.getText().toString();
                            imprimeTexto(text);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //imprimir imagem
        btnPrintImageDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (mPrinter.isHavePaper() == true) {
                        Bitmap bitmapx = BitmapFactory.decodeResource(getResources(), R.drawable.gertec1);
                        imprimirImagem(bitmapx);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //imprimir cupoom
        btnPrintCupom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (mPrinter.isHavePaper()) {
                        imprimeCupom();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //imprimir barcode
        btnPrintBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (mPrinter.isHavePaper()) {
                        imprimirBarcode();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    class MyRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                runOnUiThread(new Runnable() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void run() {

                        int printerStatus = mPrinter.getStatus();

                        switch (printerStatus) {
                            case 0:
                                status.setText(" Impressora ok ");
                                break;

                            case 262:
                                status.setText(" Imprimindo ");
                                break;

                            case -1:
                                status.setText(" Verificando ");
                                break;

                            default:
                                status.setText(" " + printerStatus + " ");
                                break;
                        }
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


    public void imprimeTexto(String txt) {

        int ret;
        Log.e("TSG810", "Print getStatus success:");
        Toast.makeText(mContext, "imprimindo...", Toast.LENGTH_SHORT).show();

        ret = mPrinter.open();
        mPrinter.startCaching();
        ret = mPrinter.setGray(3);

        mPrinter.setSpace(0, 0);
        mPrinter.setMargin(0, 0);

        Bundle bundle1 = new Bundle();
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format", Typeface.NORMAL);
        bundle1.putInt("size", 22);
        mPrinter.setFont(bundle1);
        mPrinter.setAlignStyle(1);
        mPrinter.printStr("Normal: " + txt + "\n");
        //Negrito
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format", Typeface.BOLD);
        bundle1.putInt("size", 22);
        mPrinter.setFont(bundle1);
        mPrinter.setAlignStyle(1);
        mPrinter.printStr("Negrito: " + txt + "\n");
        //Italico
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format", Typeface.ITALIC);
        bundle1.putInt("size", 22);
        mPrinter.setFont(bundle1);
        mPrinter.setAlignStyle(1);
        mPrinter.printStr("Itálico: " + txt + "\n");
        //Negrito e Itálico
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format", Typeface.BOLD_ITALIC);
        bundle1.putInt("size", 22);
        mPrinter.setFont(bundle1);
        mPrinter.setAlignStyle(1);
        mPrinter.printStr("Negrito e itálico: " + txt + "\n");
        //Centralizado
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format", Typeface.NORMAL);
        bundle1.putInt("size", 22);
        mPrinter.setFont(bundle1);
        mPrinter.setAlignStyle(2);
        mPrinter.printStr("Centralizado: " + txt + "\n");
        //Direita
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format", Typeface.NORMAL);
        bundle1.putInt("size", 22);
        mPrinter.setFont(bundle1);
        mPrinter.setAlignStyle(4);
        mPrinter.printStr("Direita: " + txt + "\n\n\n");
        mPrinter.print();
    }


    public void imprimeCupom() {
        int ret;
        ret = mPrinter.open();
        mPrinter.startCaching();
        //Criando bitmap de uma imagem salva na pasta Drawble do projeto
        Bitmap bitmapx = BitmapFactory.decodeResource(getResources(), R.drawable.gertec1);
        //Função para impressão de BITMAP - mFTPrinter.printBmp(Bitmap bitmapx)
        ret = mPrinter.printBmp(bitmapx);
        ret = mPrinter.setGray(5);

        mPrinter.setSpace(0, 0);
        mPrinter.setMargin(0, 0);

        Bundle bundle1 = new Bundle();
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format", Typeface.BOLD);
        bundle1.putInt("size", 32);
        mPrinter.setFont(bundle1);
        mPrinter.setAlignStyle(2);
        mPrinter.setSpace(0, 0);
        mPrinter.printStr("\nGertec do Brasil");
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format", Typeface.NORMAL);
        bundle1.putInt("size", 22);
        mPrinter.setFont(bundle1);
        mPrinter.printStr("\n\nAv Jabaguara, 3060 - Mirandopolis\n");
        mPrinter.printStr("Sao Paulo - SP - CEP: 04046-500\n");
        mPrinter.printStr("CNPJ: 03.654.119/0001-76\n");
        mPrinter.printStr("IE: 286.502.952.112\n");
        mPrinter.setAlignStyle(1);
        mPrinter.printStr("_________________________________\n");
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format", Typeface.BOLD);
        bundle1.putInt("size", 25);
        mPrinter.setFont(bundle1);
        mPrinter.setAlignStyle(2);
        mPrinter.printStr("Cupom Fiscal Eletronico\n");
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format", Typeface.NORMAL);
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
        bundle1.putInt("format", Typeface.NORMAL);
        bundle1.putInt("size", 22);
        mPrinter.setFont(bundle1);
        mPrinter.printStr("Consulte pela chave de acesso no site\n");
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format", Typeface.NORMAL);
        bundle1.putInt("size", 22);
        mPrinter.setFont(bundle1);
        mPrinter.printStr("http://www.nfce.fazenda.sp.gov.br\n");
        mPrinter.setAlignStyle(2);
        mPrinter.printStr("Chave de acesso:\n");
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format", Typeface.NORMAL);
        bundle1.putInt("size", 18);
        mPrinter.setFont(bundle1);
        mPrinter.setAlignStyle(1);
        mPrinter.printStr("00000111112222233333444455556666777788889999\n");
        bundle1.putString("font", "Typeface.DEFAULT");
        bundle1.putInt("format", Typeface.NORMAL);
        bundle1.putInt("size", 22);
        mPrinter.setFont(bundle1);
        mPrinter.setAlignStyle(1);
        mPrinter.printStr("_________________________________\n");
        mPrinter.setAlignStyle(2);
        mPrinter.printStr("Consultor não identificado\n");
        mPrinter.printStr("CPF: 000.000.000-00\n\n\n");
        mPrinter.print();
    }

    public void imprimirImagem(Bitmap bitmap) {
        int ret;
        ret = mPrinter.open();
        mPrinter.startCaching();
        ret = mPrinter.printBmp(bitmap);
        mPrinter.print();
    }

    private void imprimirBarcode() {
        int ret;
        ret = mPrinter.open();
        mPrinter.startCaching();
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
        if (len > src.length) n = src.length;

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


    public void initComponents() {
        status = findViewById(R.id.inforstatus);
        edTxt = findViewById(R.id.txtMensagemImpressao);
        btnPrintText = findViewById(R.id.btnImprimir);
        btnPrintImageDefault = findViewById(R.id.btnPrintImagem);
        btnPrintCupom = findViewById(R.id.btnCupom);
        btnPrintBarcode = findViewById(R.id.btnPrintBarcode);
        mPrinter = new FTPrntr(Impressora.this);
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

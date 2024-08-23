package com.example.easylayer_tsg810_217.Impressora;

import static android.widget.Toast.makeText;
import static br.com.gertec.easylayer.printer.Alignment.CENTER;
import static br.com.gertec.easylayer.printer.Alignment.LEFT;
import static br.com.gertec.easylayer.printer.Alignment.RIGHT;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easylayer_tsg810_217.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import br.com.gertec.easylayer.printer.Alignment;
import br.com.gertec.easylayer.printer.BarcodeFormat;
import br.com.gertec.easylayer.printer.BarcodeType;
import br.com.gertec.easylayer.printer.CutType;
import br.com.gertec.easylayer.printer.ListaItens;
import br.com.gertec.easylayer.printer.PrintConfig;
import br.com.gertec.easylayer.printer.Printer;
import br.com.gertec.easylayer.printer.PrinterError;
import br.com.gertec.easylayer.printer.PrinterException;
import br.com.gertec.easylayer.printer.PrinterUtils;
import br.com.gertec.easylayer.printer.Receipt;
import br.com.gertec.easylayer.printer.Status;
import br.com.gertec.easylayer.printer.TableFormat;
import br.com.gertec.easylayer.printer.TextFormat;

public class Impressora extends AppCompatActivity implements Printer.Listener {

    private EditText edTxt;
    Printer printer;
    private static boolean isRun = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impressora);

        //Inicialização da classe Printer
        printer = Printer.getInstance(this, this);

        //Contexto
        Context context = this.getApplicationContext();

        //Inicialização da classe PrinterUtils
        PrinterUtils printerUtils = printer.getPrinterUtils();

        //Inicialização da classe PrintConfig
        PrintConfig printConfig = new PrintConfig();

        //Definição de linhamento
        printConfig.setAlignment(CENTER);

        edTxt = findViewById(R.id.edt_text);


        //Impressão do texto
        Button print_text = findViewById(R.id.btn_print_text);
        print_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String text;
                    if (edTxt.getText().toString().isEmpty()) {
                        makeText(getApplicationContext(), "Digite um texto", Toast.LENGTH_SHORT).show();

                    } else {
                        text = edTxt.getText().toString();

                        //Formato 1
                        TextFormat textFormat = new TextFormat();
                        textFormat.setBold(true);
                        textFormat.setUnderscore(true);
                        textFormat.setFontSize(30);
                        textFormat.setLineSpacing(6);
                        textFormat.setAlignment(CENTER);

                        //Impressão formato 1
                        printer.printText(textFormat, text);

                        //Formato 2
                        TextFormat textFormat2 = new TextFormat();
                        textFormat.setBold(false);
                        textFormat.setUnderscore(false);
                        textFormat.setFontSize(30);
                        textFormat.setLineSpacing(6);
                        textFormat.setAlignment(LEFT);

                        //Impressão formato 2
                        printer.printText(textFormat2, text);

                        //Formato 3
                        TextFormat textFormat3 = new TextFormat();
                        textFormat.setBold(false);
                        textFormat.setUnderscore(true);
                        textFormat.setFontSize(30);
                        textFormat.setLineSpacing(6);
                        textFormat.setAlignment(RIGHT);

                        //Impressão formato 3
                        printer.printText(textFormat3, text);

                        printer.scrollPaper(3);
                        makeText(getApplicationContext(), "Imprimindo", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    //Mensagem de erro
                    Log.i(String.valueOf(getApplicationContext()), "onClick: Erro ao imprimir");
                }
            }
        });

        //Imprimir imagem
        Button print_imagem = findViewById(R.id.btn_print_imagem);
        print_imagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Definição do bitmap através do drawable
                Bitmap imagem = BitmapFactory.decodeResource(getResources(), R.drawable.gertec_jpg);

                if (imagem != null) {

//                    Aplica um nivel de intensidade de impressão em um bitmap
                    Bitmap monochromaticBitmap = printerUtils.applyIntensityLevel(imagem,
                            PrinterUtils.INTENSITY_LEVEL_4);
//                    //Converte um bitmap colorido em monocromático (preto e branco)  e remove o extra preto.
//                    Bitmap monochromaticBitmap = printerUtils.toMonochromatic(imagem,PrinterUtils
//                    .INTENSITY_LEVEL_2, true);
////
//                    //Converte um bitmap colorido em monocromático (preto e branco) e não remove o extra preto.
//                    // Apresenta o resultado invertido do método toMonochromatic.
//                    Bitmap imagem3 = printerUtils.toInvertedMonochromatic(imagem, PrinterUtils
//                    .INTENSITY_LEVEL_3, false);



                    try {
                        //Impressão
                        printer.printImageAutoResize(monochromaticBitmap);

                        //Pular linhas
                        printer.scrollPaper(3);
                    } catch (PrinterException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });

        //Botão de imprimir HTML
        Button btn_print_html = findViewById(R.id.btn_print_html);
        btn_print_html.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Formatação do HTML
                final String html = "<table border=\"1\">\n" +
                        "    <tr>\n" +
                        "        <td>Nome</td>\n" +
                        "        <td>Idade</td>\n" +
                        "        <td>Profissão</td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td>Ted</td>\n" +
                        "        <td>22</td>\n" +
                        "        <td>Médico</td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "        <td>Ralf</td>\n" +
                        "        <td>26</td>\n" +
                        "        <td>Farmacêutico</td>\n" +
                        "    </tr>\n" +
                        "</table>";
                try {
                    //Imprimir HTML
                    printer.printHtml(getApplicationContext(), html);

                } catch (PrinterException e) {
                    throw new RuntimeException(e);
                }
            }
        });

//Botão de imprimir código de barras
        Button btn_barcode = findViewById(R.id.btn_print_barcode);
        btn_barcode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    //Implementação
                    BarcodeFormat format = new BarcodeFormat(BarcodeType.AZTEC); //AZTEC
                    BarcodeFormat format5 = new BarcodeFormat(BarcodeType.DATA_MATRIX); //DATA MATRIX
                    BarcodeFormat format12 = new BarcodeFormat(BarcodeType.PDF_417); //PDF 417
                    BarcodeFormat format1 = new BarcodeFormat(BarcodeType.CODABAR); //CODA BAR
                    BarcodeFormat format2 = new BarcodeFormat(BarcodeType.CODE_39); //CODE 39
                    BarcodeFormat format4 = new BarcodeFormat(BarcodeType.CODE_128); //CODE 168
                    BarcodeFormat format6 = new BarcodeFormat(BarcodeType.EAN_8); //EAN 8
                    BarcodeFormat format7 = new BarcodeFormat(BarcodeType.EAN_13); //EAN 13
                    BarcodeFormat format8 = new BarcodeFormat(BarcodeType.ITF); //ITF
                    BarcodeFormat format9 = new BarcodeFormat(BarcodeType.UPC_A); //UPC A
                    BarcodeFormat format10 = new BarcodeFormat(BarcodeType.QR_CODE); //QR CODE

                    int scrollLines = 1;
                    TextFormat textFormat = new TextFormat();
                    textFormat.setAlignment(Alignment.CENTER);

                    //Tamanho
                    BarcodeFormat.Size size = BarcodeFormat.Size.HALF_PAPER;

                    //AZTEC"
                    printer.printText(textFormat, "AZTEC");
                    format.setSize(size);
                    printer.printBarcode(format, "12345");
                    printer.scrollPaper(scrollLines);

                    //DATA_MATRIX
                    printer.printText(textFormat, "DATA_MATRIX");
                    format5.setSize(size);
                    printer.printBarcode(format5, "CODEGS1DATAMATRIX22X22");
                    printer.scrollPaper(scrollLines);

                    //PDF_417
                    printer.printText(textFormat, "PDF_417");
                    format12.setSize(size);
                    printer.printBarcode(format12, "12345");
                    printer.scrollPaper(scrollLines);

                    //QRCODE
                    printer.printText(textFormat, "QRCODE");
                    format10.setSize(size);
                    printer.printBarcode(format10, "https://www.gertec.com.br");

                    //CODABAR
                    printer.printText(textFormat, "CODABAR");
                    printer.printBarcode(format1, "12345");
                    printer.scrollPaper(scrollLines);

                    //CODE_39
                    printer.printText(textFormat, "CODE_39");
                    printer.printBarcode(format2, "12345");
                    printer.scrollPaper(scrollLines);

                    //CODE_128
                    printer.printText(textFormat, "CODE_128");
                    printer.printBarcode(format4, "12345");
                    printer.scrollPaper(scrollLines);

                    //EAN_8
                    printer.printText(textFormat, "EAN_8");
                    printer.printBarcode(format6, "12345678");
                    printer.scrollPaper(scrollLines);

                    //EAN_13
                    printer.printText(textFormat, "EAN_13");
                    printer.printBarcode(format7, "0123456789012");
                    printer.scrollPaper(scrollLines);

                    //ITF
                    printer.printText(textFormat, "ITF");
                    printer.printBarcode(format8, "47257091688216");
                    printer.scrollPaper(scrollLines);

                    //UPC_A
                    printer.printText(textFormat, "UPC_A");
                    printer.printBarcode(format9, "63938200039");

                    printer.scrollPaper(scrollLines + 2);

                    //Mensagem no log para informar sobre a impressão
                    Log.i("Impressão", "Códigos de barra: impressos com sucesso ");

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });

        //Botão de imprimir tabelas
        Button btn_tabelas = findViewById(R.id.btn_print_tabelas);
        btn_tabelas.setOnClickListener(view ->

        {
            try {
                //Formatação
                String[] headerArray = {"H1", "H2", "H3", "H4"};
                List<String> header = Arrays.asList(headerArray);
                String[][] rowsArray = {{"11", "12", "13", "14"}, {"21", "22", "23", "24"}, {"31", "32", "33", "34"}};
                List<List<String>> rows = new ArrayList<>();
                for (int i = 0; i < rowsArray.length; i++) {
                    rows.add(Arrays.asList(rowsArray[0]));
                }
                ;

                //Impressão
                TableFormat tableFormat = new TableFormat();
                tableFormat.setFontSize(30);
                tableFormat.setHeaderAlignment(RIGHT);
                tableFormat.setRowAlignment(LEFT);

                try {
                    //Impressão
                    printer.printTable(context, tableFormat, header, rows);

                } catch (PrinterException e) {
                    throw new RuntimeException(e);
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        });

        //Botão de soltar papel
        Button btn_print_scroll = findViewById(R.id.btn_print_scroll);
        btn_print_scroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printer.scrollPaper(3);
            }
        });

        //Botão de imprimir XML
        Button print_xml = findViewById(R.id.btn_print_xml);
        print_xml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!statusPrinter()) {
                        Receipt receipt = new Receipt();
                        ListaItens[] items = new ListaItens[3];
                        for (int i = 0; i < items.length; i++) {
                            items[i] = new ListaItens();
                        }
                        ArrayList<String> listItens = new ArrayList<>();
                        listItens.add("001 00345 1 cx (1,25) - Pasta de alho");
                        listItens.add("002 00345 1 cx (5,00) - Bife de figado");
                        listItens.add("003 00345 1 cx (1,25) - Creme de barbear");
                        listItens.add("004 00345 1 cx (1,25) - Creme Hidratante");
                        listItens.add("005 00345 1 cx (1,25) - Bolacha negresco");
                        listItens.add("006 00345 1 cx (1,25) - Peito de frango");

                        ArrayList<String> listItensValue = new ArrayList<>();
                        listItensValue.add("7,00");
                        listItensValue.add("15,00");
                        listItensValue.add("15,00");
                        listItensValue.add("10,00");
                        listItensValue.add("5,00");
                        listItensValue.add("18,00");
                        receipt.setListItens(listItens);
                        receipt.setListValueItens(listItensValue);


                        receipt.setCardNumber("**** **** **** 1234");
                        printer.printXml(receipt);

                        printer.scrollPaper(2);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });



    }

    //Status da impressora
    private boolean statusPrinter() {
        isRun = true;

        try {
            Status status = printer.getStatus();
            if (status.getCode() != 0 && status.getCode() != 3) {
                Toast.makeText(Impressora.this, status.toString(), Toast.LENGTH_SHORT).show();
                isRun = false;
                return true;
            } else {
                return false;
            }
        } catch (PrinterException e) {
            throw new RuntimeException(e);
        }
    }


    //Implementação de erro da impressora
    @Override
    public void onPrinterError(PrinterError printerError) {
        String message = String.format(Locale.US, "Id: [%d] | Cause: [\"%s\"]",
                printerError.getRequestId(), printerError.getCause());
        Log.d("[onPrinterError]", message);
    }

    //Implementação de impressão com sucesso
    @Override
    public void onPrinterSuccessful(int printerRequestId) {
        String message = String.format(Locale.US, "Id: [%d]", printerRequestId);
        Log.d("[onPrinterSuccessful]", message);
    }


}
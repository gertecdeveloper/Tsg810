package com.gertec.exemplosgertec.ExemploImpressora;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Printer {

    private static OutputStream mmOutputStream;
    private static InputStream mmInputStream;

    public Printer(OutputStream mmOutputStream,InputStream inputStream) {
        this.mmOutputStream = mmOutputStream;
        this.mmInputStream = inputStream;
    }

    static void sendData(String msg) throws IOException {
        int rdgChoice2 = 1;
        try {
            byte[] bySend = Language.Portugues(rdgChoice2,msg);
            mmOutputStream.write(bySend);
            mmOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void qrcode(int size, String data) throws IOException {
        int store_len = data.length() + 3;
        byte store_pL = (byte) (store_len % 256);
        byte store_pH = (byte) (store_len / 256);

        textoAlinhadoCentro();
        mmOutputStream.flush();

        byte[] qrCodeModel = {0x1d,0x28,0x6b,0x04,0x00,0x31,0x41,(byte)49,0x00};// 49-50
        mmOutputStream.write(qrCodeModel);

        byte[] qrCodeSize = {0x1d,0x28,0x6b,0x03,0x00,0x31,0x43,(byte)size};//5-15
        mmOutputStream.write(qrCodeSize);

        byte[] qrCodeerror = {0x1d,0x28,0x6b,0x03,0x00,0x31,0x45,(byte)50};//48-51
        mmOutputStream.write(qrCodeerror);

        byte[] qrCodeData = {0x1d,0x28,0x6b,store_pL,store_pH,0x31,0x50,0x30};
        mmOutputStream.write(qrCodeData);
        mmOutputStream.write(data.getBytes());

        byte[] qrCodeSymbol = {0x1d,0x28,0x6b,0x03,0x00,0x31,0x51,(byte)48};
        mmOutputStream.write(qrCodeSymbol);

        mmOutputStream.flush();
    }

    static void textBoldOn() throws IOException {
        byte[] boldOn = new byte[]{0x1B,0x21,(byte) 8};
        mmOutputStream.flush();
        mmOutputStream.write(boldOn);
    }

    static void textBoldOff() throws IOException {
        byte[] boldOff = new byte[]{0x1B,0x21,(byte) 0};
        mmOutputStream.flush();
        mmOutputStream.write(boldOff);
    }

    static void textUnderlineOn() throws IOException {
        byte[] underlineOn = new byte[]{0x1B,0x2D,0x01};
        mmOutputStream.write(underlineOn);
    }

    static void textUnderlineOff() throws IOException {
        byte[] underlineOff = new byte[]{0x1B,0x2D,0x00};
        mmOutputStream.flush();
        mmOutputStream.write(underlineOff);
    }

    static void textoAlinhadoEsquerda() throws IOException {
        byte[] alignLeft = new byte[]{0x1B,0x61,(byte) 0};
        mmOutputStream.flush();
        mmOutputStream.write(alignLeft);
    }

    static void textoAlinhadoCentro() throws IOException {
        byte[] alignCenter = new byte[]{0x1B,0x61,(byte) 1};
        mmOutputStream.flush();
        mmOutputStream.write(alignCenter);
    }

    static void textoAlinhadoDireita() throws IOException {
        byte[] alignRight = new byte[]{0x1B,0x61,(byte) 2};
        mmOutputStream.flush();
        mmOutputStream.write(alignRight);
    }

    void tamanhoDaLetra(int tamanho) throws IOException {
        byte[] b = {0x1D,0x21,(byte)tamanho,(byte)0};
        mmOutputStream.write(b);
    }

    public void imprimiTexto(PrintConfig config) throws IOException {

        switch (config.getAlinhamentoDoTexto()){
            case 1:
                textoAlinhadoEsquerda();
                break;
            case 2:
                textoAlinhadoCentro();
                break;
            case 3:
                textoAlinhadoDireita();
                break;
        }

        switch (config.getFormatacaoDaLetra()){
            case 0:
                tamanhoDaLetra(config.getTamanhoDaLetra());
                break;
            case 1:
                textBoldOn();
                tamanhoDaLetra(config.getTamanhoDaLetra());
                break;
            case 2:
                textUnderlineOn();
                //tamanhoDaLetraSublinhada(config.getTamanhoDaLetra());
                break;
        }


        sendData(config.getTexto());

        switch (config.getFormatacaoDaLetra()){
            case 0:

                break;
            case 1:
                textBoldOff();
                break;
            case 2:
                textUnderlineOff();
                break;
        }
    }

    void imprimeCupom() throws IOException {
        tamanhoDaLetra(1);
        textoAlinhadoCentro();
        textBoldOn();
        sendData("GERTEC DO BRASIL\n");
        textBoldOff();
        tamanhoDaLetra(0);
        sendData("AV.JABAQUARA, 3060, MIRANDOPOLIS");
        sendData("SÃO PAULO - SP - CEP: 04046-500");
        textBoldOn();
        textoAlinhadoEsquerda();
        sendData("CNPJ: 03.654.119/0001-76");
        textBoldOff();
        sendData("IE: 286.502.952.112");
        textBoldOn();
        tamanhoDaLetra(0);
        textoAlinhadoCentro();
        sendData("-------------------\n");
        tamanhoDaLetra(0);
        sendData("CUPOM FISCAL ELETRONICO\n");
        textBoldOff();
        textoAlinhadoEsquerda();
        tamanhoDaLetra(0);
        sendData("ITEM      CÓDIGO       DESCRIÇÃO");
        sendData("QTD UN VAL.UN(R$) ST VL.ITEM(R$)");
        sendData("QTD  123         NOME DO PRODUTO");
        sendData("001.000UN    X    12.00    25,00\n");
        textoAlinhadoCentro();
        sendData("-------------------");
        textBoldOn();
        textoAlinhadoEsquerda();
        sendData("TOTAL                   R$ 25,00");
        textBoldOff();
        textoAlinhadoCentro();
        sendData("-------------------\n");
        textoAlinhadoEsquerda();
        sendData("N: 0000000139 \n");
        sendData("SERIE: 65   20/08/2021\n");
        textoAlinhadoCentro();
        sendData("-------------------\n");
        tamanhoDaLetra(0);
        sendData("CONSULTE PELA CHAVE DE ACESSO NO\n");
        textoAlinhadoEsquerda();
        tamanhoDaLetra(0);
        sendData("http//www.nto.fazenda.sp.gov.br\n");
        sendData("CHAVE DE ACESSO:\n");
        textoAlinhadoCentro();
        sendData("00000111112222233333444455556666777788889999\n\n");
        //CÓDIGO DE BARRAS
        imprimeCodigoDeBarras("123456789123",67,200,240,1,0);
        textoAlinhadoCentro();
        sendData("-------------------\n");
        sendData("CONSULTOR NÃO IDENTIFICADO\n");
        sendData("CPF: 000.000.000-00");
        qrcode(8,"https://www.gertec.com.br/");
        sendData("\n\n");
    }

    public void imprimeCodigoDeBarras(String code, int type, int h, int w, int font, int pos) throws IOException {

        textoAlinhadoCentro();

        byte[] setPosition = new byte[]{0x1D,0x48,(byte)pos};
        mmOutputStream.write(setPosition);

        byte[] setFont = new byte[]{0x1D,0x66,(byte)font};
        mmOutputStream.write(setFont);

        byte[] setHeight = new byte[]{0x1D,0x68,(byte)h};
        mmOutputStream.write(setHeight);

        byte[] setWeight = new byte[]{0x1D,0x77,(byte)w};
        mmOutputStream.write(setWeight);

        byte[] data = new byte[]{0x1D,0x6b,(byte)type, (byte)code.length()};
        mmOutputStream.write(data);
        sendData(code);
        mmOutputStream.write(0x00);
    }

    public void printPdf417(String code, int h, int w, int cols, int rows, int error) throws IOException {
        textoAlinhadoCentro();

        byte[] FUNCTION_65 = new byte[]{0x1D,0x28,0x6B,0x03,0x00,0x30,0x41,(byte) cols}; //0 - 30
        mmOutputStream.write(FUNCTION_65);

        byte[] FUNCTION_66 = new byte[]{0x1D,0x28,0x6B,0x03,0x00,0x30,0x42,(byte)rows}; //3-90
        mmOutputStream.write(FUNCTION_66);

        byte[] FUNCTION_67 = new byte[]{0x1D,0x28,0x6B,0x03,0x00,0x30,0x43,(byte)w};//size of module 2 - 8
        mmOutputStream.write(FUNCTION_67);

        byte[] FUNCTION_68 = new byte[]{0x1D,0x28,0x6B,0x03,0x00,0x30,0x44,(byte)h};//size of module 2 - 8
        mmOutputStream.write(FUNCTION_68);


        byte[] FUNCTION_69 = new byte[]{0x1D,0x28,0x6B,0x04,0x00,0x30,0x45,(byte)48,(byte)error};//se 48: 49-55, se 49: 1-40
        mmOutputStream.write(FUNCTION_69);

        byte[] FUNCTION_80 = new byte[]{0x1D,0x28,0x6B,(byte)(code.length()+4),0x00,0x30,0x50,0x30,(byte)48};
        mmOutputStream.write(FUNCTION_80);

        mmOutputStream.write(code.getBytes()); //data to be encoded

        byte[] FUNCTION_81 = new byte[]{0x1D,0x28,0x6B,0x03,0x00,0x30,0x51,(byte)48};
        mmOutputStream.write(FUNCTION_81);
    }

    void cutPaper() throws IOException {
        byte[] b = new byte[]{0x1D,0x56,(byte) 66,(byte) 255};// m=66 , n= 0 até 255
        mmOutputStream.write(b);
    }

    int printerStatus() throws IOException {
        byte[] b = new byte[]{0x1B,0x63,0x33,(byte)3};
//        byte[] b = new byte[]{0x10,0x04,0x01};
        mmOutputStream.write(b);
        int status = mmInputStream.read(b);
        return status;
    }

}

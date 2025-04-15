package com.example.gondolapriceview.canvas;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.view.View;

import com.example.gondolapriceview.model.Produtos;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class GondolaPriceView extends View {
    private Paint paint;
    public Produtos produto;

    String descricao, barCode;
    Double preco;

    public GondolaPriceView(Context context, String descricao1, String barCode1, Double preco1) {
        super(context);
        paint = new Paint();
        descricao = descricao1;
        barCode = barCode1;
        preco = preco1;
        produto = new Produtos(descricao,barCode,preco);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        // Rotação do canvas para orientação vertical
        canvas.rotate(90, getWidth() / 2, getHeight() / 2);

        // Defina as propriedades do seu paint
        paint.setColor(Color.BLACK);

        // Calcule as coordenadas centrais
        int centerX = getWidth() / 4;
        int centerY = getHeight() / 2;

        // Adicione um valor de início às coordenadas y
        int startY = -160; // Mover tudo para cima

        // nome do produto
        paint.setTextSize(50); // Tamanho do texto aumentado para o título
        String nome = produto.getNome();
        if (nome.length() > 37) { // Se o nome do produto for muito longo, divida-o em duas linhas
            String primeiraLinha = nome.substring(0, 37);
            String segundaLinha = nome.substring(37);
            canvas.drawText(primeiraLinha, centerX - 340, centerY - 50 + startY, paint);
            canvas.drawText(segundaLinha, centerX - 340, centerY + startY, paint);
        } else {
            canvas.drawText(nome, centerX - 330, centerY - 37 + startY, paint);
        }

        // código de barras ao lado do preço
        Bitmap barcodeBitmap = generateBarcode(produto.getCodigoDeBarras()); // Defina o código de barras como "123456789"
        if (barcodeBitmap != null) {
            canvas.drawBitmap(barcodeBitmap, centerX - 420, centerY + 20 + startY, null); // Adicione um "top" acima do código de barras
            // número abaixo do código de barras
            paint.setTextSize(30); // Tamanho do texto aumentado para o número do código de barras
            canvas.drawText(produto.getCodigoDeBarras(), centerX - 360, centerY + 200 + startY, paint); // Mova o número do código de barras para baixo
        }

        //código de barras ao lado do preço
        Bitmap barcodeBitmap1 = generateQRCode(produto.getCodigoDeBarras());
        canvas.drawBitmap(barcodeBitmap1, centerX - 170, centerY + 30 + startY, null); // Adicione um "top" acima do código de barras


        //  preço ao lado do código de barras
        paint.setTextSize(100); // Aumente o tamanho do texto para o preço
        paint.setFakeBoldText(true);
        canvas.drawText("R$ " + String.format("%.2f", produto.getPreco()), centerX + 36, centerY + 140 + startY, paint); // Mova o preço para a esquerda para alinhá-lo com o código de barras

//        // linha pontilhada para orientar a dobra do papel
        paint.setPathEffect(new DashPathEffect(new float[]{20, 40}, 0));
        paint.setStrokeWidth(1); // Aumente a espessura da linha
        // A linha é desenhada 20 pixels abaixo do número do código de barras
        canvas.drawLine(centerX - 380, centerY + 227 + startY, centerX + 480, centerY + 227 + startY, paint);
    }


    private Bitmap generateQRCode(String data) {
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 250, 150); // Dimensões do QR Code
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Bitmap generateBarcode(String data) {
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.CODE_128, 300, 150); // Dimensões do código de barras aumentadas
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Bitmap gondola() {

        // Definindo as dimensões desejadas para a View
        int viewWidth = 500;
        int viewHeight = 960;

// Criando uma instância da GondolaPriceView
        final GondolaPriceView gondolaPriceView = new GondolaPriceView(getContext(), descricao, barCode, preco);

// Medindo a View com as dimensões desejadas
        int measureSpecWidth = View.MeasureSpec.makeMeasureSpec(viewWidth, View.MeasureSpec.EXACTLY);
        int measureSpecHeight = View.MeasureSpec.makeMeasureSpec(viewHeight, View.MeasureSpec.EXACTLY);
        gondolaPriceView.measure(measureSpecWidth, measureSpecHeight);

// Dispondo a View
        gondolaPriceView.layout(0, 0, gondolaPriceView.getMeasuredWidth(), gondolaPriceView.getMeasuredHeight());


        // Obtendo o bitmap da GondolaPriceView
        Bitmap bitmap = getBitmapFromView(gondolaPriceView);
        return bitmap;
    }

    private Bitmap getBitmapFromView(View view) {
        // Verificando se a view não é nula e tem dimensões definidas
        if (view == null || view.getWidth() <= 0 || view.getHeight() <= 0) {
            return null;
        }

        // Criando um bitmap com as dimensões da view
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        // Criando um canvas associado ao bitmap
        Canvas canvas = new Canvas(bitmap);

        // Desenhando a view no canvas
        view.draw(canvas);

        return bitmap;
    }


}

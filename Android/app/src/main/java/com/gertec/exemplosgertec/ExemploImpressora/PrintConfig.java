package com.gertec.exemplosgertec.ExemploImpressora;

public class PrintConfig {
    private String texto = "";
    private int tamanhoDaLetra = 10;
    private int formatacaoDaLetra = 0;
    private int alinhamentoDoTexto = 2;
    private int tamanhoCodigoDeBarras = 40;
    private int larguraCodigoDeBarras = 40;

    public PrintConfig(String texto) {
        this.texto = texto;
    }

    public PrintConfig(String texto, int tamanhoDaLetra) {
        this.texto = texto;
        this.tamanhoDaLetra = tamanhoDaLetra;
    }

    public PrintConfig(String texto, int tamanhoDaLetra, int formatacaoDaLetra, int alinhamentoDoTexto) {
        this.texto = texto;
        this.tamanhoDaLetra = tamanhoDaLetra;
        this.formatacaoDaLetra = formatacaoDaLetra;
        this.alinhamentoDoTexto = alinhamentoDoTexto;
    }


    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getTamanhoDaLetra() {
        return tamanhoDaLetra;
    }

    public void setTamanhoDaLetra(int tamanhoDaLetra) {
        this.tamanhoDaLetra = tamanhoDaLetra;
    }

    public int getFormatacaoDaLetra() {
        return formatacaoDaLetra;
    }

    public void setFormatacaoDaLetra(int formatacaoDaLetra) {
        this.formatacaoDaLetra = formatacaoDaLetra;
    }

    public int getAlinhamentoDoTexto() {
        return alinhamentoDoTexto;
    }

    public void setAlinhamentoDoTexto(int alinhamentoDoTexto) {
        this.alinhamentoDoTexto = alinhamentoDoTexto;
    }

    public int getTamanhoCodigoDeBarras() {
        return tamanhoCodigoDeBarras;
    }

    public void setTamanhoCodigoDeBarras(int tamanhoCodigoDeBarras) {
        this.tamanhoCodigoDeBarras = tamanhoCodigoDeBarras;
    }

    public int getLarguraCodigoDeBarras() {
        return larguraCodigoDeBarras;
    }

    public void setLarguraCodigoDeBarras(int larguraCodigoDeBarras) {
        this.larguraCodigoDeBarras = larguraCodigoDeBarras;
    }

}

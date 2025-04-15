package com.example.gondolapriceview.model;

public class Produtos {
    private String nome;
    private String codigoDeBarras;
    private double preco;

    public Produtos() {
    }



    public Produtos(String nome, String codigoDeBarras, double preco) {
        this.nome = nome;
        this.codigoDeBarras = codigoDeBarras;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public String getCodigoDeBarras() {
        return codigoDeBarras;
    }

    public double getPreco() {
        return preco;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCodigoDeBarras(String codigoDeBarras) {
        this.codigoDeBarras = codigoDeBarras;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}

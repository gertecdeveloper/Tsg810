package com.example.gondolapriceview.model;

import com.google.gson.annotations.SerializedName;

public class ProductInfo {
    @SerializedName("codigoDeBarras")
    private String codigoDeBarras;

    @SerializedName("descricao")
    private String descricao;

    @SerializedName("preco1")
    private String preco1;

    @SerializedName("preco2")
    private String preco2;


    public String getCodigoDeBarras() {
        return codigoDeBarras;
    }

    public void setCodigoDeBarras(String codigoDeBarras) {
        this.codigoDeBarras = codigoDeBarras;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPreco1() {
        return preco1;
    }

    public void setPreco1(String preco1) {
        this.preco1 = preco1;
    }

    public String getPreco2() {
        return preco2;
    }

    public void setPreco2(String preco2) {
        this.preco2 = preco2;
    }
}
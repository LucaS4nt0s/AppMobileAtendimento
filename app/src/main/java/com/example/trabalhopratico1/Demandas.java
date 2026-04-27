package com.example.trabalhopratico1;

public class Demandas {
    private String titulo, date, descricao, local, tipo, estado, solucao;
    private int id;

    public Demandas(int id, String titulo, String date, String descricao, String local, String tipo, String estado, String solucao){
        this.titulo = titulo;
        this.date = date;
        this.descricao = descricao;
        this.local = local;
        this.tipo = tipo;
        this.estado = estado;
        this.id = id;
        this.solucao = solucao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getSolucao() {
        return solucao;
    }

    public void setSolucao(String solucao) {
        this.solucao = solucao;
    }
}

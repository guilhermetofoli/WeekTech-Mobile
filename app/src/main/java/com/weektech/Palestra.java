package com.weektech;

public class Palestra {
    private String titulo, palestrante, data, local;
    private int fotoResId;

    public Palestra(String titulo, String palestrante, String data, String local, int fotoResId) {
        this.titulo = titulo;
        this.palestrante = palestrante;
        this.data = data;
        this.local = local;
        this.fotoResId = fotoResId;
    }

    public String getTitulo() { return titulo; }
    public String getPalestrante() { return palestrante; }
    public String getData() { return data; }
    public String getLocal() { return local; }
    public int getFotoResId() { return fotoResId; }
}
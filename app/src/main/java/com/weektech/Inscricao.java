package com.weektech;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "inscricoes")
public class Inscricao {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String raUsuario;
    public int alunoId;
    public int palestraId;
    public boolean presente; // true se confirmou presenca
    public String dataConfirmacao;

    public Inscricao() {}

    // construtor simples pra inscricao rapida
    public Inscricao(String raUsuario, int palestraId) {
        this.raUsuario = raUsuario;
        this.palestraId = palestraId;
        this.presente = false;
    }

    public Inscricao(String raUsuario, int alunoId, int palestraId,
                     boolean presente, String dataConfirmacao) {

        this.raUsuario = raUsuario;
        this.alunoId = alunoId;
        this.palestraId = palestraId;
        this.presente = presente;
        this.dataConfirmacao = dataConfirmacao;
    }

    // getters e setters basicos

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRaUsuario() {
        return raUsuario;
    }

    public void setRaUsuario(String raUsuario) {
        this.raUsuario = raUsuario;
    }

    public int getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(int alunoId) {
        this.alunoId = alunoId;
    }

    public int getPalestraId() {
        return palestraId;
    }

    public void setPalestraId(int palestraId) {
        this.palestraId = palestraId;
    }

    public boolean isPresente() {
        return presente;
    }

    public void setPresente(boolean presente) {
        this.presente = presente;
    }

    public String getDataConfirmacao() {
        return dataConfirmacao;
    }

    public void setDataConfirmacao(String dataConfirmacao) {
        this.dataConfirmacao = dataConfirmacao;
    }
}

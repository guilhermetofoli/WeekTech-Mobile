package com.weektech;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "inscricoes")
public class Inscricao {

    @PrimaryKey(autoGenerate = true)
    public int id;

    // RA do usuário
    public String raUsuario;

    // ID do aluno
    public int alunoId;

    // ID da palestra
    public int palestraId;

    // Controle de presença
    public boolean presente;

    // Data de confirmação da inscrição
    public String dataConfirmacao;

    public Inscricao() {}

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

    // GETTERS E SETTERS

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

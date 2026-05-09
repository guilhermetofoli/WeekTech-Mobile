package com.weektech;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "projetos")
public class Projeto {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nome;
    public String ra;
    public String nomeProjeto;
    public String descricao;

    // Status do projeto: PENDENTE, APROVADO, REPROVADO
    public String status = "PENDENTE";

    // campos que o admin preenche depois
    public String dataApresentacao;
    public String horaInicioApresentacao;
    public String horaFimApresentacao;

    // pra saber quando o aluno enviou o projeto
    public String dataCriacao;
    public String horaCriacao;

    public Projeto() {}

    // construtor pro aluno cadastrar
    @Ignore
    public Projeto(String nome, String ra, String nomeProjeto, String descricao) {
        this.nome = nome;
        this.ra = ra;
        this.nomeProjeto = nomeProjeto;
        this.descricao = descricao;
        this.status = "PENDENTE";
    }

    @Ignore
    public Projeto(String nome, String ra, String nomeProjeto,
                   String descricao, String dataApresentacao,
                   String horaInicioApresentacao, String horaFimApresentacao) {

        this.nome = nome;
        this.ra = ra;
        this.nomeProjeto = nomeProjeto;
        this.descricao = descricao;
        this.dataApresentacao = dataApresentacao;
        this.horaInicioApresentacao = horaInicioApresentacao;
        this.horaFimApresentacao = horaFimApresentacao;
    }

    // getters e setters pro room funcionar e pra gente usar no app

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public String getNomeProjeto() {
        return nomeProjeto;
    }

    public void setNomeProjeto(String nomeProjeto) {
        this.nomeProjeto = nomeProjeto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataApresentacao() {
        return dataApresentacao;
    }

    public void setDataApresentacao(String dataApresentacao) {
        this.dataApresentacao = dataApresentacao;
    }

    public String getHoraInicioApresentacao() {
        return horaInicioApresentacao;
    }

    public void setHoraInicioApresentacao(String horaInicioApresentacao) {
        this.horaInicioApresentacao = horaInicioApresentacao;
    }

    public String getHoraFimApresentacao() {
        return horaFimApresentacao;
    }

    public void setHoraFimApresentacao(String horaFimApresentacao) {
        this.horaFimApresentacao = horaFimApresentacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // apelidos pra facilitar no adapter
    public String getTitulo() {
        return nomeProjeto;
    }

    public String getAlunoId() {
        return ra;
    }
}

package com.weektech;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "projetos")
public class Projeto {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nome;
    public String ra;
    public String nomeProjeto;
    public String descricao;

    // campos que o admin preenche depois
    public String dataApresentacao;
    public String horaApresentacao;

    // pra saber quando o aluno enviou o projeto
    public String dataCriacao;
    public String horaCriacao;

    public Projeto() {}

    // construtor pro aluno cadastrar
    public Projeto(String nome, String ra, String nomeProjeto, String descricao) {
        this.nome = nome;
        this.ra = ra;
        this.nomeProjeto = nomeProjeto;
        this.descricao = descricao;
    }

    public Projeto(String nome, String ra, String nomeProjeto,
                   String descricao, String dataApresentacao,
                   String horaApresentacao) {

        this.nome = nome;
        this.ra = ra;
        this.nomeProjeto = nomeProjeto;
        this.descricao = descricao;
        this.dataApresentacao = dataApresentacao;
        this.horaApresentacao = horaApresentacao;
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

    public String getHoraApresentacao() {
        return horaApresentacao;
    }

    public void setHoraApresentacao(String horaApresentacao) {
        this.horaApresentacao = horaApresentacao;
    }

    // apelidos pra facilitar no adapter
    public String getTitulo() {
        return nomeProjeto;
    }

    public String getAlunoId() {
        return ra;
    }
}

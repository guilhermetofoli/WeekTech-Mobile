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

    public Projeto() {}

    public Projeto(String nome, String ra, String nomeProjeto, String descricao) {
        this.nome = nome;
        this.ra = ra;
        this.nomeProjeto = nomeProjeto;
        this.descricao = descricao;
    }
}

package com.weektech;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Index;

@Entity(tableName = "usuarios", indices = {@Index(value = "email", unique = true), @Index(value = "ra", unique = true)})
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nome;
    public String email;
    public String ra;
    public String curso;
    public String serie;
    public String senha;
    public boolean isAdmin;
    public boolean coffeeBreak;

    public Usuario() {}

    public Usuario(String nome, String email, String ra, String curso, String serie, String senha, boolean isAdmin) {
        this.nome = nome;
        this.email = email;
        this.ra = ra;
        this.curso = curso;
        this.serie = serie;
        this.senha = senha;
        this.isAdmin = isAdmin;
        this.coffeeBreak = false;
    }
}

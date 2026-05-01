package com.weektech;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "inscricoes")
public class Inscricao {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String raUsuario;
    public int palestraId;

    public Inscricao(String raUsuario, int palestraId) {
        this.raUsuario = raUsuario;
        this.palestraId = palestraId;
    }
}
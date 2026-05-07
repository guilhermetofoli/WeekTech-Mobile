package com.weektech;

import androidx.room.Embedded;
import androidx.room.Relation;

public class InscricaoComUsuario {
    @Embedded
    public Inscricao inscricao;

    @Relation(
        parentColumn = "raUsuario",
        entityColumn = "ra"
    )
    public Usuario usuario;
}

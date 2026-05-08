package com.weektech;

import androidx.room.Embedded;
import androidx.room.Relation;

// classe pra juntar inscricao com os dados do usuario
// o room usa isso pra fazer o join automatico
public class InscricaoComUsuario {
    @Embedded
    public Inscricao inscricao;

    @Relation(
        parentColumn = "raUsuario",
        entityColumn = "ra"
    )
    public Usuario usuario;
}

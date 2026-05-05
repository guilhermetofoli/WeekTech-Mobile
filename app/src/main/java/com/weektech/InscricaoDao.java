package com.weektech;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface InscricaoDao {

    @Insert
    long inserir(Inscricao inscricao);

    @Query("SELECT * FROM palestras WHERE ativa = 1")
    List<com.weektech.Palestra> listarInscricoesAtivas();

    @Query("SELECT COUNT(*) FROM inscricoes WHERE raUsuario = :ra AND palestraId = :palestraId")
    int verificarDuplicata(String ra, int palestraId);

    @Query("SELECT * FROM inscricoes WHERE raUsuario = :ra")
    List<Inscricao> listarInscricoesPorUsuario(String ra);
}

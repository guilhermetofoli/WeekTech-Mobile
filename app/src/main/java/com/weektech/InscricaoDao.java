package com.weektech;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
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

    @Transaction
    @Query("SELECT * FROM inscricoes WHERE palestraId = :palestraId")
    List<InscricaoComUsuario> getInscritosComUsuario(int palestraId);

    @Transaction
    @Query("SELECT * FROM inscricoes WHERE palestraId = :palestraId AND presente = 1")
    List<InscricaoComUsuario> getPresencasComUsuario(int palestraId);

    @Query("UPDATE inscricoes SET presente = 1, dataConfirmacao = :data WHERE raUsuario = :ra AND palestraId = :palestraId")
    int confirmarPresenca(String ra, int palestraId, String data);

    @Query("SELECT * FROM inscricoes WHERE palestraId = :palestraId AND presente = 1")
    List<Inscricao> getPresencasByPalestra(int palestraId);
}

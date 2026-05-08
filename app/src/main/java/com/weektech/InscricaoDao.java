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

    // pega so as palestras que tao rolando
    @Query("SELECT * FROM palestras WHERE ativa = 1")
    List<com.weektech.Palestra> listarInscricoesAtivas();

    // evita que o aluno se inscreva duas vezes na mesma
    @Query("SELECT COUNT(*) FROM inscricoes WHERE raUsuario = :ra AND palestraId = :palestraId")
    int verificarDuplicata(String ra, int palestraId);

    @Query("SELECT * FROM inscricoes WHERE raUsuario = :ra")
    List<Inscricao> listarInscricoesPorUsuario(String ra);

    // faz um join com usuario pra pegar os nomes
    @Transaction
    @Query("SELECT * FROM inscricoes WHERE palestraId = :palestraId")
    List<InscricaoComUsuario> getInscritosComUsuario(int palestraId);

    // pega so quem ja deu o checkin
    @Transaction
    @Query("SELECT * FROM inscricoes WHERE palestraId = :palestraId AND presente = 1")
    List<InscricaoComUsuario> getPresencasComUsuario(int palestraId);

    // marca que o aluno chegou na palestra
    @Query("UPDATE inscricoes SET presente = 1, dataConfirmacao = :data WHERE raUsuario = :ra AND palestraId = :palestraId")
    int confirmarPresenca(String ra, int palestraId, String data);

    @Query("SELECT * FROM inscricoes WHERE palestraId = :palestraId AND presente = 1")
    List<Inscricao> getPresencasByPalestra(int palestraId);
}

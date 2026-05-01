package com.weektech;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface InscricaoDao {
    // Aqui você pode criar uma classe de modelo 'Inscricao' depois se precisar
    // Por enquanto, vamos deixar a estrutura pronta para o banco reconhecer
    @Query("SELECT * FROM palestras WHERE ativa = 1")
    List<com.weektech.Palestra> listarInscricoesAtivas();

    @Query("SELECT COUNT(*) FROM inscricoes WHERE raUsuario = :ra AND palestraId = :palestraId")
    int verificarDuplicata(String ra, int palestraId);
}
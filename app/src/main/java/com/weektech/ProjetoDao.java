package com.weektech;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProjetoDao {

    @Insert
    long inserir(Projeto projeto);

    @Update
    void update(Projeto projeto);

    @Query("SELECT * FROM projetos ORDER BY nomeProjeto ASC")
    List<Projeto> listarTodos();

    @Query("SELECT * FROM projetos WHERE dataApresentacao IS NOT NULL AND dataApresentacao != '' ORDER BY dataApresentacao, horaApresentacao ASC")
    List<Projeto> listarAgendados();

    @Query("SELECT COUNT(*) FROM projetos WHERE ra = :ra")
    int verificarProjetoExistente(String ra);
}

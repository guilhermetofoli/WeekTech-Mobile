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

    // atualiza o projeto (usado pelo admin pra agendar)
    @Update
    void update(Projeto projeto);

    // lista tudo pro admin ver
    @Query("SELECT * FROM projetos ORDER BY nomeProjeto ASC")
    List<Projeto> listarTodos();

    // so os projetos que o admin ja marcou data
    @Query("SELECT * FROM projetos WHERE dataApresentacao IS NOT NULL AND dataApresentacao != '' ORDER BY dataApresentacao, horaInicioApresentacao ASC")
    List<Projeto> listarAgendados();

    // checa se o aluno ja enviou algum projeto
    @Query("SELECT COUNT(*) FROM projetos WHERE ra = :ra")
    int verificarProjetoExistente(String ra);
}

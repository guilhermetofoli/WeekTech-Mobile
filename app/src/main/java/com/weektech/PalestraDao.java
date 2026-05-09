package com.weektech;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.weektech.Palestra;
import java.util.List;

@Dao
public interface PalestraDao {
    @Insert
    long inserir(Palestra palestra);

    // pra atualizar os dados da palestra na edicao
    @Update
    void update(Palestra palestra);

    @Delete
    void delete(Palestra palestra);

    // pega a lista completa de palestras
    @Query("SELECT * FROM palestras ORDER BY id DESC")
    LiveData<List<Palestra>> listarTodas();

    // lista as palestras de um dia especifico que tao ativas
    @Query("SELECT * FROM palestras WHERE data = :dataParam AND ativa = 1")
    LiveData<List<Palestra>> listarPorData(String dataParam);

    // ativa ou desativa a palestra
    @Query("UPDATE palestras SET ativa = :status WHERE id = :id")
    void atualizarStatus(int id, boolean status);

    @Delete
    void deletar(Palestra palestra);
}

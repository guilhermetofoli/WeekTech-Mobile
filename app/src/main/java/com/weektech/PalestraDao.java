package com.weektech;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.weektech.Palestra;
import java.util.List;

@Dao
public interface PalestraDao {
    @Insert
    long inserir(Palestra palestra);

    @Query("SELECT * FROM palestras ORDER BY id DESC")
    LiveData<List<Palestra>> listarTodas();

    // ADICIONE ESTE MÉTODO AQUI:
    @Query("SELECT * FROM palestras WHERE dia = :diaParam AND ativa = 1")
    LiveData<List<Palestra>> listarPorDia(int diaParam);

    @Query("UPDATE palestras SET ativa = :status WHERE id = :id")
    void atualizarStatus(int id, boolean status);

    @Delete
    void deletar(Palestra palestra);
}
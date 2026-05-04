package com.weektech;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UsuarioDao {

    @Insert
    long inserir(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE email = :email AND senha = :senha LIMIT 1")
    Usuario buscarPorEmailESenha(String email, String senha);

    @Query("SELECT COUNT(*) FROM usuarios WHERE email = :email")
    int verificarEmailExistente(String email);

    @Query("SELECT COUNT(*) FROM usuarios WHERE ra = :ra")
    int verificarRaExistente(String ra);

    @Query("SELECT * FROM usuarios WHERE ra = :ra LIMIT 1")
    Usuario buscarPorRa(String ra);

    @Query("SELECT * FROM usuarios ORDER BY nome ASC")
    java.util.List<Usuario> listarTodos();

    @Query("SELECT COUNT(*) FROM usuarios WHERE coffeeBreak = 1")
    int contarCoffeeBreak();
}

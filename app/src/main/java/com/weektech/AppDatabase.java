package com.weektech;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// configuracao do banco de dados room
// aumentei a versao pra resetar o banco por causa das mudancas nas tabelas
@Database(
        entities = {Palestra.class, Inscricao.class, Usuario.class, Projeto.class},
        version = 16,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PalestraDao palestraDao();
    public abstract InscricaoDao inscricaoDao();
    public abstract UsuarioDao usuarioDao();
    public abstract ProjetoDao projetoDao();

    private static volatile AppDatabase INSTANCE;
    // executor pra rodar as querys fora da main thread
    public static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(4);

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "weektech_db")
                            // se mudar a versao ele apaga e cria de novo
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

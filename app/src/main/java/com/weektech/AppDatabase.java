package com.weektech;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.weektech.Palestra;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Palestra.class, Inscricao.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PalestraDao palestraDao();

    public abstract InscricaoDao inscricaoDao();

    private static volatile AppDatabase INSTANCE;
    public static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(4);

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "weektech_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
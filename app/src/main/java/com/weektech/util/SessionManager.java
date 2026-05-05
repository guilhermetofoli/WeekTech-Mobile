package com.weektech.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME      = "WeekTechSession";
    private static final String KEY_IS_LOGGED  = "isLoggedIn";
    private static final String KEY_IS_ADMIN   = "isAdmin";
    private static final String KEY_RA         = "ra_usuario";
    private static final String KEY_NOME       = "nome_usuario";
    private static final String KEY_EMAIL      = "email_usuario";
    private static final String KEY_CURSO      = "curso_usuario";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref   = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(boolean isAdmin, String ra, String nome, String email, String curso) {
        editor.putBoolean(KEY_IS_LOGGED, true);
        editor.putBoolean(KEY_IS_ADMIN,  isAdmin);
        editor.putString(KEY_RA,    ra);
        editor.putString(KEY_NOME,  nome);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_CURSO, curso);
        editor.apply();
    }

    // Backward compat overload
    public void createLoginSession(boolean isAdmin) {
        createLoginSession(isAdmin, "", "", "", "");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED, false);
    }

    public boolean isAdmin() {
        return pref.getBoolean(KEY_IS_ADMIN, false);
    }

    public String getRa()    { return pref.getString(KEY_RA,    ""); }
    public String getNome()  { return pref.getString(KEY_NOME,  ""); }
    public String getEmail() { return pref.getString(KEY_EMAIL, ""); }
    public String getCurso() { return pref.getString(KEY_CURSO, ""); }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}

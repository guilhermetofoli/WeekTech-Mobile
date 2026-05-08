package com.weektech.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    // nomes das chaves pra salvar no sharedpreferences
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

    // cria a sessao quando o cara loga
    public void createLoginSession(boolean isAdmin, String ra, String nome, String email, String curso) {
        editor.putBoolean(KEY_IS_LOGGED, true);
        editor.putBoolean(KEY_IS_ADMIN,  isAdmin);
        editor.putString(KEY_RA,    ra);
        editor.putString(KEY_NOME,  nome);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_CURSO, curso);
        editor.apply();
    }

    // sobrecarga so pra nao quebrar se alguem chamar so com boolean
    public void createLoginSession(boolean isAdmin) {
        createLoginSession(isAdmin, "", "", "", "");
    }

    // ve se o usuario ta logado
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED, false);
    }

    // ve se o cara é adm
    public boolean isAdmin() {
        return pref.getBoolean(KEY_IS_ADMIN, false);
    }

    // funcoes pra pegar os dados salvos
    public String getRa()    { return pref.getString(KEY_RA,    ""); }
    public String getNome()  { return pref.getString(KEY_NOME,  ""); }
    public String getEmail() { return pref.getString(KEY_EMAIL, ""); }
    public String getCurso() { return pref.getString(KEY_CURSO, ""); }

    // limpa tudo no logout
    public void logout() {
        editor.clear();
        editor.apply();
    }
}

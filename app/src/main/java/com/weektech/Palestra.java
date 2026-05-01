package com.weektech;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "palestras")
public class Palestra {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String titulo;
    public String palestrante;
    public String email;      // Adicionado
    public String telefone;   // Adicionado
    public String briefing;   // Adicionado
    public String curriculo;  // Adicionado
    public int    tempo;      // Adicionado
    public String horaInicio;
    public String horaFim;
    public String local;
    public String descricao;
    public int    dia;
    public boolean ativa;

    @Ignore
    public String statusInscricao = StatusInscricao.DISPONIVEL;

    public static class StatusInscricao {
        public static final String DISPONIVEL = "DISPONIVEL";
        public static final String INSCRITO   = "INSCRITO";
        public static final String VISUALIZAR = "VISUALIZAR";
    }

    public Palestra() {}

    // Construtor atualizado para aceitar todos os campos da AdminActivity
    @Ignore
    public Palestra(String titulo, String palestrante, String email, String telefone,
                    String briefing, String curriculo, int tempo, String horaInicio,
                    String horaFim, String local, String descricao, int dia, boolean ativa) {
        this.titulo = titulo;
        this.palestrante = palestrante;
        this.email = email;
        this.telefone = telefone;
        this.briefing = briefing;
        this.curriculo = curriculo;
        this.tempo = tempo;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.local = local;
        this.descricao = descricao;
        this.dia = dia;
        this.ativa = ativa;
    }
}
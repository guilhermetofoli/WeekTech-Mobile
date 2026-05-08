package com.weektech;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

// tabela das palestras no banco
@Entity(tableName = "palestras")
public class Palestra {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String titulo;
    public String palestrante;
    public String email;
    public String telefone;
    public String briefing;
    public String curriculo;
    public int    tempo;
    public String horaInicio;
    public String horaFim;
    public String local;
    public String descricao;
    public int    dia;
    public boolean ativa; // pra saber se a palestra ainda ta valendo

    public String getTitulo() {
        return titulo;
    }

    public String getPalestrante() {
        return palestrante;
    }

    // campos que nao vao pro banco, so pro controle do app
    @Ignore
    public String statusInscricao = StatusInscricao.DISPONIVEL;

    // estados possiveis da inscricao
    public static class StatusInscricao {
        public static final String DISPONIVEL = "DISPONIVEL";
        public static final String INSCRITO   = "INSCRITO";
        public static final String VISUALIZAR = "VISUALIZAR";
    }

    public Palestra() {}

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

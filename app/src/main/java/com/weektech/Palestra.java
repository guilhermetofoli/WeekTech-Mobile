
package com.weektech.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "palestras")
public class Palestra {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String titulo;
    public String palestrante;
    public String horaInicio;
    public String horaFim;
    public String local;
    public String descricao;
    public int    dia;
    public boolean ativa;

    // ── Campo calculado em runtime (não persiste no banco)
    // Possíveis valores: "DISPONIVEL", "INSCRITO", "VISUALIZAR"
    @Ignore
    public String statusInscricao = StatusInscricao.DISPONIVEL;

    // ── Constantes de status
    public static class StatusInscricao {
        public static final String DISPONIVEL = "DISPONIVEL"; // botão azul
        public static final String INSCRITO   = "INSCRITO";   // botão verde
        public static final String VISUALIZAR = "VISUALIZAR"; // botão borda azul
    }

    //Construtor vazio obrigatório para o Room
    public Palestra() {}

    @Ignore
    public Palestra(String titulo, String palestrante, String horaInicio,
                    String horaFim, String local, String descricao,
                    int dia, boolean ativa) {
        this.titulo      = titulo;
        this.palestrante = palestrante;
        this.horaInicio  = horaInicio;
        this.horaFim     = horaFim;
        this.local       = local;
        this.descricao   = descricao;
        this.dia         = dia;
        this.ativa       = ativa;
    }
}
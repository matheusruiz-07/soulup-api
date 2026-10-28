package br.com.soulup.model;

import java.time.LocalDateTime;

public class Resgate {

    private int id;
    private int idUsuario;
    private int idRecompensa;
    private String nomeRecompensa;
    private int pontosDebitados;
    private LocalDateTime dataResgate;

    public Resgate() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdRecompensa() {
        return idRecompensa;
    }

    public void setIdRecompensa(int idRecompensa) {
        this.idRecompensa = idRecompensa;
    }

    public String getNomeRecompensa() {
        return nomeRecompensa;
    }

    public void setNomeRecompensa(String nomeRecompensa) {
        this.nomeRecompensa = nomeRecompensa;
    }

    public int getPontosDebitados() {
        return pontosDebitados;
    }

    public void setPontosDebitados(int pontosDebitados) {
        this.pontosDebitados = pontosDebitados;
    }

    public LocalDateTime getDataResgate() {
        return dataResgate;
    }

    public void setDataResgate(LocalDateTime dataResgate) {
        this.dataResgate = dataResgate;
    }
}

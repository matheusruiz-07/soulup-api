package br.com.soulup.model;

import java.time.LocalDateTime;

public class Interacao {

    private int id;
    private int idAvatar;
    private TipoInteracao tipo;
    private String mensagemUsuario;
    private String respostaAvatar;
    private LocalDateTime dataInteracao;

    public Interacao() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAvatar() {
        return idAvatar;
    }

    public void setIdAvatar(int idAvatar) {
        this.idAvatar = idAvatar;
    }

    public TipoInteracao getTipo() {
        return tipo;
    }

    public void setTipo(TipoInteracao tipo) {
        this.tipo = tipo;
    }

    public String getMensagemUsuario() {
        return mensagemUsuario;
    }

    public void setMensagemUsuario(String mensagemUsuario) {
        this.mensagemUsuario = mensagemUsuario;
    }

    public String getRespostaAvatar() {
        return respostaAvatar;
    }

    public void setRespostaAvatar(String respostaAvatar) {
        this.respostaAvatar = respostaAvatar;
    }

    public LocalDateTime getDataInteracao() {
        return dataInteracao;
    }

    public void setDataInteracao(LocalDateTime dataInteracao) {
        this.dataInteracao = dataInteracao;
    }
}

package br.com.soulup.model;

import java.time.LocalDateTime;

public class RegistroAcao {

    private int id;
    private int idUsuario;
    private int idAcao;
    private String nomeAcao;
    private String descricao;
    private int pontuacao;
    private Impacto impacto;
    private LocalDateTime dataRegistro;

    public RegistroAcao() {
    }

    // a pontuação vem do catálogo de ações, nunca do que o cliente enviou
    public void pontuarCom(Acao acao) {
        this.idAcao = acao.getId();
        this.nomeAcao = acao.getNome();
        this.pontuacao = acao.getPontosPadrao();
        this.impacto = Impacto.classificar(pontuacao);
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

    public int getIdAcao() {
        return idAcao;
    }

    public void setIdAcao(int idAcao) {
        this.idAcao = idAcao;
    }

    public String getNomeAcao() {
        return nomeAcao;
    }

    public void setNomeAcao(String nomeAcao) {
        this.nomeAcao = nomeAcao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public Impacto getImpacto() {
        return impacto;
    }

    public void setImpacto(Impacto impacto) {
        this.impacto = impacto;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}

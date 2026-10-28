package br.com.soulup.model;

public class Acao {

    private int id;
    private int idCategoria;
    private String nomeCategoria;
    private String nome;
    private String descricao;
    private int pontosPadrao;

    public Acao() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getPontosPadrao() {
        return pontosPadrao;
    }

    public void setPontosPadrao(int pontosPadrao) {
        this.pontosPadrao = pontosPadrao;
    }
}

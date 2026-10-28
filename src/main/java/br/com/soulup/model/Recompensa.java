package br.com.soulup.model;

import br.com.soulup.exception.RegraNegocioException;

public class Recompensa {

    private int id;
    private String nome;
    private String descricao;
    private int custoPontos;
    private TipoRecompensa tipo;
    private boolean disponivel = true;

    public Recompensa() {
    }

    public void resgatarPara(Usuario usuario) {
        if (!disponivel) {
            throw new RegraNegocioException("Esta recompensa está indisponível no momento.");
        }
        usuario.debitarSaldo(custoPontos);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCustoPontos() {
        return custoPontos;
    }

    public void setCustoPontos(int custoPontos) {
        this.custoPontos = custoPontos;
    }

    public TipoRecompensa getTipo() {
        return tipo;
    }

    public void setTipo(TipoRecompensa tipo) {
        this.tipo = tipo;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}

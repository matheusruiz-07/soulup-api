package br.com.soulup.model;

import java.time.LocalDate;

import br.com.soulup.exception.RegraNegocioException;

public class Usuario {

    private static final int[] XP_MINIMO_POR_NIVEL = {0, 50, 200, 500, 1000};

    private int id;
    private String nome;
    private String email;
    private LocalDate dataCadastro;
    private int pontosExperiencia;
    private int saldoPontos;
    private int nivel = 1;

    public Usuario() {
    }

    public void adicionarPontos(int pontos) {
        pontosExperiencia += pontos;
        saldoPontos += pontos;
        nivel = calcularNivel(pontosExperiencia);
    }

    public void debitarSaldo(int pontos) {
        if (pontos > saldoPontos) {
            throw new RegraNegocioException("Saldo insuficiente. Faltam " + (pontos - saldoPontos) + " pontos.");
        }
        saldoPontos -= pontos;
    }

    // usado quando um registro de ação é excluído: tira o que ele tinha somado
    public void estornarPontos(int pontos) {
        if (pontos > saldoPontos) {
            throw new RegraNegocioException(
                    "Os pontos deste registro já foram gastos em resgates e não podem ser estornados.");
        }
        pontosExperiencia -= pontos;
        saldoPontos -= pontos;
        nivel = calcularNivel(pontosExperiencia);
    }

    public static int calcularNivel(int pontosExperiencia) {
        int nivel = 1;
        for (int i = 1; i < XP_MINIMO_POR_NIVEL.length; i++) {
            if (pontosExperiencia >= XP_MINIMO_POR_NIVEL[i]) {
                nivel = i + 1;
            }
        }
        return nivel;
    }

    public int getPontosParaProximoNivel() {
        if (nivel >= XP_MINIMO_POR_NIVEL.length) {
            return 0;
        }
        return XP_MINIMO_POR_NIVEL[nivel] - pontosExperiencia;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public int getPontosExperiencia() {
        return pontosExperiencia;
    }

    public void setPontosExperiencia(int pontosExperiencia) {
        this.pontosExperiencia = pontosExperiencia;
    }

    public int getSaldoPontos() {
        return saldoPontos;
    }

    public void setSaldoPontos(int saldoPontos) {
        this.saldoPontos = saldoPontos;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
}

package br.com.soulup.model;

import java.time.LocalDateTime;

import br.com.soulup.exception.RegraNegocioException;

public class UsuarioMissao {

    private int idUsuario;
    private Missao missao;
    private StatusMissao status = StatusMissao.PENDENTE;
    private LocalDateTime dataConclusao;

    public UsuarioMissao() {
    }

    public void concluir(Usuario usuario) {
        if (status == StatusMissao.CONCLUIDA) {
            throw new RegraNegocioException("Esta missão já foi concluída por este usuário.");
        }
        status = StatusMissao.CONCLUIDA;
        dataConclusao = LocalDateTime.now();
        usuario.adicionarPontos(missao.getPontosRecompensa());
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Missao getMissao() {
        return missao;
    }

    public void setMissao(Missao missao) {
        this.missao = missao;
    }

    public StatusMissao getStatus() {
        return status;
    }

    public void setStatus(StatusMissao status) {
        this.status = status;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }
}

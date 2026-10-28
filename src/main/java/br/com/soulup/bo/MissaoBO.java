package br.com.soulup.bo;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.soulup.dao.MissaoDAO;
import br.com.soulup.dao.UsuarioDAO;
import br.com.soulup.dao.UsuarioMissaoDAO;
import br.com.soulup.exception.RegraNegocioException;
import br.com.soulup.model.Missao;
import br.com.soulup.model.Usuario;
import br.com.soulup.model.UsuarioMissao;

@Service
public class MissaoBO extends BaseBO {

    public List<Missao> listar() throws SQLException {
        return consultar(conexao -> new MissaoDAO(conexao).listar());
    }

    public Missao buscar(int id) throws SQLException {
        return consultar(conexao -> exigirExistente(new MissaoDAO(conexao).buscarPorId(id),
                "Missão " + id + " não encontrada."));
    }

    public Missao cadastrar(Missao dados) throws SQLException {
        Missao missao = validar(dados);
        return emTransacao(conexao -> {
            MissaoDAO missaoDAO = new MissaoDAO(conexao);
            exigirNomeLivre(missaoDAO, missao.getNome(), 0);
            missaoDAO.inserir(missao);
            return missao;
        });
    }

    public Missao atualizar(int id, Missao dados) throws SQLException {
        Missao missao = validar(dados);
        missao.setId(id);
        return emTransacao(conexao -> {
            MissaoDAO missaoDAO = new MissaoDAO(conexao);
            exigirExistente(missaoDAO.buscarPorId(id), "Missão " + id + " não encontrada.");
            exigirNomeLivre(missaoDAO, missao.getNome(), id);
            missaoDAO.atualizar(missao);
            return missao;
        });
    }

    public void excluir(int id) throws SQLException {
        emTransacao(conexao -> {
            MissaoDAO missaoDAO = new MissaoDAO(conexao);
            exigirExistente(missaoDAO.buscarPorId(id), "Missão " + id + " não encontrada.");
            if (missaoDAO.possuiParticipantes(id)) {
                throw new RegraNegocioException("Esta missão já tem participantes e não pode ser excluída.");
            }
            missaoDAO.excluir(id);
            return null;
        });
    }

    public List<UsuarioMissao> listarDoUsuario(int idUsuario) throws SQLException {
        return consultar(conexao -> {
            exigirExistente(new UsuarioDAO(conexao).buscarPorId(idUsuario), "Usuário " + idUsuario + " não encontrado.");
            return new UsuarioMissaoDAO(conexao).listarPorUsuario(idUsuario);
        });
    }

    public UsuarioMissao concluir(int idUsuario, int idMissao) throws SQLException {
        return emTransacao(conexao -> {
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexao);
            Usuario usuario = exigirExistente(usuarioDAO.buscarPorId(idUsuario),
                    "Usuário " + idUsuario + " não encontrado.");
            UsuarioMissaoDAO usuarioMissaoDAO = new UsuarioMissaoDAO(conexao);
            UsuarioMissao usuarioMissao = exigirExistente(usuarioMissaoDAO.buscar(idUsuario, idMissao),
                    "Missão " + idMissao + " não encontrada.");

            usuarioMissao.concluir(usuario);
            usuarioMissaoDAO.salvar(usuarioMissao);
            usuarioDAO.atualizar(usuario);
            return usuarioMissao;
        });
    }

    private Missao validar(Missao dados) {
        Missao missao = new Missao();
        missao.setNome(validarTexto(dados.getNome(), "nome", 100, true));
        missao.setDescricao(validarTexto(dados.getDescricao(), "descricao", 300, true));
        validarIntervalo(dados.getPontosRecompensa(), "pontosRecompensa", 1, 9999);
        missao.setPontosRecompensa(dados.getPontosRecompensa());
        missao.setDificuldade(exigirInformado(dados.getDificuldade(), "dificuldade"));
        return missao;
    }

    private void exigirNomeLivre(MissaoDAO missaoDAO, String nome, int idIgnorado) throws SQLException {
        if (missaoDAO.nomeEmUso(nome, idIgnorado)) {
            throw new RegraNegocioException("Já existe uma missão com este nome.");
        }
    }
}

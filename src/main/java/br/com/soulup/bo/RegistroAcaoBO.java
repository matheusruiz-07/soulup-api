package br.com.soulup.bo;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.soulup.dao.AcaoDAO;
import br.com.soulup.dao.RegistroAcaoDAO;
import br.com.soulup.dao.UsuarioDAO;
import br.com.soulup.exception.ValidacaoException;
import br.com.soulup.model.Acao;
import br.com.soulup.model.RegistroAcao;
import br.com.soulup.model.Usuario;

@Service
public class RegistroAcaoBO extends BaseBO {

    public List<RegistroAcao> listarPorUsuario(int idUsuario) throws SQLException {
        return consultar(conexao -> {
            exigirExistente(new UsuarioDAO(conexao).buscarPorId(idUsuario), "Usuário " + idUsuario + " não encontrado.");
            return new RegistroAcaoDAO(conexao).listarPorUsuario(idUsuario);
        });
    }

    public RegistroAcao buscar(int id) throws SQLException {
        return consultar(conexao -> exigirExistente(new RegistroAcaoDAO(conexao).buscarPorId(id),
                "Registro " + id + " não encontrado."));
    }

    public RegistroAcao registrar(int idUsuario, RegistroAcao dados) throws SQLException {
        String descricao = validarTexto(dados.getDescricao(), "descricao", 300, true);

        return emTransacao(conexao -> {
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexao);
            Usuario usuario = exigirExistente(usuarioDAO.buscarPorId(idUsuario),
                    "Usuário " + idUsuario + " não encontrado.");
            Acao acao = new AcaoDAO(conexao).buscarPorId(dados.getIdAcao());
            if (acao == null) {
                throw new ValidacaoException("A ação " + dados.getIdAcao() + " não existe no catálogo.");
            }

            RegistroAcao registro = new RegistroAcao();
            registro.setIdUsuario(idUsuario);
            registro.setDescricao(descricao);
            registro.setDataRegistro(LocalDateTime.now());
            registro.pontuarCom(acao);

            new RegistroAcaoDAO(conexao).inserir(registro);
            usuario.adicionarPontos(registro.getPontuacao());
            usuarioDAO.atualizar(usuario);
            return registro;
        });
    }

    // só a descrição muda; trocar a ação mudaria pontos já somados
    public RegistroAcao atualizar(int id, RegistroAcao dados) throws SQLException {
        String descricao = validarTexto(dados.getDescricao(), "descricao", 300, true);
        return emTransacao(conexao -> {
            RegistroAcaoDAO registroDAO = new RegistroAcaoDAO(conexao);
            RegistroAcao registro = exigirExistente(registroDAO.buscarPorId(id), "Registro " + id + " não encontrado.");
            registroDAO.atualizarDescricao(id, descricao);
            registro.setDescricao(descricao);
            return registro;
        });
    }

    public void excluir(int id) throws SQLException {
        emTransacao(conexao -> {
            RegistroAcaoDAO registroDAO = new RegistroAcaoDAO(conexao);
            RegistroAcao registro = exigirExistente(registroDAO.buscarPorId(id), "Registro " + id + " não encontrado.");
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexao);
            Usuario usuario = usuarioDAO.buscarPorId(registro.getIdUsuario());

            usuario.estornarPontos(registro.getPontuacao());
            usuarioDAO.atualizar(usuario);
            registroDAO.excluir(id);
            return null;
        });
    }
}

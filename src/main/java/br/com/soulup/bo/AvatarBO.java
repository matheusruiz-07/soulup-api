package br.com.soulup.bo;

import java.time.LocalDateTime;
import java.util.List;
import java.sql.SQLException;

import org.springframework.stereotype.Service;

import br.com.soulup.dao.AvatarDAO;
import br.com.soulup.dao.InteracaoDAO;
import br.com.soulup.dao.UsuarioDAO;
import br.com.soulup.exception.ValidacaoException;
import br.com.soulup.model.Avatar;
import br.com.soulup.model.Interacao;
import br.com.soulup.model.TipoInteracao;
import br.com.soulup.model.Usuario;

@Service
public class AvatarBO extends BaseBO {

    public Avatar buscar(int idUsuario) throws SQLException {
        return consultar(conexao -> exigirExistente(new AvatarDAO(conexao).buscarPorUsuario(idUsuario),
                "Avatar do usuário " + idUsuario + " não encontrado."));
    }

    public Avatar atualizar(int idUsuario, Avatar dados) throws SQLException {
        String nome = validarTexto(dados.getNome(), "nome", 50, true);
        String personalidade = validarTexto(dados.getPersonalidade(), "personalidade", 50, true);

        return emTransacao(conexao -> {
            AvatarDAO avatarDAO = new AvatarDAO(conexao);
            Avatar avatar = exigirExistente(avatarDAO.buscarPorUsuario(idUsuario),
                    "Avatar do usuário " + idUsuario + " não encontrado.");
            avatar.setNome(nome);
            avatar.setPersonalidade(personalidade);
            avatarDAO.atualizar(avatar);
            return avatar;
        });
    }

    public List<Interacao> listarInteracoes(int idUsuario) throws SQLException {
        return consultar(conexao -> {
            Avatar avatar = exigirExistente(new AvatarDAO(conexao).buscarPorUsuario(idUsuario),
                    "Avatar do usuário " + idUsuario + " não encontrado.");
            return new InteracaoDAO(conexao).listarPorAvatar(avatar.getId());
        });
    }

    public Interacao interagir(int idUsuario, Interacao dados) throws SQLException {
        TipoInteracao tipo = exigirInformado(dados.getTipo(), "tipo");
        String mensagem = validarTexto(dados.getMensagemUsuario(), "mensagemUsuario", 400, false);
        if (tipo == TipoInteracao.CONVERSA && mensagem == null) {
            throw new ValidacaoException("Para conversar com o Soul, envie uma mensagem.");
        }

        return emTransacao(conexao -> {
            Usuario usuario = exigirExistente(new UsuarioDAO(conexao).buscarPorId(idUsuario),
                    "Usuário " + idUsuario + " não encontrado.");
            AvatarDAO avatarDAO = new AvatarDAO(conexao);
            Avatar avatar = exigirExistente(avatarDAO.buscarPorUsuario(idUsuario),
                    "Avatar do usuário " + idUsuario + " não encontrado.");

            Interacao interacao = new Interacao();
            interacao.setIdAvatar(avatar.getId());
            interacao.setTipo(tipo);
            interacao.setMensagemUsuario(mensagem);
            interacao.setRespostaAvatar(avatar.responder(tipo, usuario));
            interacao.setDataInteracao(LocalDateTime.now());

            new InteracaoDAO(conexao).inserir(interacao);
            avatarDAO.atualizar(avatar);
            return interacao;
        });
    }
}

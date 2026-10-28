package br.com.soulup.bo;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.soulup.dao.AvatarDAO;
import br.com.soulup.dao.InteracaoDAO;
import br.com.soulup.dao.RegistroAcaoDAO;
import br.com.soulup.dao.ResgateDAO;
import br.com.soulup.dao.UsuarioDAO;
import br.com.soulup.dao.UsuarioMissaoDAO;
import br.com.soulup.exception.RegraNegocioException;
import br.com.soulup.model.Avatar;
import br.com.soulup.model.Usuario;

@Service
public class UsuarioBO extends BaseBO {

    public List<Usuario> listar() throws SQLException {
        return consultar(conexao -> new UsuarioDAO(conexao).listar());
    }

    public Usuario buscar(int id) throws SQLException {
        return consultar(conexao -> exigirExistente(new UsuarioDAO(conexao).buscarPorId(id),
                "Usuário " + id + " não encontrado."));
    }

    // todo usuário novo ganha o seu avatar Soul na mesma transação
    public Usuario cadastrar(Usuario dados) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setNome(validarTexto(dados.getNome(), "nome", 100, true));
        usuario.setEmail(validarEmail(dados.getEmail()));

        return emTransacao(conexao -> {
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexao);
            if (usuarioDAO.emailEmUso(usuario.getEmail(), 0)) {
                throw new RegraNegocioException("Já existe uma conta com este e-mail.");
            }
            usuarioDAO.inserir(usuario);

            AvatarDAO avatarDAO = new AvatarDAO(conexao);
            avatarDAO.inserir(Avatar.padraoPara(avatarDAO.proximoId(), usuario.getId()));
            return usuarioDAO.buscarPorId(usuario.getId());
        });
    }

    // pontos e nível não são editáveis por aqui, só mudam por ações, missões e resgates
    public Usuario atualizar(int id, Usuario dados) throws SQLException {
        String nome = validarTexto(dados.getNome(), "nome", 100, true);
        String email = validarEmail(dados.getEmail());

        return emTransacao(conexao -> {
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexao);
            Usuario usuario = exigirExistente(usuarioDAO.buscarPorId(id), "Usuário " + id + " não encontrado.");
            if (usuarioDAO.emailEmUso(email, id)) {
                throw new RegraNegocioException("Já existe outra conta com este e-mail.");
            }
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuarioDAO.atualizar(usuario);
            return usuario;
        });
    }

    public void excluir(int id) throws SQLException {
        emTransacao(conexao -> {
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexao);
            exigirExistente(usuarioDAO.buscarPorId(id), "Usuário " + id + " não encontrado.");
            new InteracaoDAO(conexao).excluirPorUsuario(id);
            new AvatarDAO(conexao).excluirPorUsuario(id);
            new RegistroAcaoDAO(conexao).excluirPorUsuario(id);
            new UsuarioMissaoDAO(conexao).excluirPorUsuario(id);
            new ResgateDAO(conexao).excluirPorUsuario(id);
            usuarioDAO.excluir(id);
            return null;
        });
    }
}

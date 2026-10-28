package br.com.soulup.bo;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.soulup.dao.RecompensaDAO;
import br.com.soulup.dao.ResgateDAO;
import br.com.soulup.dao.UsuarioDAO;
import br.com.soulup.exception.RegraNegocioException;
import br.com.soulup.exception.ValidacaoException;
import br.com.soulup.model.Recompensa;
import br.com.soulup.model.Resgate;
import br.com.soulup.model.Usuario;

@Service
public class RecompensaBO extends BaseBO {

    public List<Recompensa> listar(Boolean somenteDisponiveis) throws SQLException {
        return consultar(conexao -> new RecompensaDAO(conexao).listar(somenteDisponiveis));
    }

    public Recompensa buscar(int id) throws SQLException {
        return consultar(conexao -> exigirExistente(new RecompensaDAO(conexao).buscarPorId(id),
                "Recompensa " + id + " não encontrada."));
    }

    public Recompensa cadastrar(Recompensa dados) throws SQLException {
        Recompensa recompensa = validar(dados);
        return emTransacao(conexao -> {
            new RecompensaDAO(conexao).inserir(recompensa);
            return recompensa;
        });
    }

    public Recompensa atualizar(int id, Recompensa dados) throws SQLException {
        Recompensa recompensa = validar(dados);
        recompensa.setId(id);
        return emTransacao(conexao -> {
            RecompensaDAO recompensaDAO = new RecompensaDAO(conexao);
            exigirExistente(recompensaDAO.buscarPorId(id), "Recompensa " + id + " não encontrada.");
            recompensaDAO.atualizar(recompensa);
            return recompensa;
        });
    }

    public void excluir(int id) throws SQLException {
        emTransacao(conexao -> {
            RecompensaDAO recompensaDAO = new RecompensaDAO(conexao);
            exigirExistente(recompensaDAO.buscarPorId(id), "Recompensa " + id + " não encontrada.");
            if (recompensaDAO.possuiResgates(id)) {
                throw new RegraNegocioException(
                        "Esta recompensa já foi resgatada. Marque como indisponível em vez de excluir.");
            }
            recompensaDAO.excluir(id);
            return null;
        });
    }

    public List<Resgate> listarResgates(int idUsuario) throws SQLException {
        return consultar(conexao -> {
            exigirExistente(new UsuarioDAO(conexao).buscarPorId(idUsuario), "Usuário " + idUsuario + " não encontrado.");
            return new ResgateDAO(conexao).listarPorUsuario(idUsuario);
        });
    }

    // o resgate gasta saldo, mas não mexe na experiência nem no nível
    public Resgate resgatar(int idUsuario, Resgate dados) throws SQLException {
        return emTransacao(conexao -> {
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexao);
            Usuario usuario = exigirExistente(usuarioDAO.buscarPorId(idUsuario),
                    "Usuário " + idUsuario + " não encontrado.");
            Recompensa recompensa = new RecompensaDAO(conexao).buscarPorId(dados.getIdRecompensa());
            if (recompensa == null) {
                throw new ValidacaoException("A recompensa " + dados.getIdRecompensa() + " não existe.");
            }

            recompensa.resgatarPara(usuario);

            Resgate resgate = new Resgate();
            resgate.setIdUsuario(idUsuario);
            resgate.setIdRecompensa(recompensa.getId());
            resgate.setNomeRecompensa(recompensa.getNome());
            resgate.setPontosDebitados(recompensa.getCustoPontos());
            resgate.setDataResgate(LocalDateTime.now());

            new ResgateDAO(conexao).inserir(resgate);
            usuarioDAO.atualizar(usuario);
            return resgate;
        });
    }

    private Recompensa validar(Recompensa dados) {
        Recompensa recompensa = new Recompensa();
        recompensa.setNome(validarTexto(dados.getNome(), "nome", 100, true));
        recompensa.setDescricao(validarTexto(dados.getDescricao(), "descricao", 300, false));
        validarIntervalo(dados.getCustoPontos(), "custoPontos", 1, 99999);
        recompensa.setCustoPontos(dados.getCustoPontos());
        recompensa.setTipo(exigirInformado(dados.getTipo(), "tipo"));
        recompensa.setDisponivel(dados.isDisponivel());
        return recompensa;
    }
}

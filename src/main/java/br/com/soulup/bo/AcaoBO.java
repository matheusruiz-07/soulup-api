package br.com.soulup.bo;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.soulup.dao.AcaoDAO;
import br.com.soulup.dao.CategoriaDAO;
import br.com.soulup.exception.RegraNegocioException;
import br.com.soulup.exception.ValidacaoException;
import br.com.soulup.model.Acao;
import br.com.soulup.model.Categoria;

@Service
public class AcaoBO extends BaseBO {

    public List<Categoria> listarCategorias() throws SQLException {
        return consultar(conexao -> new CategoriaDAO(conexao).listar());
    }

    public List<Acao> listar(Integer idCategoria) throws SQLException {
        return consultar(conexao -> new AcaoDAO(conexao).listar(idCategoria));
    }

    public Acao buscar(int id) throws SQLException {
        return consultar(conexao -> exigirExistente(new AcaoDAO(conexao).buscarPorId(id),
                "Ação " + id + " não encontrada."));
    }

    public Acao cadastrar(Acao dados) throws SQLException {
        Acao acao = validar(dados);
        return emTransacao(conexao -> {
            exigirCategoria(new CategoriaDAO(conexao), acao.getIdCategoria());
            AcaoDAO acaoDAO = new AcaoDAO(conexao);
            acaoDAO.inserir(acao);
            return acaoDAO.buscarPorId(acao.getId());
        });
    }

    public Acao atualizar(int id, Acao dados) throws SQLException {
        Acao acao = validar(dados);
        acao.setId(id);
        return emTransacao(conexao -> {
            AcaoDAO acaoDAO = new AcaoDAO(conexao);
            exigirExistente(acaoDAO.buscarPorId(id), "Ação " + id + " não encontrada.");
            exigirCategoria(new CategoriaDAO(conexao), acao.getIdCategoria());
            acaoDAO.atualizar(acao);
            return acaoDAO.buscarPorId(id);
        });
    }

    public void excluir(int id) throws SQLException {
        emTransacao(conexao -> {
            AcaoDAO acaoDAO = new AcaoDAO(conexao);
            exigirExistente(acaoDAO.buscarPorId(id), "Ação " + id + " não encontrada.");
            if (acaoDAO.possuiRegistros(id)) {
                throw new RegraNegocioException("Esta ação já foi registrada por usuários e não pode ser excluída.");
            }
            acaoDAO.excluir(id);
            return null;
        });
    }

    private Acao validar(Acao dados) {
        Acao acao = new Acao();
        acao.setIdCategoria(dados.getIdCategoria());
        acao.setNome(validarTexto(dados.getNome(), "nome", 100, true));
        acao.setDescricao(validarTexto(dados.getDescricao(), "descricao", 300, false));
        validarIntervalo(dados.getPontosPadrao(), "pontosPadrao", 0, 100);
        acao.setPontosPadrao(dados.getPontosPadrao());
        return acao;
    }

    private void exigirCategoria(CategoriaDAO categoriaDAO, int idCategoria) throws SQLException {
        if (!categoriaDAO.existePorId(idCategoria)) {
            throw new ValidacaoException("A categoria " + idCategoria + " não existe.");
        }
    }
}

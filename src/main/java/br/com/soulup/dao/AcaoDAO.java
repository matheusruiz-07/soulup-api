package br.com.soulup.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.soulup.model.Acao;

public class AcaoDAO extends BaseDAO {

    private static final String SELECT = "SELECT a.id_acao, a.id_categoria, c.nm_categoria, a.nm_acao, "
            + "a.ds_acao, a.qt_pontos_padrao FROM t_su_acao a "
            + "JOIN t_su_categoria c ON c.id_categoria = a.id_categoria ";

    public AcaoDAO(Connection conexao) {
        super(conexao);
    }

    public List<Acao> listar(Integer idCategoria) throws SQLException {
        String sql = SELECT + (idCategoria == null ? "" : "WHERE a.id_categoria = ? ") + "ORDER BY a.nm_acao";
        List<Acao> acoes = new ArrayList<>();
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            if (idCategoria != null) {
                comando.setInt(1, idCategoria);
            }
            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    acoes.add(montar(resultado));
                }
            }
        }
        return acoes;
    }

    public Acao buscarPorId(int id) throws SQLException {
        try (PreparedStatement comando = conexao.prepareStatement(SELECT + "WHERE a.id_acao = ?")) {
            comando.setInt(1, id);
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() ? montar(resultado) : null;
            }
        }
    }

    public void inserir(Acao acao) throws SQLException {
        acao.setId(proximoId("t_su_acao", "id_acao"));
        String sql = "INSERT INTO t_su_acao (id_acao, id_categoria, nm_acao, ds_acao, qt_pontos_padrao) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, acao.getId());
            comando.setInt(2, acao.getIdCategoria());
            comando.setString(3, acao.getNome());
            comando.setString(4, acao.getDescricao());
            comando.setInt(5, acao.getPontosPadrao());
            comando.executeUpdate();
        }
    }

    public void atualizar(Acao acao) throws SQLException {
        String sql = "UPDATE t_su_acao SET id_categoria = ?, nm_acao = ?, ds_acao = ?, qt_pontos_padrao = ? "
                + "WHERE id_acao = ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, acao.getIdCategoria());
            comando.setString(2, acao.getNome());
            comando.setString(3, acao.getDescricao());
            comando.setInt(4, acao.getPontosPadrao());
            comando.setInt(5, acao.getId());
            comando.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        try (PreparedStatement comando = conexao.prepareStatement("DELETE FROM t_su_acao WHERE id_acao = ?")) {
            comando.setInt(1, id);
            comando.executeUpdate();
        }
    }

    public boolean possuiRegistros(int id) throws SQLException {
        return existe("SELECT COUNT(*) FROM t_su_registro_acao WHERE id_acao = ?", id);
    }

    private Acao montar(ResultSet resultado) throws SQLException {
        Acao acao = new Acao();
        acao.setId(resultado.getInt("id_acao"));
        acao.setIdCategoria(resultado.getInt("id_categoria"));
        acao.setNomeCategoria(resultado.getString("nm_categoria"));
        acao.setNome(resultado.getString("nm_acao"));
        acao.setDescricao(resultado.getString("ds_acao"));
        acao.setPontosPadrao(resultado.getInt("qt_pontos_padrao"));
        return acao;
    }
}

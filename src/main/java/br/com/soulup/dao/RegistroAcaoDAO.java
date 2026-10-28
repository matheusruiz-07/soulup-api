package br.com.soulup.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import br.com.soulup.model.Impacto;
import br.com.soulup.model.RegistroAcao;

public class RegistroAcaoDAO extends BaseDAO {

    private static final String SELECT = "SELECT r.id_registro, r.id_usuario, r.id_acao, a.nm_acao, r.ds_registro, "
            + "r.qt_pontuacao, r.ds_impacto, r.dt_registro FROM t_su_registro_acao r "
            + "JOIN t_su_acao a ON a.id_acao = r.id_acao ";

    public RegistroAcaoDAO(Connection conexao) {
        super(conexao);
    }

    public List<RegistroAcao> listarPorUsuario(int idUsuario) throws SQLException {
        String sql = SELECT + "WHERE r.id_usuario = ? ORDER BY r.dt_registro DESC, r.id_registro DESC";
        List<RegistroAcao> registros = new ArrayList<>();
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idUsuario);
            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    registros.add(montar(resultado));
                }
            }
        }
        return registros;
    }

    public RegistroAcao buscarPorId(int id) throws SQLException {
        try (PreparedStatement comando = conexao.prepareStatement(SELECT + "WHERE r.id_registro = ?")) {
            comando.setInt(1, id);
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() ? montar(resultado) : null;
            }
        }
    }

    public void inserir(RegistroAcao registro) throws SQLException {
        registro.setId(proximoId("t_su_registro_acao", "id_registro"));
        String sql = "INSERT INTO t_su_registro_acao (id_registro, id_usuario, id_acao, ds_registro, "
                + "qt_pontuacao, ds_impacto, dt_registro) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, registro.getId());
            comando.setInt(2, registro.getIdUsuario());
            comando.setInt(3, registro.getIdAcao());
            comando.setString(4, registro.getDescricao());
            comando.setInt(5, registro.getPontuacao());
            comando.setString(6, registro.getImpacto().name());
            comando.setTimestamp(7, Timestamp.valueOf(registro.getDataRegistro()));
            comando.executeUpdate();
        }
    }

    public void atualizarDescricao(int id, String descricao) throws SQLException {
        String sql = "UPDATE t_su_registro_acao SET ds_registro = ? WHERE id_registro = ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, descricao);
            comando.setInt(2, id);
            comando.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM t_su_registro_acao WHERE id_registro = ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, id);
            comando.executeUpdate();
        }
    }

    public void excluirPorUsuario(int idUsuario) throws SQLException {
        String sql = "DELETE FROM t_su_registro_acao WHERE id_usuario = ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idUsuario);
            comando.executeUpdate();
        }
    }

    private RegistroAcao montar(ResultSet resultado) throws SQLException {
        RegistroAcao registro = new RegistroAcao();
        registro.setId(resultado.getInt("id_registro"));
        registro.setIdUsuario(resultado.getInt("id_usuario"));
        registro.setIdAcao(resultado.getInt("id_acao"));
        registro.setNomeAcao(resultado.getString("nm_acao"));
        registro.setDescricao(resultado.getString("ds_registro"));
        registro.setPontuacao(resultado.getInt("qt_pontuacao"));
        registro.setImpacto(Impacto.valueOf(resultado.getString("ds_impacto")));
        registro.setDataRegistro(paraDataHora(resultado.getTimestamp("dt_registro")));
        return registro;
    }
}

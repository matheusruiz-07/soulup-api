package br.com.soulup.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import br.com.soulup.model.StatusMissao;
import br.com.soulup.model.UsuarioMissao;

public class UsuarioMissaoDAO extends BaseDAO {

    // LEFT JOIN: missão que o usuário nunca abriu aparece como PENDENTE
    private static final String SELECT = "SELECT m.id_missao, m.nm_missao, m.ds_missao, m.qt_pontos_recompensa, "
            + "m.ds_dificuldade, NVL(um.ds_status, 'PENDENTE') AS ds_status, um.dt_conclusao "
            + "FROM t_su_missao m LEFT JOIN t_su_usuario_missao um "
            + "ON um.id_missao = m.id_missao AND um.id_usuario = ? ";

    public UsuarioMissaoDAO(Connection conexao) {
        super(conexao);
    }

    public List<UsuarioMissao> listarPorUsuario(int idUsuario) throws SQLException {
        List<UsuarioMissao> lista = new ArrayList<>();
        try (PreparedStatement comando = conexao.prepareStatement(SELECT + "ORDER BY m.qt_pontos_recompensa, m.nm_missao")) {
            comando.setInt(1, idUsuario);
            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    lista.add(montar(resultado, idUsuario));
                }
            }
        }
        return lista;
    }

    public UsuarioMissao buscar(int idUsuario, int idMissao) throws SQLException {
        try (PreparedStatement comando = conexao.prepareStatement(SELECT + "WHERE m.id_missao = ?")) {
            comando.setInt(1, idUsuario);
            comando.setInt(2, idMissao);
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() ? montar(resultado, idUsuario) : null;
            }
        }
    }

    public void salvar(UsuarioMissao usuarioMissao) throws SQLException {
        int idUsuario = usuarioMissao.getIdUsuario();
        int idMissao = usuarioMissao.getMissao().getId();
        boolean jaExiste = existe(
                "SELECT COUNT(*) FROM t_su_usuario_missao WHERE id_usuario = ? AND id_missao = ?", idUsuario, idMissao);
        String sql = jaExiste
                ? "UPDATE t_su_usuario_missao SET ds_status = ?, dt_conclusao = ? WHERE id_usuario = ? AND id_missao = ?"
                : "INSERT INTO t_su_usuario_missao (ds_status, dt_conclusao, id_usuario, id_missao) VALUES (?, ?, ?, ?)";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, usuarioMissao.getStatus().name());
            comando.setTimestamp(2, usuarioMissao.getDataConclusao() == null
                    ? null : Timestamp.valueOf(usuarioMissao.getDataConclusao()));
            comando.setInt(3, idUsuario);
            comando.setInt(4, idMissao);
            comando.executeUpdate();
        }
    }

    public void excluirPorUsuario(int idUsuario) throws SQLException {
        try (PreparedStatement comando = conexao.prepareStatement(
                "DELETE FROM t_su_usuario_missao WHERE id_usuario = ?")) {
            comando.setInt(1, idUsuario);
            comando.executeUpdate();
        }
    }

    private UsuarioMissao montar(ResultSet resultado, int idUsuario) throws SQLException {
        UsuarioMissao usuarioMissao = new UsuarioMissao();
        usuarioMissao.setIdUsuario(idUsuario);
        usuarioMissao.setMissao(MissaoDAO.montar(resultado));
        usuarioMissao.setStatus(StatusMissao.valueOf(resultado.getString("ds_status")));
        usuarioMissao.setDataConclusao(paraDataHora(resultado.getTimestamp("dt_conclusao")));
        return usuarioMissao;
    }
}

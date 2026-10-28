package br.com.soulup.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.soulup.model.Avatar;

public class AvatarDAO extends BaseDAO {

    public AvatarDAO(Connection conexao) {
        super(conexao);
    }

    public Avatar buscarPorUsuario(int idUsuario) throws SQLException {
        String sql = "SELECT id_avatar, id_usuario, nm_avatar, ds_personalidade, qt_interacoes "
                + "FROM t_su_avatar WHERE id_usuario = ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idUsuario);
            try (ResultSet resultado = comando.executeQuery()) {
                if (!resultado.next()) {
                    return null;
                }
                Avatar avatar = new Avatar();
                avatar.setId(resultado.getInt("id_avatar"));
                avatar.setIdUsuario(resultado.getInt("id_usuario"));
                avatar.setNome(resultado.getString("nm_avatar"));
                avatar.setPersonalidade(resultado.getString("ds_personalidade"));
                avatar.setQuantidadeInteracoes(resultado.getInt("qt_interacoes"));
                return avatar;
            }
        }
    }

    public int proximoId() throws SQLException {
        return proximoId("t_su_avatar", "id_avatar");
    }

    public void inserir(Avatar avatar) throws SQLException {
        String sql = "INSERT INTO t_su_avatar (id_avatar, id_usuario, nm_avatar, ds_personalidade, qt_interacoes) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, avatar.getId());
            comando.setInt(2, avatar.getIdUsuario());
            comando.setString(3, avatar.getNome());
            comando.setString(4, avatar.getPersonalidade());
            comando.setInt(5, avatar.getQuantidadeInteracoes());
            comando.executeUpdate();
        }
    }

    public void atualizar(Avatar avatar) throws SQLException {
        String sql = "UPDATE t_su_avatar SET nm_avatar = ?, ds_personalidade = ?, qt_interacoes = ? "
                + "WHERE id_avatar = ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, avatar.getNome());
            comando.setString(2, avatar.getPersonalidade());
            comando.setInt(3, avatar.getQuantidadeInteracoes());
            comando.setInt(4, avatar.getId());
            comando.executeUpdate();
        }
    }

    public void excluirPorUsuario(int idUsuario) throws SQLException {
        try (PreparedStatement comando = conexao.prepareStatement("DELETE FROM t_su_avatar WHERE id_usuario = ?")) {
            comando.setInt(1, idUsuario);
            comando.executeUpdate();
        }
    }
}

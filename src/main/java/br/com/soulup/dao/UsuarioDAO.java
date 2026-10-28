package br.com.soulup.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.soulup.model.Usuario;

public class UsuarioDAO extends BaseDAO {

    private static final String SELECT = "SELECT id_usuario, nm_usuario, ds_email, dt_cadastro, "
            + "qt_pontos_experiencia, qt_saldo_pontos, nr_nivel FROM t_su_usuario ";

    public UsuarioDAO(Connection conexao) {
        super(conexao);
    }

    public List<Usuario> listar() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = SELECT + "ORDER BY qt_pontos_experiencia DESC, nm_usuario";
        try (PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) {
                usuarios.add(montar(resultado));
            }
        }
        return usuarios;
    }

    public Usuario buscarPorId(int id) throws SQLException {
        try (PreparedStatement comando = conexao.prepareStatement(SELECT + "WHERE id_usuario = ?")) {
            comando.setInt(1, id);
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() ? montar(resultado) : null;
            }
        }
    }

    public boolean emailEmUso(String email, int idIgnorado) throws SQLException {
        return existe("SELECT COUNT(*) FROM t_su_usuario WHERE LOWER(ds_email) = LOWER(?) AND id_usuario <> ?",
                email, idIgnorado);
    }

    public void inserir(Usuario usuario) throws SQLException {
        usuario.setId(proximoId("t_su_usuario", "id_usuario"));
        String sql = "INSERT INTO t_su_usuario (id_usuario, nm_usuario, ds_email, "
                + "qt_pontos_experiencia, qt_saldo_pontos, nr_nivel) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, usuario.getId());
            comando.setString(2, usuario.getNome());
            comando.setString(3, usuario.getEmail());
            comando.setInt(4, usuario.getPontosExperiencia());
            comando.setInt(5, usuario.getSaldoPontos());
            comando.setInt(6, usuario.getNivel());
            comando.executeUpdate();
        }
    }

    public void atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE t_su_usuario SET nm_usuario = ?, ds_email = ?, qt_pontos_experiencia = ?, "
                + "qt_saldo_pontos = ?, nr_nivel = ? WHERE id_usuario = ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, usuario.getNome());
            comando.setString(2, usuario.getEmail());
            comando.setInt(3, usuario.getPontosExperiencia());
            comando.setInt(4, usuario.getSaldoPontos());
            comando.setInt(5, usuario.getNivel());
            comando.setInt(6, usuario.getId());
            comando.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        try (PreparedStatement comando = conexao.prepareStatement("DELETE FROM t_su_usuario WHERE id_usuario = ?")) {
            comando.setInt(1, id);
            comando.executeUpdate();
        }
    }

    private Usuario montar(ResultSet resultado) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(resultado.getInt("id_usuario"));
        usuario.setNome(resultado.getString("nm_usuario"));
        usuario.setEmail(resultado.getString("ds_email"));
        usuario.setDataCadastro(resultado.getDate("dt_cadastro").toLocalDate());
        usuario.setPontosExperiencia(resultado.getInt("qt_pontos_experiencia"));
        usuario.setSaldoPontos(resultado.getInt("qt_saldo_pontos"));
        usuario.setNivel(resultado.getInt("nr_nivel"));
        return usuario;
    }
}

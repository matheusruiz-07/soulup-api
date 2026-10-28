package br.com.soulup.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.soulup.model.Dificuldade;
import br.com.soulup.model.Missao;

public class MissaoDAO extends BaseDAO {

    private static final String SELECT = "SELECT id_missao, nm_missao, ds_missao, qt_pontos_recompensa, "
            + "ds_dificuldade FROM t_su_missao ";

    public MissaoDAO(Connection conexao) {
        super(conexao);
    }

    public List<Missao> listar() throws SQLException {
        List<Missao> missoes = new ArrayList<>();
        try (PreparedStatement comando = conexao.prepareStatement(SELECT + "ORDER BY qt_pontos_recompensa, nm_missao");
             ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) {
                missoes.add(montar(resultado));
            }
        }
        return missoes;
    }

    public Missao buscarPorId(int id) throws SQLException {
        try (PreparedStatement comando = conexao.prepareStatement(SELECT + "WHERE id_missao = ?")) {
            comando.setInt(1, id);
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() ? montar(resultado) : null;
            }
        }
    }

    public boolean nomeEmUso(String nome, int idIgnorado) throws SQLException {
        return existe("SELECT COUNT(*) FROM t_su_missao WHERE LOWER(nm_missao) = LOWER(?) AND id_missao <> ?",
                nome, idIgnorado);
    }

    public void inserir(Missao missao) throws SQLException {
        missao.setId(proximoId("t_su_missao", "id_missao"));
        String sql = "INSERT INTO t_su_missao (id_missao, nm_missao, ds_missao, qt_pontos_recompensa, ds_dificuldade) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, missao.getId());
            comando.setString(2, missao.getNome());
            comando.setString(3, missao.getDescricao());
            comando.setInt(4, missao.getPontosRecompensa());
            comando.setString(5, missao.getDificuldade().name());
            comando.executeUpdate();
        }
    }

    public void atualizar(Missao missao) throws SQLException {
        String sql = "UPDATE t_su_missao SET nm_missao = ?, ds_missao = ?, qt_pontos_recompensa = ?, "
                + "ds_dificuldade = ? WHERE id_missao = ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, missao.getNome());
            comando.setString(2, missao.getDescricao());
            comando.setInt(3, missao.getPontosRecompensa());
            comando.setString(4, missao.getDificuldade().name());
            comando.setInt(5, missao.getId());
            comando.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        try (PreparedStatement comando = conexao.prepareStatement("DELETE FROM t_su_missao WHERE id_missao = ?")) {
            comando.setInt(1, id);
            comando.executeUpdate();
        }
    }

    public boolean possuiParticipantes(int id) throws SQLException {
        return existe("SELECT COUNT(*) FROM t_su_usuario_missao WHERE id_missao = ?", id);
    }

    static Missao montar(ResultSet resultado) throws SQLException {
        Missao missao = new Missao();
        missao.setId(resultado.getInt("id_missao"));
        missao.setNome(resultado.getString("nm_missao"));
        missao.setDescricao(resultado.getString("ds_missao"));
        missao.setPontosRecompensa(resultado.getInt("qt_pontos_recompensa"));
        missao.setDificuldade(Dificuldade.valueOf(resultado.getString("ds_dificuldade")));
        return missao;
    }
}

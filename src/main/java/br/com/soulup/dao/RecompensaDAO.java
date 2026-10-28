package br.com.soulup.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.soulup.model.Recompensa;
import br.com.soulup.model.TipoRecompensa;

public class RecompensaDAO extends BaseDAO {

    private static final String SELECT = "SELECT id_recompensa, nm_recompensa, ds_recompensa, qt_custo_pontos, "
            + "ds_tipo, st_disponivel FROM t_su_recompensa ";

    public RecompensaDAO(Connection conexao) {
        super(conexao);
    }

    public List<Recompensa> listar(Boolean somenteDisponiveis) throws SQLException {
        String filtro = Boolean.TRUE.equals(somenteDisponiveis) ? "WHERE st_disponivel = 'S' " : "";
        List<Recompensa> recompensas = new ArrayList<>();
        try (PreparedStatement comando = conexao.prepareStatement(SELECT + filtro + "ORDER BY qt_custo_pontos");
             ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) {
                recompensas.add(montar(resultado));
            }
        }
        return recompensas;
    }

    public Recompensa buscarPorId(int id) throws SQLException {
        try (PreparedStatement comando = conexao.prepareStatement(SELECT + "WHERE id_recompensa = ?")) {
            comando.setInt(1, id);
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() ? montar(resultado) : null;
            }
        }
    }

    public void inserir(Recompensa recompensa) throws SQLException {
        recompensa.setId(proximoId("t_su_recompensa", "id_recompensa"));
        String sql = "INSERT INTO t_su_recompensa (id_recompensa, nm_recompensa, ds_recompensa, qt_custo_pontos, "
                + "ds_tipo, st_disponivel) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, recompensa.getId());
            comando.setString(2, recompensa.getNome());
            comando.setString(3, recompensa.getDescricao());
            comando.setInt(4, recompensa.getCustoPontos());
            comando.setString(5, recompensa.getTipo().name());
            comando.setString(6, recompensa.isDisponivel() ? "S" : "N");
            comando.executeUpdate();
        }
    }

    public void atualizar(Recompensa recompensa) throws SQLException {
        String sql = "UPDATE t_su_recompensa SET nm_recompensa = ?, ds_recompensa = ?, qt_custo_pontos = ?, "
                + "ds_tipo = ?, st_disponivel = ? WHERE id_recompensa = ?";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, recompensa.getNome());
            comando.setString(2, recompensa.getDescricao());
            comando.setInt(3, recompensa.getCustoPontos());
            comando.setString(4, recompensa.getTipo().name());
            comando.setString(5, recompensa.isDisponivel() ? "S" : "N");
            comando.setInt(6, recompensa.getId());
            comando.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        try (PreparedStatement comando = conexao.prepareStatement(
                "DELETE FROM t_su_recompensa WHERE id_recompensa = ?")) {
            comando.setInt(1, id);
            comando.executeUpdate();
        }
    }

    public boolean possuiResgates(int id) throws SQLException {
        return existe("SELECT COUNT(*) FROM t_su_resgate WHERE id_recompensa = ?", id);
    }

    private Recompensa montar(ResultSet resultado) throws SQLException {
        Recompensa recompensa = new Recompensa();
        recompensa.setId(resultado.getInt("id_recompensa"));
        recompensa.setNome(resultado.getString("nm_recompensa"));
        recompensa.setDescricao(resultado.getString("ds_recompensa"));
        recompensa.setCustoPontos(resultado.getInt("qt_custo_pontos"));
        recompensa.setTipo(TipoRecompensa.valueOf(resultado.getString("ds_tipo")));
        recompensa.setDisponivel("S".equals(resultado.getString("st_disponivel")));
        return recompensa;
    }
}

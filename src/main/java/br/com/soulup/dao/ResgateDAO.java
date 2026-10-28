package br.com.soulup.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import br.com.soulup.model.Resgate;

public class ResgateDAO extends BaseDAO {

    public ResgateDAO(Connection conexao) {
        super(conexao);
    }

    public List<Resgate> listarPorUsuario(int idUsuario) throws SQLException {
        String sql = "SELECT r.id_resgate, r.id_usuario, r.id_recompensa, c.nm_recompensa, r.qt_pontos_debitados, "
                + "r.dt_resgate FROM t_su_resgate r JOIN t_su_recompensa c ON c.id_recompensa = r.id_recompensa "
                + "WHERE r.id_usuario = ? ORDER BY r.dt_resgate DESC, r.id_resgate DESC";
        List<Resgate> resgates = new ArrayList<>();
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idUsuario);
            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    Resgate resgate = new Resgate();
                    resgate.setId(resultado.getInt("id_resgate"));
                    resgate.setIdUsuario(resultado.getInt("id_usuario"));
                    resgate.setIdRecompensa(resultado.getInt("id_recompensa"));
                    resgate.setNomeRecompensa(resultado.getString("nm_recompensa"));
                    resgate.setPontosDebitados(resultado.getInt("qt_pontos_debitados"));
                    resgate.setDataResgate(paraDataHora(resultado.getTimestamp("dt_resgate")));
                    resgates.add(resgate);
                }
            }
        }
        return resgates;
    }

    public void inserir(Resgate resgate) throws SQLException {
        resgate.setId(proximoId("t_su_resgate", "id_resgate"));
        String sql = "INSERT INTO t_su_resgate (id_resgate, id_usuario, id_recompensa, qt_pontos_debitados, dt_resgate) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, resgate.getId());
            comando.setInt(2, resgate.getIdUsuario());
            comando.setInt(3, resgate.getIdRecompensa());
            comando.setInt(4, resgate.getPontosDebitados());
            comando.setTimestamp(5, Timestamp.valueOf(resgate.getDataResgate()));
            comando.executeUpdate();
        }
    }

    public void excluirPorUsuario(int idUsuario) throws SQLException {
        try (PreparedStatement comando = conexao.prepareStatement("DELETE FROM t_su_resgate WHERE id_usuario = ?")) {
            comando.setInt(1, idUsuario);
            comando.executeUpdate();
        }
    }
}

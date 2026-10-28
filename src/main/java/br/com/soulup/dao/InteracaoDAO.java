package br.com.soulup.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import br.com.soulup.model.Interacao;
import br.com.soulup.model.TipoInteracao;

public class InteracaoDAO extends BaseDAO {

    public InteracaoDAO(Connection conexao) {
        super(conexao);
    }

    public List<Interacao> listarPorAvatar(int idAvatar) throws SQLException {
        String sql = "SELECT id_interacao, id_avatar, ds_tipo, ds_mensagem_usuario, ds_resposta_avatar, dt_interacao "
                + "FROM t_su_interacao WHERE id_avatar = ? ORDER BY dt_interacao DESC, id_interacao DESC";
        List<Interacao> interacoes = new ArrayList<>();
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idAvatar);
            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    Interacao interacao = new Interacao();
                    interacao.setId(resultado.getInt("id_interacao"));
                    interacao.setIdAvatar(resultado.getInt("id_avatar"));
                    interacao.setTipo(TipoInteracao.valueOf(resultado.getString("ds_tipo")));
                    interacao.setMensagemUsuario(resultado.getString("ds_mensagem_usuario"));
                    interacao.setRespostaAvatar(resultado.getString("ds_resposta_avatar"));
                    interacao.setDataInteracao(paraDataHora(resultado.getTimestamp("dt_interacao")));
                    interacoes.add(interacao);
                }
            }
        }
        return interacoes;
    }

    public void inserir(Interacao interacao) throws SQLException {
        interacao.setId(proximoId("t_su_interacao", "id_interacao"));
        String sql = "INSERT INTO t_su_interacao (id_interacao, id_avatar, ds_tipo, ds_mensagem_usuario, "
                + "ds_resposta_avatar, dt_interacao) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, interacao.getId());
            comando.setInt(2, interacao.getIdAvatar());
            comando.setString(3, interacao.getTipo().name());
            comando.setString(4, interacao.getMensagemUsuario());
            comando.setString(5, interacao.getRespostaAvatar());
            comando.setTimestamp(6, Timestamp.valueOf(interacao.getDataInteracao()));
            comando.executeUpdate();
        }
    }

    public void excluirPorUsuario(int idUsuario) throws SQLException {
        String sql = "DELETE FROM t_su_interacao WHERE id_avatar IN "
                + "(SELECT id_avatar FROM t_su_avatar WHERE id_usuario = ?)";
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idUsuario);
            comando.executeUpdate();
        }
    }
}

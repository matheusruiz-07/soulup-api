package br.com.soulup.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class BaseDAO {

    protected final Connection conexao;

    protected BaseDAO(Connection conexao) {
        this.conexao = conexao;
    }

    // as tabelas do SoulUp não têm sequence, então o próximo id sai do maior id gravado
    protected int proximoId(String tabela, String colunaId) throws SQLException {
        String sql = "SELECT NVL(MAX(" + colunaId + "), 0) + 1 FROM " + tabela;
        try (PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {
            resultado.next();
            return resultado.getInt(1);
        }
    }

    protected boolean existe(String sqlContagem, Object... parametros) throws SQLException {
        try (PreparedStatement comando = conexao.prepareStatement(sqlContagem)) {
            for (int i = 0; i < parametros.length; i++) {
                comando.setObject(i + 1, parametros[i]);
            }
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() && resultado.getInt(1) > 0;
            }
        }
    }

    protected static LocalDateTime paraDataHora(Timestamp valor) {
        return valor == null ? null : valor.toLocalDateTime();
    }
}

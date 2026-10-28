package br.com.soulup.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.soulup.model.Categoria;

public class CategoriaDAO extends BaseDAO {

    public CategoriaDAO(Connection conexao) {
        super(conexao);
    }

    public List<Categoria> listar() throws SQLException {
        String sql = "SELECT id_categoria, nm_categoria FROM t_su_categoria ORDER BY nm_categoria";
        List<Categoria> categorias = new ArrayList<>();
        try (PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) {
                categorias.add(new Categoria(resultado.getInt("id_categoria"), resultado.getString("nm_categoria")));
            }
        }
        return categorias;
    }

    public boolean existePorId(int id) throws SQLException {
        return existe("SELECT COUNT(*) FROM t_su_categoria WHERE id_categoria = ?", id);
    }
}

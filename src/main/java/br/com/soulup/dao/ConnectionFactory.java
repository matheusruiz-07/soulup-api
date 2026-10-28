package br.com.soulup.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// As credenciais vêm de variáveis de ambiente para a senha não ficar no repositório.
public final class ConnectionFactory {

    private static final String URL = lerVariavel("DB_URL", "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL");
    private static final String USUARIO = lerVariavel("DB_USER", "rm570701");
    private static final String SENHA = System.getenv("DB_PASSWORD");

    static {
        DriverManager.setLoginTimeout(15);
    }

    private ConnectionFactory() {
    }

    public static Connection obterConexao() throws SQLException {
        if (SENHA == null || SENHA.isBlank()) {
            throw new SQLException("A variável de ambiente DB_PASSWORD não foi definida.");
        }
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    private static String lerVariavel(String nome, String padrao) {
        String valor = System.getenv(nome);
        return valor == null || valor.isBlank() ? padrao : valor;
    }
}

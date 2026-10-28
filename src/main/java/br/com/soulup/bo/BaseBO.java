package br.com.soulup.bo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;

import br.com.soulup.dao.ConnectionFactory;
import br.com.soulup.exception.RecursoNaoEncontradoException;
import br.com.soulup.exception.ValidacaoException;

public abstract class BaseBO {

    private static final Pattern FORMATO_EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    @FunctionalInterface
    protected interface Operacao<T> {
        T executar(Connection conexao) throws SQLException;
    }

    protected <T> T consultar(Operacao<T> operacao) throws SQLException {
        try (Connection conexao = ConnectionFactory.obterConexao()) {
            return operacao.executar(conexao);
        }
    }

    // tudo dentro da operação é gravado junto, ou nada é gravado
    protected <T> T emTransacao(Operacao<T> operacao) throws SQLException {
        try (Connection conexao = ConnectionFactory.obterConexao()) {
            conexao.setAutoCommit(false);
            try {
                T resultado = operacao.executar(conexao);
                conexao.commit();
                return resultado;
            } catch (SQLException | RuntimeException e) {
                conexao.rollback();
                throw e;
            }
        }
    }

    protected <T> T exigirExistente(T objeto, String mensagem) {
        if (objeto == null) {
            throw new RecursoNaoEncontradoException(mensagem);
        }
        return objeto;
    }

    protected <T> T exigirInformado(T valor, String campo) {
        if (valor == null) {
            throw new ValidacaoException("O campo " + campo + " é obrigatório.");
        }
        return valor;
    }

    protected String validarTexto(String valor, String campo, int tamanhoMaximo, boolean obrigatorio) {
        String texto = valor == null ? "" : valor.trim();
        if (texto.isEmpty()) {
            if (obrigatorio) {
                throw new ValidacaoException("O campo " + campo + " é obrigatório.");
            }
            return null;
        }
        if (texto.length() > tamanhoMaximo) {
            throw new ValidacaoException("O campo " + campo + " aceita no máximo " + tamanhoMaximo + " caracteres.");
        }
        return texto;
    }

    protected void validarIntervalo(int valor, String campo, int minimo, int maximo) {
        if (valor < minimo || valor > maximo) {
            throw new ValidacaoException("O campo " + campo + " deve estar entre " + minimo + " e " + maximo + ".");
        }
    }

    protected String validarEmail(String email) {
        String texto = validarTexto(email, "email", 150, true).toLowerCase();
        if (!FORMATO_EMAIL.matcher(texto).matches()) {
            throw new ValidacaoException("Informe um e-mail válido, como nome@exemplo.com.");
        }
        return texto;
    }
}

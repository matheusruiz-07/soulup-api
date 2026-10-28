package br.com.soulup.exception;

import java.time.LocalDateTime;

public record ErroResposta(int status, String erro, String mensagem, String caminho, LocalDateTime momento) {
}

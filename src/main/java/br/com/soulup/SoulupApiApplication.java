package br.com.soulup;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Equipe SoulUp | Turma 1TDSPO
// Matheus Ruiz Giatti (RM 570701) e Matheus Leite Souza das Virgens (RM 573416)
@SpringBootApplication
public class SoulupApiApplication {

    public static void main(String[] args) {
        // o servidor de deploy roda em UTC; as datas gravadas no Oracle seguem o horário de Brasília
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
        SpringApplication.run(SoulupApiApplication.class, args);
    }
}

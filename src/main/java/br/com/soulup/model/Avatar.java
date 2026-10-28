package br.com.soulup.model;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Avatar {

    private static final List<String> SUGESTOES = List.of(
            "Que tal ir de transporte público hoje?",
            "Já separou o lixo reciclável desta semana?",
            "Tire da tomada os aparelhos que ficam em standby.",
            "Leve a sua sacola quando for ao mercado.",
            "Um banho de 5 minutos economiza bastante água.");

    private int id;
    private int idUsuario;
    private String nome;
    private String personalidade;
    private int quantidadeInteracoes;

    public Avatar() {
    }

    // método de fábrica em vez de construtor: com um construtor de dois ints,
    // o Jackson 3 tentaria usá-lo para ler o JSON do PUT e recusaria o corpo
    public static Avatar padraoPara(int id, int idUsuario) {
        Avatar avatar = new Avatar();
        avatar.id = id;
        avatar.idUsuario = idUsuario;
        avatar.nome = "Soul";
        avatar.personalidade = "Motivador";
        return avatar;
    }

    public String responder(TipoInteracao tipo, Usuario usuario) {
        quantidadeInteracoes++;
        return switch (tipo) {
            case SUGESTAO -> SUGESTOES.get(ThreadLocalRandom.current().nextInt(SUGESTOES.size()));
            case MOTIVACAO -> mensagemMotivacional(usuario.getPontosExperiencia());
            case PROGRESSO -> mensagemProgresso(usuario);
            case CONVERSA -> "Anotado, " + usuario.getNome() + ". Se quiser uma ideia de ação, peça uma sugestão.";
        };
    }

    private String mensagemMotivacional(int pontosExperiencia) {
        if (pontosExperiencia >= 1000) {
            return "Você chegou ao nível máximo. Continue servindo de exemplo!";
        }
        if (pontosExperiencia >= 500) {
            return "Seu progresso está ótimo, falta pouco para o topo.";
        }
        if (pontosExperiencia >= 200) {
            return "Bom trabalho! Cada ação registrada conta.";
        }
        if (pontosExperiencia >= 50) {
            return "Bom começo! Continue registrando suas ações.";
        }
        return "Bem-vindo! Registre a sua primeira ação e vamos evoluir juntos.";
    }

    private String mensagemProgresso(Usuario usuario) {
        String texto = "Você está no nível " + usuario.getNivel() + " com "
                + usuario.getPontosExperiencia() + " pontos de experiência e "
                + usuario.getSaldoPontos() + " de saldo.";
        if (usuario.getPontosParaProximoNivel() > 0) {
            texto += " Faltam " + usuario.getPontosParaProximoNivel() + " para o próximo nível.";
        }
        return texto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPersonalidade() {
        return personalidade;
    }

    public void setPersonalidade(String personalidade) {
        this.personalidade = personalidade;
    }

    public int getQuantidadeInteracoes() {
        return quantidadeInteracoes;
    }

    public void setQuantidadeInteracoes(int quantidadeInteracoes) {
        this.quantidadeInteracoes = quantidadeInteracoes;
    }
}

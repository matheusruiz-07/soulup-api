package br.com.soulup.model;

public enum Impacto {
    MINIMO, BAIXO, MEDIO, ALTO;

    public static Impacto classificar(int pontuacao) {
        if (pontuacao >= 80) {
            return ALTO;
        }
        if (pontuacao >= 50) {
            return MEDIO;
        }
        if (pontuacao >= 20) {
            return BAIXO;
        }
        return MINIMO;
    }
}

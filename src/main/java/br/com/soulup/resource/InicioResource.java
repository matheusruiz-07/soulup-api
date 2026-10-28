package br.com.soulup.resource;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Página de entrada do deploy: mostra que a API está no ar e quais recursos existem.
@RestController
public class InicioResource {

    @GetMapping("/")
    public Map<String, Object> inicio() {
        return Map.of(
                "api", "SoulUp",
                "status", "no ar",
                "recursos", List.of(
                        "/usuarios", "/usuarios/{id}/avatar", "/usuarios/{id}/interacoes",
                        "/usuarios/{id}/registros", "/usuarios/{id}/missoes", "/usuarios/{id}/resgates",
                        "/categorias", "/acoes", "/registros/{id}", "/missoes", "/recompensas"));
    }
}

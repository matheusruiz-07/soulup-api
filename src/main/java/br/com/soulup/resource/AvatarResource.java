package br.com.soulup.resource;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.soulup.bo.AvatarBO;
import br.com.soulup.model.Avatar;
import br.com.soulup.model.Interacao;

@RestController
@RequestMapping("/usuarios/{idUsuario}")
public class AvatarResource {

    private final AvatarBO avatarBO;

    public AvatarResource(AvatarBO avatarBO) {
        this.avatarBO = avatarBO;
    }

    @GetMapping("/avatar")
    public Avatar buscar(@PathVariable int idUsuario) throws SQLException {
        return avatarBO.buscar(idUsuario);
    }

    @PutMapping("/avatar")
    public Avatar atualizar(@PathVariable int idUsuario, @RequestBody Avatar dados) throws SQLException {
        return avatarBO.atualizar(idUsuario, dados);
    }

    @GetMapping("/interacoes")
    public List<Interacao> listarInteracoes(@PathVariable int idUsuario) throws SQLException {
        return avatarBO.listarInteracoes(idUsuario);
    }

    @PostMapping("/interacoes")
    public ResponseEntity<Interacao> interagir(@PathVariable int idUsuario, @RequestBody Interacao dados)
            throws SQLException {
        return ResponseEntity.status(HttpStatus.CREATED).body(avatarBO.interagir(idUsuario, dados));
    }
}

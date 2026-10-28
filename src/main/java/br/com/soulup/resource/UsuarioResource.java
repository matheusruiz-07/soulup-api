package br.com.soulup.resource;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.soulup.bo.UsuarioBO;
import br.com.soulup.model.Usuario;

@RestController
@RequestMapping("/usuarios")
public class UsuarioResource {

    private final UsuarioBO usuarioBO;

    public UsuarioResource(UsuarioBO usuarioBO) {
        this.usuarioBO = usuarioBO;
    }

    @GetMapping
    public List<Usuario> listar() throws SQLException {
        return usuarioBO.listar();
    }

    @GetMapping("/{id}")
    public Usuario buscar(@PathVariable int id) throws SQLException {
        return usuarioBO.buscar(id);
    }

    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario dados) throws SQLException {
        Usuario usuario = usuarioBO.cadastrar(dados);
        return ResponseEntity.created(URI.create("/usuarios/" + usuario.getId())).body(usuario);
    }

    @PutMapping("/{id}")
    public Usuario atualizar(@PathVariable int id, @RequestBody Usuario dados) throws SQLException {
        return usuarioBO.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable int id) throws SQLException {
        usuarioBO.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

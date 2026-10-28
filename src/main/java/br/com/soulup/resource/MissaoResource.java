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
import org.springframework.web.bind.annotation.RestController;

import br.com.soulup.bo.MissaoBO;
import br.com.soulup.model.Missao;
import br.com.soulup.model.UsuarioMissao;

@RestController
public class MissaoResource {

    private final MissaoBO missaoBO;

    public MissaoResource(MissaoBO missaoBO) {
        this.missaoBO = missaoBO;
    }

    @GetMapping("/missoes")
    public List<Missao> listar() throws SQLException {
        return missaoBO.listar();
    }

    @GetMapping("/missoes/{id}")
    public Missao buscar(@PathVariable int id) throws SQLException {
        return missaoBO.buscar(id);
    }

    @PostMapping("/missoes")
    public ResponseEntity<Missao> cadastrar(@RequestBody Missao dados) throws SQLException {
        Missao missao = missaoBO.cadastrar(dados);
        return ResponseEntity.created(URI.create("/missoes/" + missao.getId())).body(missao);
    }

    @PutMapping("/missoes/{id}")
    public Missao atualizar(@PathVariable int id, @RequestBody Missao dados) throws SQLException {
        return missaoBO.atualizar(id, dados);
    }

    @DeleteMapping("/missoes/{id}")
    public ResponseEntity<Void> excluir(@PathVariable int id) throws SQLException {
        missaoBO.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuarios/{idUsuario}/missoes")
    public List<UsuarioMissao> listarDoUsuario(@PathVariable int idUsuario) throws SQLException {
        return missaoBO.listarDoUsuario(idUsuario);
    }

    @PostMapping("/usuarios/{idUsuario}/missoes/{idMissao}/conclusao")
    public UsuarioMissao concluir(@PathVariable int idUsuario, @PathVariable int idMissao) throws SQLException {
        return missaoBO.concluir(idUsuario, idMissao);
    }
}

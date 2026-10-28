package br.com.soulup.resource;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.soulup.bo.RecompensaBO;
import br.com.soulup.model.Recompensa;
import br.com.soulup.model.Resgate;

@RestController
public class RecompensaResource {

    private final RecompensaBO recompensaBO;

    public RecompensaResource(RecompensaBO recompensaBO) {
        this.recompensaBO = recompensaBO;
    }

    @GetMapping("/recompensas")
    public List<Recompensa> listar(@RequestParam(required = false) Boolean disponivel) throws SQLException {
        return recompensaBO.listar(disponivel);
    }

    @GetMapping("/recompensas/{id}")
    public Recompensa buscar(@PathVariable int id) throws SQLException {
        return recompensaBO.buscar(id);
    }

    @PostMapping("/recompensas")
    public ResponseEntity<Recompensa> cadastrar(@RequestBody Recompensa dados) throws SQLException {
        Recompensa recompensa = recompensaBO.cadastrar(dados);
        return ResponseEntity.created(URI.create("/recompensas/" + recompensa.getId())).body(recompensa);
    }

    @PutMapping("/recompensas/{id}")
    public Recompensa atualizar(@PathVariable int id, @RequestBody Recompensa dados) throws SQLException {
        return recompensaBO.atualizar(id, dados);
    }

    @DeleteMapping("/recompensas/{id}")
    public ResponseEntity<Void> excluir(@PathVariable int id) throws SQLException {
        recompensaBO.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuarios/{idUsuario}/resgates")
    public List<Resgate> listarResgates(@PathVariable int idUsuario) throws SQLException {
        return recompensaBO.listarResgates(idUsuario);
    }

    @PostMapping("/usuarios/{idUsuario}/resgates")
    public ResponseEntity<Resgate> resgatar(@PathVariable int idUsuario, @RequestBody Resgate dados)
            throws SQLException {
        return ResponseEntity.status(HttpStatus.CREATED).body(recompensaBO.resgatar(idUsuario, dados));
    }
}

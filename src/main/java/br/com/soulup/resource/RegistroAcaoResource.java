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

import br.com.soulup.bo.RegistroAcaoBO;
import br.com.soulup.model.RegistroAcao;

@RestController
public class RegistroAcaoResource {

    private final RegistroAcaoBO registroAcaoBO;

    public RegistroAcaoResource(RegistroAcaoBO registroAcaoBO) {
        this.registroAcaoBO = registroAcaoBO;
    }

    @GetMapping("/usuarios/{idUsuario}/registros")
    public List<RegistroAcao> listarPorUsuario(@PathVariable int idUsuario) throws SQLException {
        return registroAcaoBO.listarPorUsuario(idUsuario);
    }

    @PostMapping("/usuarios/{idUsuario}/registros")
    public ResponseEntity<RegistroAcao> registrar(@PathVariable int idUsuario, @RequestBody RegistroAcao dados)
            throws SQLException {
        RegistroAcao registro = registroAcaoBO.registrar(idUsuario, dados);
        return ResponseEntity.created(URI.create("/registros/" + registro.getId())).body(registro);
    }

    @GetMapping("/registros/{id}")
    public RegistroAcao buscar(@PathVariable int id) throws SQLException {
        return registroAcaoBO.buscar(id);
    }

    @PutMapping("/registros/{id}")
    public RegistroAcao atualizar(@PathVariable int id, @RequestBody RegistroAcao dados) throws SQLException {
        return registroAcaoBO.atualizar(id, dados);
    }

    @DeleteMapping("/registros/{id}")
    public ResponseEntity<Void> excluir(@PathVariable int id) throws SQLException {
        registroAcaoBO.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

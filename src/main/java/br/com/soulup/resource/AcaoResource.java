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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.soulup.bo.AcaoBO;
import br.com.soulup.model.Acao;
import br.com.soulup.model.Categoria;

@RestController
public class AcaoResource {

    private final AcaoBO acaoBO;

    public AcaoResource(AcaoBO acaoBO) {
        this.acaoBO = acaoBO;
    }

    @GetMapping("/categorias")
    public List<Categoria> listarCategorias() throws SQLException {
        return acaoBO.listarCategorias();
    }

    @GetMapping("/acoes")
    public List<Acao> listar(@RequestParam(required = false) Integer categoria) throws SQLException {
        return acaoBO.listar(categoria);
    }

    @GetMapping("/acoes/{id}")
    public Acao buscar(@PathVariable int id) throws SQLException {
        return acaoBO.buscar(id);
    }

    @PostMapping("/acoes")
    public ResponseEntity<Acao> cadastrar(@RequestBody Acao dados) throws SQLException {
        Acao acao = acaoBO.cadastrar(dados);
        return ResponseEntity.created(URI.create("/acoes/" + acao.getId())).body(acao);
    }

    @PutMapping("/acoes/{id}")
    public Acao atualizar(@PathVariable int id, @RequestBody Acao dados) throws SQLException {
        return acaoBO.atualizar(id, dados);
    }

    @DeleteMapping("/acoes/{id}")
    public ResponseEntity<Void> excluir(@PathVariable int id) throws SQLException {
        acaoBO.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

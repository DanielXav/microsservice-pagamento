package com.danielxavier.Pagamentos.controller;

import com.danielxavier.Pagamentos.record.PagamentoRecord;
import com.danielxavier.Pagamentos.service.PagamentoService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService service;

    @GetMapping
    public Page<PagamentoRecord> listar(@PageableDefault(size = 10) Pageable paginacao){
        return service.obterTodos(paginacao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoRecord> detalhar(@PathVariable @NotNull long id){
        PagamentoRecord record = service.obterPorId(id);
        return ResponseEntity.ok(record);
    }

    @PostMapping
    public ResponseEntity<PagamentoRecord> cadastrar(@RequestBody @Valid PagamentoRecord record, UriComponentsBuilder uriBuilder) {
        PagamentoRecord pagamento = service.criarPagamento(record);
        URI endereco = uriBuilder.path("/pagamentos/{id}").buildAndExpand(pagamento.id()).toUri();
        return ResponseEntity.created(endereco).body(pagamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoRecord> atualizar(@PathVariable @NotNull Long id, @RequestBody @Valid PagamentoRecord record) {
        PagamentoRecord atualizado = service.atualizarPagamento(id, record);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PagamentoRecord> remover(@PathVariable @NotNull Long id) {
        service.excluirPagamento(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/confirmar")
    @CircuitBreaker(name = "atualizaPedido", fallbackMethod = "pagamentoAutorizadoComIntegracaoPendente")
    public void confirmarPagamento(@PathVariable @NotNull Long id){
        service.confirmarPagamento(id);
    }

    public void pagamentoAutorizadoComIntegracaoPendente(Long id, Exception e){
        service.alteraStatus(id);
    }

}

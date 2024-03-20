package com.danielxavier.Pagamentos.service;

import com.danielxavier.Pagamentos.mapper.PagamentoMapper;
import com.danielxavier.Pagamentos.model.Pagamento;
import com.danielxavier.Pagamentos.model.Status;
import com.danielxavier.Pagamentos.record.PagamentoRecord;
import com.danielxavier.Pagamentos.repository.PagamentoRepository;
import com.danielxavier.Pagamentos.http.PedidoClient;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository repository;

    @Autowired
    private PedidoClient pedido;

    public Page<PagamentoRecord> obterTodos(Pageable paginacao) {
        return repository
                .findAll(paginacao)
                .map(PagamentoMapper.INSTANCE::toDto);
    }

    public PagamentoRecord obterPorId(Long id) {
        Pagamento pagamento = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        PagamentoRecord dto = PagamentoMapper.INSTANCE.toDto(pagamento);

        var itens = pedido.obterItensDoPedido(pagamento.getPedidoId()).getItens();

        PagamentoRecord dtoNovo = new PagamentoRecord(
                dto.id(),
                dto.valor(),
                dto.nome(),
                dto.numero(),
                dto.expiracao(),
                dto.codigo(),
                dto.status(),
                dto.formaDePagamentoId(),
                dto.pedidoId(),
                itens
        );

        return dtoNovo;
    }

    public PagamentoRecord criarPagamento(PagamentoRecord pagamentoRecord) {
        Pagamento pagamento = PagamentoMapper.INSTANCE.toEntity(pagamentoRecord);
        pagamento.setStatus(Status.CRIADO);
        repository.save(pagamento);
        return PagamentoMapper.INSTANCE.toDto(pagamento);
    }

    public PagamentoRecord atualizarPagamento(Long id, PagamentoRecord pagamentoRecord) {
        Pagamento pagamento = PagamentoMapper.INSTANCE.toEntity(pagamentoRecord);
        pagamento.setId(id);
        pagamento = repository.save(pagamento);
        return PagamentoMapper.INSTANCE.toDto(pagamento);
    }

    public void excluirPagamento(Long id){
        repository.deleteById(id);
    }

    public void confirmarPagamento(Long id){
        Optional<Pagamento> pagamento = repository.findById(id);

        if (!pagamento.isPresent()) {
            throw new EntityNotFoundException();
        }

        pagamento.get().setStatus(Status.CONFIRMADO);
        repository.save(pagamento.get());
        pedido.atualizarPagamento(pagamento.get().getPedidoId());
    }

    public void alteraStatus(Long id) {
        Optional<Pagamento> pagamento = repository.findById(id);

        if (!pagamento.isPresent()) {
            throw new EntityNotFoundException();
        }

        pagamento.get().setStatus(Status.CONFIRMADO_SEM_INTEGRACAO);
        repository.save(pagamento.get());

    }
}

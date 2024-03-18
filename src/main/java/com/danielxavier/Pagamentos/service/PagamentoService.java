package com.danielxavier.Pagamentos.service;

import com.danielxavier.Pagamentos.mapper.PagamentoMapper;
import com.danielxavier.Pagamentos.model.Pagamento;
import com.danielxavier.Pagamentos.model.Status;
import com.danielxavier.Pagamentos.record.PagamentoRecord;
import com.danielxavier.Pagamentos.repository.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository repository;

    @Autowired
    private ModelMapper mapper;

    public Page<PagamentoRecord> obterTodos(Pageable paginacao) {
        return repository
                .findAll(paginacao)
                .map(PagamentoMapper.INSTANCE::toDto);
    }

    public PagamentoRecord obterPorId(Long id) {
        Pagamento pagamento = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return PagamentoMapper.INSTANCE.toDto(pagamento);
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
}

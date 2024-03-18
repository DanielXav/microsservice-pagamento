package com.danielxavier.Pagamentos.repository;

import com.danielxavier.Pagamentos.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}

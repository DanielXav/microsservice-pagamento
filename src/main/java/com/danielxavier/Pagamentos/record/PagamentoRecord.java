package com.danielxavier.Pagamentos.record;

import com.danielxavier.Pagamentos.model.Status;

import java.math.BigDecimal;

public record PagamentoRecord(
        Long id,
        BigDecimal valor,
        String nome,
        String numero,
        String expiracao,
        String codigo,
        Status status,
        Long formaDePagamentoId,
        Long pedidoId
) {
}

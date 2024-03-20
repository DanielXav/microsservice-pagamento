package com.danielxavier.Pagamentos.record;

import com.danielxavier.Pagamentos.model.ItemDoPedido;
import com.danielxavier.Pagamentos.model.Status;

import java.math.BigDecimal;
import java.util.List;

public record PagamentoRecord(
        Long id,
        BigDecimal valor,
        String nome,
        String numero,
        String expiracao,
        String codigo,
        Status status,
        Long formaDePagamentoId,
        Long pedidoId,
        List<ItemDoPedido> itens
) {
}

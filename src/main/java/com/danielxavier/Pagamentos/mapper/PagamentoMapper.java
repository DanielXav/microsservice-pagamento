package com.danielxavier.Pagamentos.mapper;

import com.danielxavier.Pagamentos.model.Pagamento;
import com.danielxavier.Pagamentos.record.PagamentoRecord;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PagamentoMapper {
    PagamentoMapper INSTANCE = Mappers.getMapper(PagamentoMapper.class);

    Pagamento toEntity(PagamentoRecord dto);

    PagamentoRecord toDto(Pagamento entity);
}

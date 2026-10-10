package org.example.bodrn.stocks.domain;

import org.example.bodrn.stocks.api.dto.request.StockCreateRequest;
import org.example.bodrn.stocks.api.dto.request.StockUpdateRequest;
import org.example.bodrn.stocks.api.dto.response.StockResponse;
import org.example.bodrn.stocks.api.dto.response.StockShortResponse;
import org.example.bodrn.stocks.db.StockEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StockMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "photo", ignore = true)
    StockEntity toEntity(StockCreateRequest request);

    StockResponse toResponse(StockEntity entity);

    StockShortResponse toShortResponse(StockEntity entity);

    List<StockShortResponse> toShortResponseList(List<StockEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "photo", ignore = true)
    void update(@MappingTarget StockEntity entity, StockUpdateRequest request);
}
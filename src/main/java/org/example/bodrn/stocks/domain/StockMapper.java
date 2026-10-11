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
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "priceBefore", source = "priceBefore")
    @Mapping(target = "priceAfter", source = "priceAfter")
    @Mapping(target = "validFrom", source = "validFrom")
    @Mapping(target = "validTo", source = "validTo")
    StockEntity toEntity(StockCreateRequest request);

    @Mapping(target = "description", source = "description")
    StockResponse toResponse(StockEntity entity);

    @Mapping(target = "description", source = "description")
    StockShortResponse toShortResponse(StockEntity entity);

    List<StockShortResponse> toShortResponseList(List<StockEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "photo", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "priceBefore", source = "priceBefore")
    @Mapping(target = "priceAfter", source = "priceAfter")
    @Mapping(target = "validFrom", source = "validFrom")
    @Mapping(target = "validTo", source = "validTo")
    void update(@MappingTarget StockEntity entity, StockUpdateRequest request);
}
package org.example.bodrn.product.domain.mapper;

import org.example.bodrn.product.api.dto.request.ProductCreateRequest;
import org.example.bodrn.product.api.dto.request.ProductUpdateRequest;
import org.example.bodrn.product.api.dto.response.ProductResponse;
import org.example.bodrn.product.api.dto.response.ProductShortResponse;
import org.example.bodrn.product.db.ProductEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "photos", ignore = true)
    @Mapping(target = "category", ignore = true)
    ProductEntity toEntity(ProductCreateRequest request);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    ProductResponse toResponse(ProductEntity entity);

    @Mapping(target = "mainPhoto", expression = "java(firstPhoto(entity))")
    ProductShortResponse toShortResponse(ProductEntity entity);

    List<ProductShortResponse> toShortResponseList(List<ProductEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "photos", ignore = true)
    @Mapping(target = "category", ignore = true)
    void update(@MappingTarget ProductEntity entity, ProductUpdateRequest request);

    default String firstPhoto(ProductEntity entity) {
        if (entity.getPhotos() == null || entity.getPhotos().isEmpty()) return null;
        return entity.getPhotos().get(0);
    }
}
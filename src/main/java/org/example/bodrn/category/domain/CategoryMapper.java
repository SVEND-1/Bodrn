package org.example.bodrn.category.domain;

import org.example.bodrn.category.api.dto.request.CategoryCreateRequest;
import org.example.bodrn.category.api.dto.request.CategoryUpdateRequest;
import org.example.bodrn.category.api.dto.response.CategoryResponse;
import org.example.bodrn.category.api.dto.response.CategoryShortResponse;
import org.example.bodrn.category.db.CategoryEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    CategoryEntity toEntity(CategoryCreateRequest request);

    CategoryResponse toResponse(CategoryEntity entity);

    CategoryShortResponse toShortResponse(CategoryEntity entity);

    List<CategoryShortResponse> toShortResponseList(List<CategoryEntity> entities);

    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget CategoryEntity entity, CategoryUpdateRequest request);
}
package org.example.bodrn.company.domain;

import org.example.bodrn.company.api.dto.request.CompanyCreateRequest;
import org.example.bodrn.company.api.dto.request.CompanyUpdateRequest;
import org.example.bodrn.company.api.dto.response.CompanyResponse;
import org.example.bodrn.company.db.CompanyEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CompanyEntity toEntity(CompanyCreateRequest request);

    CompanyResponse toResponse(CompanyEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void update(@MappingTarget CompanyEntity entity, CompanyUpdateRequest request);
}
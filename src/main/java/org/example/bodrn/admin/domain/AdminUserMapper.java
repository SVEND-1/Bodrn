package org.example.bodrn.admin.domain;

import org.example.bodrn.admin.api.dto.request.AdminUserCreateRequest;
import org.example.bodrn.admin.api.dto.request.AdminUserUpdateRequest;
import org.example.bodrn.admin.api.dto.response.AdminUserResponse;
import org.example.bodrn.admin.db.AdminUserEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminUserMapper {

    /** Создание: id/пароль/аудит проставим в сервисе. */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AdminUserEntity toEntity(AdminUserCreateRequest request);

    AdminUserResponse toResponse(AdminUserEntity entity);

    List<AdminUserResponse> toResponseList(List<AdminUserEntity> entities);

    /** Обновление: пароль и id не трогаем, всё остальное — по null-ignore. */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "lastLoginAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void update(@MappingTarget AdminUserEntity entity, AdminUserUpdateRequest request);
}
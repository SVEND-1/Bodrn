package org.example.bodrn.reviews.domain;

import org.example.bodrn.reviews.api.dto.request.ReviewCreateRequest;
import org.example.bodrn.reviews.api.dto.response.ReviewResponse;
import org.example.bodrn.reviews.db.ReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ReviewEntity toEntity(ReviewCreateRequest request);

    ReviewResponse toResponse(ReviewEntity entity);

    List<ReviewResponse> toResponseList(List<ReviewEntity> entities);
}
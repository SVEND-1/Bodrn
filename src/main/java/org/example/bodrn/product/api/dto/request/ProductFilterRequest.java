package org.example.bodrn.product.api.dto.request;

import org.springframework.web.bind.annotation.BindParam;

public record ProductFilterRequest(

        @BindParam("categoryId")
        Long categoryId,

        @BindParam("search")
        String search
) {}
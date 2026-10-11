package org.example.bodrn.product.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bodrn.category.db.CategoryEntity;
import org.example.bodrn.category.db.CategoryRepository;
import org.example.bodrn.comm.PhotoService;
import org.example.bodrn.product.api.dto.request.ProductCreateRequest;
import org.example.bodrn.product.api.dto.request.ProductFilterRequest;
import org.example.bodrn.product.api.dto.request.ProductUpdateRequest;
import org.example.bodrn.product.api.dto.response.ProductResponse;
import org.example.bodrn.product.api.dto.response.ProductShortResponse;
import org.example.bodrn.product.db.ProductEntity;
import org.example.bodrn.product.db.ProductRepository;
import org.example.bodrn.product.domain.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final PhotoService photoService;

    public ProductResponse create(ProductCreateRequest request) {
        CategoryEntity category = findCategory(request.categoryId());

        ProductEntity entity = productMapper.toEntity(request);
        entity.setCategory(category);
        entity.setPhotos(new ArrayList<>());

        if (request.photos() != null && !request.photos().isEmpty()) {
            entity.setPhotos(uploadPhotos(request.photos()));
        }

        ProductEntity saved = productRepository.save(entity);
        return productMapper.toResponse(saved);
    }

    @Transactional
    public ProductResponse update(Long id, ProductUpdateRequest request) {
        ProductEntity entity = findProduct(id);

        if (request.categoryId() != null) {
            entity.setCategory(findCategory(request.categoryId()));
        }

        productMapper.update(entity, request);

        List<String> current = entity.getPhotos() == null
                ? new ArrayList<>()
                : new ArrayList<>(entity.getPhotos());
        List<String> keep = request.keepPhotos() == null ? List.of() : request.keepPhotos();

        for (String url : current) {
            if (!keep.contains(url)) {
                photoService.delete(url);
            }
        }

        List<String> result = new ArrayList<>(keep);
        if (request.photos() != null && !request.photos().isEmpty()) {
            result.addAll(uploadPhotos(request.photos()));
        }

        entity.setPhotos(result);
        ProductEntity saved = productRepository.save(entity);
        return productMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ProductShortResponse> findAll(ProductFilterRequest filter) {
        String search = filter.search() == null ? "" : filter.search().trim();
        List<ProductEntity> list = productRepository.findAllFiltered(filter.categoryId(), search);
        return productMapper.toShortResponseList(list);
    }

    @Transactional(readOnly = true)
    public ProductResponse findOne(Long id) {
        return productMapper.toResponse(findProduct(id));
    }

    @Transactional
    public void delete(Long id) {
        ProductEntity entity = findProduct(id);
        if (entity.getPhotos() != null) {
            entity.getPhotos().forEach(photoService::delete);
        }
        productRepository.delete(entity);
    }

    // === helpers ===

    private ProductEntity findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Продукт не найден: " + id));
    }

    private CategoryEntity findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Категория не найдена: " + id));
    }

    private List<String> uploadPhotos(List<MultipartFile> files) {
        List<String> urls = new ArrayList<>(files.size());
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) continue;
            photoService.compress(file);
            urls.add(photoService.save(file));
        }
        return urls;
    }
}
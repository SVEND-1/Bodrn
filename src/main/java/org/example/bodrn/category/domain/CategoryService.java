package org.example.bodrn.category.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bodrn.category.api.dto.request.CategoryCreateRequest;
import org.example.bodrn.category.api.dto.request.CategoryUpdateRequest;
import org.example.bodrn.category.api.dto.response.CategoryResponse;
import org.example.bodrn.category.api.dto.response.CategoryShortResponse;
import org.example.bodrn.category.db.CategoryEntity;
import org.example.bodrn.category.db.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryResponse create(CategoryCreateRequest request) {
        String name = request.name().trim();

        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new IllegalArgumentException("Категория с именем '" + name + "' уже существует");
        }

        CategoryEntity entity = categoryMapper.toEntity(request);
        entity.setName(name);
        CategoryEntity saved = categoryRepository.save(entity);
        return categoryMapper.toResponse(saved);
    }

    public CategoryResponse update(Long id, CategoryUpdateRequest request) {
        CategoryEntity entity = findCategory(id);

        if (request.name() != null && !request.name().isBlank()) {
            String newName = request.name().trim();
            if (!newName.equalsIgnoreCase(entity.getName())
                    && categoryRepository.existsByNameIgnoreCase(newName)) {
                throw new IllegalArgumentException("Категория с именем '" + newName + "' уже существует");
            }
            entity.setName(newName);
        }

        CategoryEntity saved = categoryRepository.save(entity);
        return categoryMapper.toResponse(saved);
    }

    public List<CategoryShortResponse> findAll() {
        List<CategoryEntity> list = categoryRepository.findAll();
        return categoryMapper.toShortResponseList(list);
    }

    public CategoryResponse findOne(Long id) {
        return categoryMapper.toResponse(findCategory(id));
    }

    public void delete(Long id) {
        CategoryEntity entity = findCategory(id);
        categoryRepository.delete(entity);
    }

    private CategoryEntity findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Категория не найдена: " + id));
    }
}
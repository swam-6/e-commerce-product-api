package com.ecommerce.api.service;

import com.ecommerce.api.dto.request.CategoryRequest;
import com.ecommerce.api.dto.response.CategoryResponse;
import com.ecommerce.api.entity.Category;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // ─── CREATE ───────────────────────────────────────────

    public CategoryResponse createCategory(CategoryRequest request) {

        // Check duplicate name
        if (categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException(
                    "Category already exists with name: " + request.getName()
            );
        }

        // Create new category
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        // Save to DB
        Category saved = categoryRepository.save(category);

        // Return response
        return mapToResponse(saved);
    }

    // ─── GET ALL ──────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ─── GET BY ID ────────────────────────────────────────

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", id)
                );
        return mapToResponse(category);
    }

    // ─── UPDATE ───────────────────────────────────────────

    public CategoryResponse updateCategory(Long id, CategoryRequest request) {

        // Find existing category
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", id)
                );

        // Check if new name conflicts with another category
        if (!category.getName().equals(request.getName()) &&
                categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException(
                    "Category already exists with name: " + request.getName()
            );
        }

        // Update fields
        category.setName(request.getName());
        category.setDescription(request.getDescription());

        // Save updated category
        Category updated = categoryRepository.save(category);

        return mapToResponse(updated);
    }

    // ─── DELETE ───────────────────────────────────────────

    public void deleteCategory(Long id) {

        // Check if category exists
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category", id)
                );

        // Delete category
        categoryRepository.delete(category);
    }

    // ─── HELPER METHOD ────────────────────────────────────

    private CategoryResponse mapToResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());
        return response;
    }
}
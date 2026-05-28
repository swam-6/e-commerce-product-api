package com.ecommerce.api.controller;

import com.ecommerce.api.dto.request.CategoryRequest;
import com.ecommerce.api.dto.response.CategoryResponse;
import com.ecommerce.api.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // ─── CREATE CATEGORY (ADMIN ONLY) ─────────────────────

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") //admin only privilege
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryRequest request) {
        try {
            CategoryResponse response =
                    categoryService.createCategory(request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // ─── GET ALL CATEGORIES (PUBLIC) ──────────────────────
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories =
                categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // ─── GET CATEGORY BY ID (PUBLIC) ──────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        try {
            CategoryResponse response =
                    categoryService.getCategoryById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    // ─── UPDATE CATEGORY (ADMIN ONLY) ─────────────────────

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        try {
            CategoryResponse response =
                    categoryService.updateCategory(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // ─── DELETE CATEGORY (ADMIN ONLY) ─────────────────────

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok("Category deleted successfully");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
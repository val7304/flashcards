package com.example.flashcards.controller;

import com.example.flashcards.dto.CategoryDTO;
import com.example.flashcards.entity.Category;
import com.example.flashcards.mapper.CategoryMapper;
import com.example.flashcards.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> getAll() {
        return categoryService.getAllCategories()
                .stream()
                .map(CategoryMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(CategoryMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<CategoryDTO>> searchByName(
            @RequestParam("name") String name) {
        return ResponseEntity.ok(categoryService.searchByName(name));
    }

    @PostMapping
    public CategoryDTO create(@RequestBody CategoryDTO dto) {
        Category saved = categoryService.createCategory(CategoryMapper.toEntity(dto));
        return CategoryMapper.toDTO(saved);
    }

    @PutMapping("/{id}")
    public CategoryDTO update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        Category updated = categoryService.updateCategory(id, CategoryMapper.toEntity(dto));
        return CategoryMapper.toDTO(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}

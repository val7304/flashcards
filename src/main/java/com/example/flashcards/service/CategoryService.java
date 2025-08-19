package com.example.flashcards.service;

import com.example.flashcards.entity.Category;
import com.example.flashcards.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.flashcards.dto.CategoryDTO;
import com.example.flashcards.mapper.CategoryMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<CategoryDTO> searchByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(CategoryMapper::toDTO)
                .toList();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category category) {
        return categoryRepository.findById(id).map(c -> {
            c.setName(category.getName());
            return categoryRepository.save(c);
        }).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}

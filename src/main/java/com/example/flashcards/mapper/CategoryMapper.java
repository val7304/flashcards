package com.example.flashcards.mapper;

import com.example.flashcards.dto.CategoryDTO;
import com.example.flashcards.entity.Category;

public class CategoryMapper {

    public static CategoryDTO toDTO(Category category) {
        if (category == null) return null;
        return new CategoryDTO(category.getId(), category.getName());
    }

    public static Category toEntity(CategoryDTO dto) {
        if (dto == null) return null;
        return new Category(dto.getId(), dto.getName(), null);
    }
}

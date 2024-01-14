package me.shovelog.category.service;

import lombok.RequiredArgsConstructor;
import me.shovelog.category.repository.CategoryRepository;
import me.shovelog.category.service.response.CategoryResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponse::of)
                .collect(Collectors.toList());
    }
}

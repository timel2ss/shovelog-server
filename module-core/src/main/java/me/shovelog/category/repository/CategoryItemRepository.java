package me.shovelog.category.repository;

import me.shovelog.category.domain.CategoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryItemRepository extends JpaRepository<CategoryItem, Long> {
    Optional<CategoryItem> findByItemName(String itemName);
}

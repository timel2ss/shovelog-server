package me.shovelog.category.repository;

import me.shovelog.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c join fetch c.categoryItems")
    List<Category> findAllFetchJoin();
}

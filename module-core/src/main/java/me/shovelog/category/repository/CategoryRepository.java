package me.shovelog.category.repository;

import me.shovelog.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c join fetch c.categoryItems ci order by c.categoryOrder, ci.itemOrder")
    List<Category> findAllFetchJoin();

    Optional<Category> findByName(String name);
}

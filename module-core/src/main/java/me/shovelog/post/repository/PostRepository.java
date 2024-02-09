package me.shovelog.post.repository;

import me.shovelog.category.domain.CategoryItem;
import me.shovelog.post.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByTitle(String title);

    @Query("select p from Post p where p.status = me.shovelog.post.domain.PostStatus.PUBLIC")
    List<Post> findAllPublic(Pageable pageable);

    @Query("select p from Post p join p.categoryItem ci where ci.id = :#{#categoryItem.id} and p.status = me.shovelog.post.domain.PostStatus.PUBLIC")
    List<Post> findByCategoryItemPublic(@Param("categoryItem") CategoryItem categoryItem, Pageable pageable);

    @Query("select p from Post p join p.categoryItem ci join ci.category c where c.id = :categoryId and p.status = me.shovelog.post.domain.PostStatus.PUBLIC")
    List<Post> findByCategoryPublic(@Param("categoryId") Long categoryId, Pageable pageable);
}

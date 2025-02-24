package org.wefresh.wefresh_server.bookmark.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.wefresh.wefresh_server.bookmark.domain.Bookmark;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    void deleteByUserId(Long userId);

    @Query(value = """
        SELECT b FROM Bookmark b
        LEFT JOIN FETCH b.recipe
        LEFT JOIN FETCH b.todayRecipe
        WHERE b.user.id = :userId
        ORDER BY b.createdAt DESC
    """, countQuery = """
        SELECT COUNT(b) FROM Bookmark b
        WHERE b.user.id = :userId
    """)
    List<Bookmark> findByUserId(Long userId, Pageable pageable);

    @Query(value = """
        SELECT b FROM Bookmark b
        LEFT JOIN FETCH b.recipe
        LEFT JOIN FETCH b.todayRecipe
        WHERE b.user.id = :userId
        ORDER BY b.createdAt DESC
    """, countQuery = """
        SELECT COUNT(b) FROM Bookmark b
        WHERE b.user.id = :userId
    """)
    Page<Bookmark> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Query("""
        SELECT b FROM Bookmark b
        LEFT JOIN FETCH b.recipe r
        LEFT JOIN FETCH b.todayRecipe t
        WHERE b.id = :bookmarkId
    """)
    Optional<Bookmark> findById(Long bookmarkId);
}

package org.wefresh.wefresh_server.bookmark.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.wefresh.wefresh_server.bookmark.domain.Bookmark;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    void deleteByUserId(Long userId);

    List<Bookmark> findByUserId(Long userId);

    Page<Bookmark> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}

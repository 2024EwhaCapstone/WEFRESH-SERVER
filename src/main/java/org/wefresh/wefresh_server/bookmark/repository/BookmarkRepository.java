package org.wefresh.wefresh_server.bookmark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wefresh.wefresh_server.bookmark.domain.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    void deleteByUserId(Long userId);
}

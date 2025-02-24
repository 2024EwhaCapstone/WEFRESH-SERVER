package org.wefresh.wefresh_server.bookmark.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.wefresh.wefresh_server.bookmark.domain.Bookmark;
import org.wefresh.wefresh_server.bookmark.repository.BookmarkRepository;
import org.wefresh.wefresh_server.common.exception.BusinessException;
import org.wefresh.wefresh_server.common.exception.code.BookmarkErrorCode;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookmarkRetriever {

    private final BookmarkRepository bookmarkRepository;

    public List<Bookmark> findByUserId(
            final Long userId,
            Pageable pageable
    ) {
        return bookmarkRepository.findByUserId(userId, pageable);
    }

    public Page<Bookmark> findByUserIdOrderByCreatedAtDesc(
            final Long userId,
            final Pageable pageable
    ) {
        return bookmarkRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public Bookmark findById(final Long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new BusinessException(BookmarkErrorCode.BOOKMARK_NOT_FOUND));
    }
}

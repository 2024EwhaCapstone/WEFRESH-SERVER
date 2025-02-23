package org.wefresh.wefresh_server.bookmark.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wefresh.wefresh_server.bookmark.repository.BookmarkRepository;

@Component
@RequiredArgsConstructor
public class BookmarkRemover {

    private final BookmarkRepository bookmarkRepository;

    public void deleteByUserId(Long userId) {
        bookmarkRepository.deleteByUserId(userId);
    }
}

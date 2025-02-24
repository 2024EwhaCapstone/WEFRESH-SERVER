package org.wefresh.wefresh_server.bookmark.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wefresh.wefresh_server.bookmark.repository.BookmarkRepository;

@Component
@RequiredArgsConstructor
public class BookmarkRemover {

    private final BookmarkRepository bookmarkRepository;

    public void deleteByUserId(final Long userId) {
        bookmarkRepository.deleteByUserId(userId);
    }

    public void deleteById(final Long id) {
        bookmarkRepository.deleteById(id);
    }
}

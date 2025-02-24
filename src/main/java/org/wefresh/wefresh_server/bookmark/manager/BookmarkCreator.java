package org.wefresh.wefresh_server.bookmark.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.wefresh.wefresh_server.bookmark.domain.Bookmark;
import org.wefresh.wefresh_server.bookmark.repository.BookmarkRepository;

@Component
@RequiredArgsConstructor
public class BookmarkCreator {

    private final BookmarkRepository bookmarkRepository;

    public void save(final Bookmark bookmark) {
        bookmarkRepository.save(bookmark);
    }
}

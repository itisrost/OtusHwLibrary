package ru.otus.homework.libraryJdbc.dao.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BookRelation {
    private final long bookId;
    private final long relatedId;
}

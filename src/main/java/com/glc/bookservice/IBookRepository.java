package com.glc.bookservice;

import java.util.Collection;

public interface IBookRepository<T> {
    public void save(T t);

    public Collection<T> getAllBooks();
    public Book getABookById(int id);
    public Collection<Book> deleteABookById(int id);
    public Book updateSpecificBook(Book myBook);
}

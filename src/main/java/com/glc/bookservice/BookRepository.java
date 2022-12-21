package com.glc.bookservice;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class BookRepository implements IBookRepository<Book>{
    private Map<Integer, Book> repository;

    public BookRepository() {
        this.repository = new HashMap<>();
    }

    @Override
    public void save(Book book){
        repository.put(book.getId(), book);
    }

    @Override
    public Collection<Book> getAllBooks(){
        return repository.values();
    }
    @Override
    public Book getABookById(int id) {
        return repository.get(id);
    }
    @Override
    public Collection<Book> deleteABookById(int id) {
        repository.remove(id);
        return repository.values();
    }
    @Override
    public Book updateSpecificBook(Book myBook){
        Book specificBook = repository.get(myBook.getId());
        if (specificBook != null){
            repository.get(myBook.getId());
        }
        return repository.put(myBook.getId(), myBook);
    }
    
}

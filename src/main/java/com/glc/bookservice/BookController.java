package com.glc.bookservice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/books")  // Any address like https://localhost:8080/books
@RabbitListener(queues = "book-list")
public class BookController {
    private final BookRepository repository;

    @Autowired
    private Queue queue;
    @Autowired
    private RabbitTemplate temp;

    public BookController(BookRepository repository){
        this.repository = repository;
    }

    @PostMapping("")  // (POST) https://localhost:8080/books
    public void createBook(@RequestBody Book book) {
        this.repository.save(book);
    }

    @GetMapping("/all") // (GET) https://localhost:8080/books/all
    public Collection<Book> getAllBooks(){
        return this.repository.getAllBooks();
    }
    @GetMapping("/{id}") // (GET) https://localhost:8080/books/all/{id}
    public Book getABookById(@PathVariable int id){
        return this.repository.getABookById(id);
    }
    @DeleteMapping("/{id}") // (GET) https://localhost:8080/books/all/{id}
    public Collection<Book> deleteABookById(@PathVariable int id){
        return this.repository.deleteABookById(id);
    }

    @PutMapping("")
    public Book updateSpecificBook(@RequestBody Book myBook){
        return this.repository.updateSpecificBook(myBook);
    }

    @RabbitHandler
    public void receiver (String msg) throws Exception {
        ObjectMapper objMap = new ObjectMapper();
        List<Book> myBook = objMap.readValue(msg, new TypeReference<List<Book>>(){});
        myBook.forEach((book) -> repository.save(book));
        myBook.forEach((book) -> System.out.println(book.getTitle()));
    }

    @PostMapping("/booklist") // http://localhost:8080/books/booklist
    public void sender() throws Exception{
        ObjectMapper objMap = new ObjectMapper();
        Book book1 = new Book(3,"Code With Mosh","Mosh", 1998, 2220);
        Book book2 = new Book(4,"Code With Jeff","Dr. Jeff", 1978, 210);
        List<Book> myBooks = new ArrayList<>();
        myBooks.add(book1);
        myBooks.add(book2);
        temp.convertAndSend(queue.getName(), objMap.writeValueAsString(myBooks));
    }


}

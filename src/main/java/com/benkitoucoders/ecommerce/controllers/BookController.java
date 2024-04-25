package com.benkitoucoders.ecommerce.controllers;

import com.benkitoucoders.ecommerce.entities.Book;
import com.benkitoucoders.ecommerce.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/books/all")
    public List<Book> getBooks(){
        return bookService.getBooks();
    }
}

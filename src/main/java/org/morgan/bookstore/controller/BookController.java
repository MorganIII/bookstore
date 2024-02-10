package org.morgan.bookstore.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.response.BookDetailsResponse;
import org.morgan.bookstore.response.BookListingResponse;
import org.morgan.bookstore.request.BookRequest;
import org.morgan.bookstore.response.BookResponse;
import org.morgan.bookstore.service.BookService;
import org.morgan.bookstore.service.MultipartFileService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    private final MultipartFileService multipartFileService;
    @PostMapping(value = "",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public String addBook(@RequestPart("book")BookRequest request,
                          @RequestPart("cover")MultipartFile bookCover,
                          @RequestPart("thumbnail") MultipartFile bookThumbnail) {

        return bookService.addBook(request,bookCover,bookThumbnail);
    }

    @PutMapping(value = {"/{id}"},consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public String updateBook(@RequestPart("book")BookRequest request,
                             @RequestPart(value = "cover", required = false)MultipartFile bookCover,
                             @RequestPart(value = "thumbnail", required = false) MultipartFile bookThumbnail,
                             @PathVariable(name = "id") Integer id) {
        return bookService.updateBook(id, request,bookCover,bookThumbnail);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable(name = "id") Integer id) {
        bookService.deleteBook(id);
    }

    @GetMapping("/{id}")
    public BookResponse getBook(@PathVariable(name = "id") Integer id) {
        return bookService.getBookResponse(id);
    }

    @GetMapping("/details/{id}")
    public BookDetailsResponse getBookDetails(@PathVariable(name = "id") Integer bookId) {
        return bookService.getBookDetails(bookId);
    }

    @GetMapping("/category")
    public Page<BookListingResponse> getBooksBySectionAndCategory(@RequestParam(value = "section",required = false) String sectionName,
                                                                  @RequestParam(value = "category",required = false) String categoryName,
                                                                  @RequestParam(value = "page")@PositiveOrZero Integer pageNumber,
                                                                  @RequestParam(value = "size")@Positive() Integer pageSize,
                                                                  @RequestParam(value = "orderby",required = false) String sortBy) {
        System.out.println("sectionName = " + sectionName + ", categoryName = " + categoryName + ", pageNumber = " + pageNumber + ", pageSize = " + pageSize + ", sortBy = " + sortBy);
        return bookService.getBooksBySectionAndCategory(sectionName, categoryName, pageNumber, pageSize, sortBy);
    }

    @GetMapping("/title")
    public Page<BookListingResponse> getBooksByTitle(@RequestParam(name = "title") String title,
                                                     @RequestParam(value = "page")@PositiveOrZero Integer pageNumber,
                                                     @RequestParam(value = "size")@Positive() Integer pageSize){
        return bookService.getBooksByTitle(title,pageNumber,pageSize);
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> getImage(@RequestParam("path") String imagePath) {
        byte[] image = multipartFileService.downloadImage(imagePath);
        MediaType mediaType = multipartFileService.getContentType(imagePath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return new ResponseEntity<>(image,headers,HttpStatus.OK);
    }
}

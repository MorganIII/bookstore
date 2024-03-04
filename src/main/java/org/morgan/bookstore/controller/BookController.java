package org.morgan.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Book", description = "Book API")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    private final MultipartFileService multipartFileService;

    @Operation(summary = "Add a new book", description = """
                Add a new book to the system
            """)
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Book added successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "409", description = "Book already exists")
            }
    )
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public String addBook(@RequestPart("book") @Valid BookRequest request,
                          @RequestPart("cover")MultipartFile bookCover,
                          @RequestPart("thumbnail") MultipartFile bookThumbnail) {

        return bookService.addBook(request,bookCover,bookThumbnail);
    }

    @Operation(summary = "Update a book", description = """
                Update a book in the system
            """)
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Book updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Book not found")
            }
    )
    @PutMapping(value = {"/{id}"},consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public String updateBook(@RequestPart("book") @Valid BookRequest request,
                             @RequestPart(value = "cover", required = false)MultipartFile bookCover,
                             @RequestPart(value = "thumbnail", required = false) MultipartFile bookThumbnail,
                             @PathVariable(name = "id") @PositiveOrZero Integer id) {
        return bookService.updateBook(id, request,bookCover,bookThumbnail);
    }


    @Operation(summary = "Delete a book", description = """
                Delete a book from the system
            """)
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Book not found")
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable(name = "id") @PositiveOrZero Integer id) {
        bookService.deleteBook(id);
    }

    @Operation(summary = "Get a book", description = """
                Get a book from the system
            """)
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Book retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Book not found")
            }
    )
    @GetMapping("/{id}")
    public BookResponse getBook(@PathVariable(name = "id") Integer id) {
        return bookService.getBookResponse(id);
    }

    @Operation(summary = "Get book details", description = """
                Get book details from the system
            """)
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Book details retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Book not found")
            }
    )
    @GetMapping("/details/{id}")
    public BookDetailsResponse getBookDetails(@PathVariable(name = "id") Integer bookId) {
        return bookService.getBookDetails(bookId);
    }

    @Operation(summary = "Get books by section and category", description = """
                Get books by section and category from the system
            """)
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
            }
    )
    @GetMapping("/category")
    public Page<BookListingResponse> getBooksBySectionAndCategory(@RequestParam(value = "section",required = false) String sectionName,
                                                                  @RequestParam(value = "category",required = false) String categoryName,
                                                                  @RequestParam(value = "page")@PositiveOrZero Integer pageNumber,
                                                                  @RequestParam(value = "size")@Positive() Integer pageSize,
                                                                  @RequestParam(value = "orderby",required = false) String sortBy) {
        return bookService.getBooksBySectionAndCategory(sectionName, categoryName, pageNumber, pageSize, sortBy);
    }

    @Operation(summary = "Get books by title", description = """
                Get books by title from the system
            """)
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
            }
    )
    @GetMapping("/title")
    public Page<BookListingResponse> getBooksByTitle(@RequestParam(name = "title") String title,
                                                     @RequestParam(value = "page")@PositiveOrZero Integer pageNumber,
                                                     @RequestParam(value = "size")@Positive() Integer pageSize){
        return bookService.getBooksByTitle(title,pageNumber,pageSize);
    }

    @Operation(summary = "Get image", description = """
                Get image from the system
            """)
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Image retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Image not found")
            }
    )
    @GetMapping("/image")
    public ResponseEntity<byte[]> getImage(@RequestParam("path") String imagePath) {
        byte[] image = multipartFileService.downloadImage(imagePath);
        MediaType mediaType = multipartFileService.getContentType(imagePath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return new ResponseEntity<>(image,headers,HttpStatus.OK);
    }
}

package org.morgan.bookstore.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.response.BookDetailsResponse;
import org.morgan.bookstore.response.BookListingResponse;
import org.morgan.bookstore.request.BookRequest;
import org.morgan.bookstore.response.BookResponse;
import org.morgan.bookstore.model.Book;
import org.morgan.bookstore.model.Category;
import org.morgan.bookstore.model.Section;
import org.morgan.bookstore.enums.ImageType;
import org.morgan.bookstore.enums.SortBy;
import org.morgan.bookstore.exception.ImageNotValidException;
import org.morgan.bookstore.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final SectionService sectionService;
    private final CategoryService categoryService;
    private final MultipartFileService multipartFileService;



    public String addBook(BookRequest request, MultipartFile bookCover, MultipartFile bookThumbnail) {
        String title = request.getTitle();
        String sectionName = request.getSection();
        String categoryName = request.getCategory();
        validateBookDuplicate(title);
        Section section = sectionService.getSection(sectionName);
        Category category = categoryService.getCategoryByNameAndSectionName(categoryName,sectionName);
        if(bookCover.isEmpty() || bookThumbnail.isEmpty()) {
            throw new ImageNotValidException("book cover image and book thumbnail image should not be null");
        }
        String bookCoverName = multipartFileService.uploadImage(bookCover, ImageType.COVER);
        String bookThumbnailName = multipartFileService.uploadImage(bookThumbnail, ImageType.THUMBNAIL);
        return buildBook(request, title, section, category, bookCoverName, bookThumbnailName);
    }
    public String updateBook(Integer bookId,BookRequest request, MultipartFile bookCover, MultipartFile bookThumbnail) {
        Book book = getBookById(bookId);
        String title = request.getTitle();
        if(!title.equals(book.getTitle())) {
            validateBookDuplicate(title);
        }

        String sectionName = request.getSection();
        String categoryName = request.getCategory();
        Section section = book.getSection();
        Category category = book.getCategory();
        if(!section.getName().equals(sectionName) || !category.getName().equals(categoryName)) {
            section = sectionService.getSection(sectionName);
            category = categoryService.getCategoryByNameAndSectionName(categoryName,sectionName);
        }
        String bookCoverName = book.getBookCover();
        String bookThumbnailName = book.getBookThumbnail();
        if(!bookCover.isEmpty()) {
            multipartFileService.deleteImage(book.getBookCover());
            bookCoverName = multipartFileService.uploadImage(bookCover,ImageType.COVER);
        }
        if(!bookThumbnail.isEmpty()) {
            multipartFileService.deleteImage(book.getBookCover());
            bookThumbnailName = multipartFileService.uploadImage(bookCover,ImageType.COVER);
        }
        return buildBook(request, title, section, category, bookCoverName, bookThumbnailName);
    }

    public void deleteBook(Integer bookId) {
        Book book = getBookById(bookId);
        multipartFileService.deleteImage(book.getBookCover());
        multipartFileService.deleteImage(book.getBookThumbnail());
        bookRepository.deleteById(bookId);
    }



    private String buildBook(BookRequest request, String title, Section section, Category category, String bookCoverName, String bookThumbnailName) {
        Book newBook = Book.builder()
                .title(title)
                .actualPrice(request.getActualPrice())
                .discountPrice(request.getDiscountPrice())
                .numberOfPages(request.getNumberOfPages())
                .copiesInStock(request.getBooksInStock() == null ? 1: request.getBooksInStock())
                .copiesToBePrinted(request.getBooksToBePrinted())
                .description(request.getDescription())
                .soldCopies(request.getSoldBooks())
                .bookCover(bookCoverName)
                .bookThumbnail(bookThumbnailName)
                .section(section)
                .category(category)
                .build();
        bookRepository.save(newBook);
        return title;
    }

    public Book getBookById(Integer bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        return bookOptional.orElseThrow(()->new EntityNotFoundException(String.format("The book with id %s is not found", bookId)));
    }

    public BookResponse getBookResponse(Integer bookId) {
        Optional<BookResponse> bookOptional = bookRepository.findBookResponseById(bookId);
        return bookOptional.orElseThrow(()->new EntityNotFoundException(String.format("The book with id %s is not found", bookId)));
    }


    public Book getBookByTitle(String bookTitle) {
        Optional<Book> bookOptional = bookRepository.findBookByTitle(bookTitle);
        return bookOptional.orElseThrow(()->new EntityNotFoundException(String.format("The book with title %s is not found", bookTitle)));
    }

    private void validateBookDuplicate(String bookTitle) {
        Optional<Book> optionalBook = bookRepository.findBookByTitle(bookTitle);
        if(optionalBook.isPresent()) {
            throw new EntityExistsException(String.format("Book with title '%s' is already in the system.", bookTitle));
        }
    }

    public Page<BookListingResponse> getBooksBySectionAndCategory(String sectionName, String categoryName, Integer pageNumber, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        if(SortBy.convert(sortBy) == SortBy.DATE) {
            return bookRepository.filterBooks(sectionName,categoryName, SortBy.DATE.name(),pageable);
        } else {
            return bookRepository.filterBooks(sectionName,categoryName,SortBy.POPULARITY.name(), pageable);
        }
    }


    public BookDetailsResponse getBookDetails(Integer bookId) {
        Book book = getBookById(bookId);
        BookListingResponse listingDTO = BookListingResponse.builder()
                .id(bookId)
                .title(book.getTitle())
                .actualPrice(book.getActualPrice())
                .discountPrice(book.getDiscountPrice())
                .bookThumbnail(book.getBookThumbnail())
                .build();
        return BookDetailsResponse.builder()
                .bookListingResponse(listingDTO)
                .description(book.getDescription())
                .numberOfPages(book.getNumberOfPages())
                .coverImage(book.getBookCover())
                .inStock(book.getCopiesInStock() != null && book.getCopiesInStock() <= 0)
                .build();
    }

    public Page<BookListingResponse> getBooksByTitle(String title, @PositiveOrZero Integer pageNumber, @Positive() Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return bookRepository.findBooksByTitle(title, pageable);
    }
}

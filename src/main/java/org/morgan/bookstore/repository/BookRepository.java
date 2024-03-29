package org.morgan.bookstore.repository;


import jakarta.transaction.Transactional;
import org.morgan.bookstore.response.BookListingResponse;
import org.morgan.bookstore.response.BookResponse;
import org.morgan.bookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {

    @Query(value = """
           select new org.morgan.bookstore.response.BookResponse(b.id,b.title,b.actualPrice,b.discountPrice,b.bookThumbnail,
           b.description, b.numberOfPages,b.bookCover, b.copiesInStock,b.soldCopies,b.creationTime,b.lastUpdatedTime,
           b.category.name,b.section.name) from Book b
           where b.id=:id
           """)
    Optional<BookResponse> findBookResponseById(Integer id);
    Optional<Book> findBookByTitle(String title);

    @Query(value = """
            select new org.morgan.bookstore.response.BookListingResponse(b.id, b.title, b.actualPrice, b.discountPrice, b.bookThumbnail)
            from Book b where (b.section.name=:sectionName or :sectionName is null ) and
            (b.category.name=:categoryName or :categoryName is null)
            order by
            case when :orderBy = 'POPULARITY' then b.soldCopies
                else b.id
            end desc
            """)
    Page<BookListingResponse> filterBooks(String sectionName, String categoryName, String orderBy, Pageable pageable);

    @Query(value = """ 
                SELECT new org.morgan.bookstore.response.BookListingResponse(b.id, b.title, b.actualPrice,b.discountPrice,b.bookThumbnail)
                FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))
          """)
    Page<BookListingResponse> findBooksByTitle(String title, Pageable pageable);




    @Query(value = """
            select b.copiesInStock from Book b where b.id=:id
            """)
    Integer getCopiesInStockById(Integer id);

    @Modifying
    @Query(value = """
            update Book b set b.copiesInStock = b.copiesInStock + :quantity where b.id=:bookId
    """)
    @Transactional
    void updateBookCopiesInStock(@Param("bookId") Integer bookId,
                                 @Param("quantity") Integer quantity);




    @Modifying
    @Query(value = """
            update Book b set b.soldCopies = b.soldCopies + :quantity where b.id=:bookId
    """)
    void updateSoldCopies(Integer bookId, Integer quantity);




}

package org.morgan.bookstore.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Builder
@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Book extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "book_title",unique = true, nullable = false)
    private String title;

    @PositiveOrZero(message = "The actual price should not be a negative value")
    @Column(name = "actual_price",nullable = false)
    private Double actualPrice;

    @PositiveOrZero(message = "The discount price should not be a negative value")
    @Column(name = "discount_price",nullable = false)
    private Double discountPrice;

    @Column(name = "number_of_pages",nullable = false)
    @Positive(message = "Number of pages should be greater than zero")
    private Integer numberOfPages;

    @PositiveOrZero(message = "Number of books in the stock should not be a negative value")
    @Column(name = "copies_in_stock")
    private Integer copiesInStock;

    @PositiveOrZero(message = "Number of books to be printed should not be a negative value")
    @Column(name = "books_to_be_printed")
    private Integer copiesToBePrinted;


    @Column(name = "book_description", length = 5000)
    private String description;

    @PositiveOrZero(message = "Sold books should not be a negative number")
    @Column(name = "sold_copies")
    private Integer soldCopies;

    @Column(name = "book_cover",nullable = false, length = 300)
    private String bookCover;

    @Column(name = "book_thumbnail",nullable = false, length = 300)
    private String bookThumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_category", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_section", nullable = false)
    private Section section;

}

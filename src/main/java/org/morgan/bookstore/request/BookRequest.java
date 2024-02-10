package org.morgan.bookstore.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {


    @NotBlank(message = "Book title should not be blank")
    private String title;

    @NotBlank(message = "Book section should not be blank")
    private String section;

    @NotBlank(message = "Book category should not be blank")
    private String category;


    @NotBlank(message = "Book actual price should not be blank")
    @PositiveOrZero(message = "Book actual price should be zero or more")
    private Double actualPrice;


    @NotBlank(message = "Book discount price should not be blank")
    @PositiveOrZero(message = "Book discount price should be zero or more")
    private Double discountPrice;


    @NotBlank(message = "Book number of pages should not be blank")
    @Positive(message = "Book number of pages should be greater than zero")
    private Integer numberOfPages;

    @PositiveOrZero(message = "Number of books in the stock should not be a negative value")
    private Integer booksInStock;


    @PositiveOrZero(message = "Number of books to be printed should not be a negative value")
    private Integer booksToBePrinted;

    @Size(message = "Book description should not exceed 5000 character",max = 5000)
    private String description;

    @PositiveOrZero(message = "Sold books should not be a negative number")
    private Integer soldBooks;

}

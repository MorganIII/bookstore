package org.morgan.bookstore.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookResponse {
    private Integer id;

    private String title;

    private Double actualPrice;

    private Double discountPrice;

    private String bookThumbnail;

    private String description;

    private Integer numberOfPages;

    private String bookCover;

    private Integer copiesInStock;

    private Integer soldCopies;

    private LocalDateTime creationDate;

    private LocalDateTime updatedDate;

    private String category;

    private String section;
}

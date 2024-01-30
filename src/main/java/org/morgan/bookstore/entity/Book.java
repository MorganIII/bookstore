package org.morgan.bookstore.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Book {

    @Id
    private Integer id;

    @Column(unique = true, nullable = false)
    private String title;

    private Double actualPrice;

    private Double discountPrice;

    @Column(nullable = false)
    private Integer numberOfPages;

    private Integer booksInStock;

    private Integer booksToBePrinted;

    private String Description;

    private Date creationDate;

    private Date updatedDate;

    @Column(nullable = false)
    private String thumbnailImage;

    @Column(nullable = false)
    private String bookImage;

    private Integer soldBooks;

    @ManyToOne
    @JoinColumn(name = "sub_category", nullable = false)
    private SubCategory subCategory;

    @ManyToOne
    @JoinColumn(name = "main_category", nullable = false)
    private MainCategory mainCategory;

}

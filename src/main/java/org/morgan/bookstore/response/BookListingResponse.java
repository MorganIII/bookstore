package org.morgan.bookstore.response;


import lombok.*;

@Builder
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class BookListingResponse {

    private Integer id;

    private String title;

    private Double actualPrice;

    private Double discountPrice;

    private String bookThumbnail;


}

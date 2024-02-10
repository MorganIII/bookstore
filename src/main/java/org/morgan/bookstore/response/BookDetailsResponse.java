package org.morgan.bookstore.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDetailsResponse {


    @JsonUnwrapped
    private BookListingResponse bookListingResponse;

    private String description;

    private Integer numberOfPages;

    private String coverImage;

    private Boolean inStock;
}

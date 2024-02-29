package org.morgan.bookstore.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.morgan.bookstore.enums.Privacy;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WishlistListing {

    private Integer id;
    private String name;
    private LocalDateTime creationTime;
    private Privacy privacy;
}

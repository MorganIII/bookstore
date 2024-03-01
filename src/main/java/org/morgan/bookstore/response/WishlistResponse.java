package org.morgan.bookstore.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.morgan.bookstore.enums.Privacy;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WishlistResponse {
    private Integer wishlistId;
    private String name;
    private String description;
    private Privacy privacy;
    private List<ItemDTO> items;
}

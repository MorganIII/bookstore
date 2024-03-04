package org.morgan.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.request.AddItemRequest;
import org.morgan.bookstore.request.CreateWishlistRequest;
import org.morgan.bookstore.response.WishlistListing;
import org.morgan.bookstore.response.WishlistResponse;
import org.morgan.bookstore.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Wishlist", description = "Wishlist API")
@RestController
@RequestMapping("/api/wishlists")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;

    @Operation(summary = "Create a new wishlist")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Wishlist created"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "409", description = "Wishlist already exists")
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public WishlistResponse createWishlist(@RequestBody CreateWishlistRequest createWishlistRequest) {
        return wishlistService.createWishlist(createWishlistRequest);
    }



    @Operation(summary = "Add an item to a wishlist")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Item added to wishlist"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description = "Wishlist not found")
            }
    )
    @PostMapping("/{wishlist-id}/item")
    public WishlistResponse addItem(@PathVariable(name = "wishlist-id") Integer wishlistId,
                                    @RequestBody AddItemRequest request) {
        return wishlistService.addItem(wishlistId,request);
    }

    @Operation(summary = "Remove an item from a wishlist")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Item removed from wishlist"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description = "Wishlist or item not found")
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{wishlist-id}/item/{item-id}")
    public void removeItem(@PathVariable(name = "wishlist-id") Integer wishlistId,
                           @PathVariable(name = "item-id") Integer itemId) {
        wishlistService.removeItem(wishlistId, itemId);
    }

    @Operation(summary = "Delete a wishlist")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Wishlist deleted"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description = "Wishlist not found")
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{wishlist-id}")
    public void deleteWishlist(@PathVariable(name = "wishlist-id") Integer wishlistId) {
        wishlistService.deleteWishlist(wishlistId);
    }

    @Operation(summary = "Update a wishlist")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Wishlist updated"),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "404", description = "Wishlist not found")
            }
    )
    @PutMapping("/{wishlist-id}")
    public WishlistResponse updateWishlist(@PathVariable(name = "wishlist-id") Integer wishlistId,
                                           @RequestBody CreateWishlistRequest createWishlistRequest) {
        return wishlistService.updateWishlist(wishlistId, createWishlistRequest);
    }

    @Operation(summary = "Get a wishlist")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Wishlist found"),
                    @ApiResponse(responseCode = "404", description = "Wishlist not found")
            }
    )
    @GetMapping("/{wishlist-id}")
    public WishlistResponse getWishlist(@PathVariable(name = "wishlist-id") Integer wishlistId) {
        return wishlistService.getWishlist(wishlistId);
    }

    @Operation(summary = "Get all wishlists")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Wishlists found")
            }
    )
    @GetMapping
    public List<WishlistListing> getWishlists() {
        return wishlistService.getWishlists();
    }




}

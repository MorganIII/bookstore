package org.morgan.bookstore.controller;

import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.request.AddItemRequest;
import org.morgan.bookstore.request.CreateWishlistRequest;
import org.morgan.bookstore.response.WishlistListing;
import org.morgan.bookstore.response.WishlistResponse;
import org.morgan.bookstore.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlists")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public WishlistResponse createWishlist(@RequestBody CreateWishlistRequest createWishlistRequest) {
        return wishlistService.createWishlist(createWishlistRequest);
    }



    @PostMapping("/{wishlist-id}/item")
    public WishlistResponse addItem(@PathVariable(name = "wishlist-id") Integer wishlistId,
                                    @RequestBody AddItemRequest request) {
        return wishlistService.addItem(wishlistId,request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{wishlist-id}/item/{item-id}")
    public void removeItem(@PathVariable(name = "wishlist-id") Integer wishlistId,
                           @PathVariable(name = "item-id") Integer itemId) {
        wishlistService.removeItem(wishlistId, itemId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{wishlist-id}")
    public void deleteWishlist(@PathVariable(name = "wishlist-id") Integer wishlistId) {
        wishlistService.deleteWishlist(wishlistId);
    }

    @PutMapping("/{wishlist-id}")
    public WishlistResponse updateWishlist(@PathVariable(name = "wishlist-id") Integer wishlistId,
                                           @RequestBody CreateWishlistRequest createWishlistRequest) {
        return wishlistService.updateWishlist(wishlistId, createWishlistRequest);
    }


    @GetMapping("/{wishlist-id}")
    public WishlistResponse getWishlist(@PathVariable(name = "wishlist-id") Integer wishlistId) {
        return wishlistService.getWishlist(wishlistId);
    }

    @GetMapping
    public List<WishlistListing> getWishlists() {
        return wishlistService.getWishlists();
    }




}

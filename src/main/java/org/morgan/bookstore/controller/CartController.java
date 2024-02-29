package org.morgan.bookstore.controller;

import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.enums.Government;
import org.morgan.bookstore.request.AddItemRequest;
import org.morgan.bookstore.response.CartPriceResponse;
import org.morgan.bookstore.response.CartResponse;
import org.morgan.bookstore.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CartResponse addItem(@RequestBody AddItemRequest request) {
        return cartService.addItem(request);
    }


    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{itemId}")
    public CartResponse removeItem(@PathVariable Integer itemId) {
        return cartService.removeItem(itemId);
    }

    @PatchMapping("/coupon")
    public CartPriceResponse applyCoupon(@RequestParam("code") String couponCode) {
        return cartService.applyCoupon(couponCode);
    }

    @PatchMapping("/shipping")
    public CartPriceResponse updateShipping(@RequestParam("government")  String government) {
        return cartService.updateShipping(Government.fromValue(government));
    }

    @DeleteMapping("/coupon")
    public CartPriceResponse removeCoupon() {
        return cartService.removeCoupon();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/clear")
    public void clearCart() {
        cartService.clearCart();
    }

    @GetMapping
    public CartResponse getCart() {
        return cartService.getCart();
    }
}

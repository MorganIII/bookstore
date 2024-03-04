package org.morgan.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.enums.Government;
import org.morgan.bookstore.request.AddItemRequest;
import org.morgan.bookstore.response.CartPriceResponse;
import org.morgan.bookstore.response.CartResponse;
import org.morgan.bookstore.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Cart", description = "The cart API")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Add item to cart", description = "Add item to cart")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Item added to cart"),
                    @ApiResponse(responseCode = "404", description = "Item not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CartResponse addItem(@RequestBody @Valid AddItemRequest request) {
        return cartService.addItem(request);
    }


    @Operation(summary = "Remove item from cart", description = "Remove item from cart")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Item removed from cart"),
                    @ApiResponse(responseCode = "404", description = "Item not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{itemId}")
    public CartResponse removeItem(@PathVariable @Positive Integer itemId) {
        return cartService.removeItem(itemId);
    }

    @Operation(summary = "apply coupon", description = "apply coupon to cart")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Coupon applied to cart"),
                    @ApiResponse(responseCode = "404", description = "Coupon not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    @PatchMapping("/coupon")
    public CartPriceResponse applyCoupon(@RequestParam("code") @NotBlank String couponCode) {
        return cartService.applyCoupon(couponCode);
    }


    @Operation(summary = "update shipping", description = "update customer shipping address")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Shipping updated"),
                    @ApiResponse(responseCode = "404", description = "government not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    @PatchMapping("/shipping")
    public CartPriceResponse updateShipping(@RequestParam("government")  @NotBlank String government) {
        return cartService.updateShipping(Government.fromValue(government));
    }

    @Operation(summary = "remove coupon", description = "remove coupon applied to cart")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Coupon removed from cart"),
                    @ApiResponse(responseCode = "404", description = "Coupon not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    @DeleteMapping("/coupon")
    public CartPriceResponse removeCoupon() {
        return cartService.removeCoupon();
    }

    @Operation(summary = "clear cart", description = "clear cart")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Cart cleared")
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/clear")
    public void clearCart() {
        cartService.clearCart();
    }

    @Operation(summary = "get cart", description = "get cart")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Cart retrieved")
            }
    )
    @GetMapping
    public CartResponse getCart() {
        return cartService.getCart();
    }
}

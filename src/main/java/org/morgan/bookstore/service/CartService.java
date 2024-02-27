package org.morgan.bookstore.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.enums.CartStatus;
import org.morgan.bookstore.enums.ChangeType;
import org.morgan.bookstore.enums.Government;
import org.morgan.bookstore.exception.CouponException;
import org.morgan.bookstore.model.*;
import org.morgan.bookstore.order.OrderHandler;
import org.morgan.bookstore.repository.CartItemRepository;
import org.morgan.bookstore.repository.CartRepository;
import org.morgan.bookstore.request.CartRequest;
import org.morgan.bookstore.request.OrderRequest;
import org.morgan.bookstore.response.CartPriceResponse;
import org.morgan.bookstore.response.CartResponse;
import org.morgan.bookstore.response.ItemDTO;
import org.morgan.bookstore.response.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService extends OrderHandler {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final BookService bookService;
    private final AddressService addressService;
    private final CouponService couponService;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public CartResponse addItem(CartRequest request) {
        Cart cart = getCartByUserId(userService.userId());
        Book book = bookService.getBookById(request.getBookId());
        bookService.validateBookAvailability(request.getBookId(), request.getQuantity(), book.getTitle());
        if(cartItemRepository.existsByCartAndBook(cart, book)) {
            cartItemRepository.updateCartItemQuantity(cart, book, request.getQuantity());
        } else {
            cartItemRepository.save(CartItem.builder()
                    .cart(cart)
                    .book(book)
                    .quantity(request.getQuantity())
                    .build());
        }
        CartResponse cartResponse = buildCartResponse(cart);
        cartRepository.save(cart);
        return cartResponse;
    }

    @Transactional
    public CartResponse removeItem(Integer itemId) {
        Cart cart = getCartByUserId(userService.userId());
        if(cartItemRepository.existsByCartAndId(cart, itemId)) {
            cartItemRepository.deleteById(itemId);
        } else {
            throw new IllegalArgumentException("Item not found");
        }
        CartResponse cartResponse = buildCartResponse(cart);
        cartRepository.save(cart);
        return cartResponse;
    }

    public CartResponse getCart() {
        Cart cart = getCartByUserId(userService.userId());
        return buildCartResponse(cart);
    }

    @Transactional
    public void clearCart() {
        Cart cart = getCartByUserId(userService.userId());
        cartItemRepository.clearCart(cart);
        cart.clearCart();
        cartRepository.save(cart);
    }

    @Transactional
    public CartPriceResponse applyCoupon(String couponCode) {
        Cart cart = getCartByUserId(userService.userId());
        validateCartNotEmpty(cart);
        if(cart.getCoupon() != null) {
            throw new CouponException("Coupon already applied");
        }
        Coupon coupon = couponService.getCouponByCode(couponCode);
        Double itemsPrice = cart.getItemsPrice();
        Double shippingPrice = cart.getShippingPrice();
        Double oldTotal = itemsPrice + shippingPrice;
        couponService.validateCoupon(coupon, itemsPrice);
        Double newTotal = couponService.calculateDiscount(coupon, itemsPrice, shippingPrice);
        cart.setDiscountPrice(oldTotal-newTotal);
        cart.setCoupon(coupon);
        cartRepository.save(cart);
        return CartPriceResponse.builder().
                oldPrice(oldTotal).
                newPrice(newTotal).build();
    }

    public CartPriceResponse removeCoupon() {
        Cart cart = getCartByUserId(userService.userId());
        validateCartNotEmpty(cart);
        if(cart.getCoupon() == null) {
            throw new CouponException("No coupon to remove");
        }
        cart.setCoupon(null);
        Double itemsPrice = cart.getItemsPrice();
        Double shippingPrice = cart.getShippingPrice();
        Double discount = cart.getDiscountPrice();
        Double oldTotal = itemsPrice + shippingPrice - discount;
        cart.setDiscountPrice(0.0);
        cartRepository.save(cart);
        return CartPriceResponse.builder().
                oldPrice(oldTotal).
                newPrice(itemsPrice + shippingPrice).build();
    }

    @Transactional
    public CartPriceResponse updateShipping(Government government) {
        Cart cart = getCartByUserId(userService.userId());
        validateCartNotEmpty(cart);
        Double oldShippingPrice = cart.getShippingPrice();
        Double itemsPrice = cart.getItemsPrice();
        Double discount = cart.getDiscountPrice();
        cart.setShippingPrice(government.getShippingPrice());
        cartRepository.save(cart);
        Double newTotal = itemsPrice + government.getShippingPrice() - discount;
        return CartPriceResponse.builder().
                oldPrice(itemsPrice + oldShippingPrice - discount).
                newPrice(newTotal).build();
    }

    public CartResponse buildCartResponse(Cart cart) {
        List<ItemDTO> items = getItemDTOS(cart);

        Double cartSubTotal = items
                .stream()
                .mapToDouble(ItemDTO::getSubTotal)
                .sum();

        cart.setTotalItems(items.size());
        cart.setItemsPrice(cartSubTotal);

        /* get the shipping price*/
        if(cart.getShippingPrice() == 0) {
            cart.setShippingPrice(getShippingPrice(cart));
        }
        Double shippingPrice = cart.getShippingPrice();

        /* get the total*/
        Double total = cartSubTotal + shippingPrice;

        /* get the total after discount*/
        Double totalAfterDiscount;
        Coupon coupon = cart.getCoupon();
        if(coupon != null) {
            totalAfterDiscount = couponService.calculateDiscount(cart.getCoupon(), cartSubTotal, shippingPrice);
        } else {
            totalAfterDiscount = total;
        }
        cart.setDiscountPrice(total - totalAfterDiscount);

        return CartResponse.builder()
                .items(items)
                .totalItems(cart.getTotalItems())
                .cartSubTotal(cartSubTotal)
                .cartShippingPrice(shippingPrice)
                .total(total)
                .couponCode(coupon != null ? coupon.getCode() : null)
                .totalAfterDiscount(totalAfterDiscount)
                .build();
    }

    @NotNull
    private List<ItemDTO> getItemDTOS(Cart cart) {
        List<ItemDTO> items = cartItemRepository.getCartItems(cart);
        items = items.stream().peek(item -> item.setSubTotal(item.getQuantity() * item.getActualPrice())).toList();
        return items;
    }


    public Double getShippingPrice(Cart cart) {
        if(cart.getShippingPrice() == 0) {
            Address address = addressService.getDefaultAddress();
            if(address != null) {
                return address.getShippingPrice();
            } else {
                return Government.CAIRO.getShippingPrice();
            }
        }
        return cart.getShippingPrice();
    }

    public void validateCartNotEmpty(Cart cart) {
        if(cartItemRepository.countCartItems(cart) == 0) {
            throw new IllegalArgumentException("Cart is empty");
        }
    }

    public static Cart createCart(Cart cart, User user) {
        if(cart == null) {
            cart = Cart.builder()
                    .cartStatus(CartStatus.READ_WRITE)
                    .user(user)
                    .discountPrice(0.0)
                    .itemsPrice(0.0)
                    .shippingPrice(0.0)
                    .totalItems(0)
                    .build();
        }
        return cart;
    }

public Cart getCartByUserId(Integer userId) {
        return cartRepository.getCartByUser(userId).orElseThrow(() -> new EntityNotFoundException("Cart not found"));
    }


    @Override
    @Transactional
    public OrderResponse handleOrder(OrderRequest request, OrderResponse response, Order order) {
        User user = order.getUser();
        Cart cart = getCartByUserId(user.getId());
        validateCartNotEmpty(cart);
        List<ItemDTO> items = getItemDTOS(cart);
        bookService.validateBooksAvailability(items);
        bookService.updateBookCopiesInStock(items, ChangeType.DECREASE);
        bookService.updateSoldCopies(items, ChangeType.INCREASE);
        for(ItemDTO item: items) {
            order.getOrderItems().add(OrderItem.builder()
                    .book(bookService.getBookById(item.getBookId()))
                    .quantity(item.getQuantity())
                    .price(item.getActualPrice())
                    .totalPrice(item.getSubTotal())
                    .order(order)
                    .build());
        }
        Double orderTotal = items
                .stream()
                .mapToDouble(ItemDTO::getSubTotal)
                .sum();
        response.setItems(items);
        response.setTotalItems(items.size());
        response.setSubTotal(orderTotal);
        order.setTotalPrice(orderTotal);
        order.setTotalItems(items.size());
        Coupon coupon = cart.getCoupon();
        if(coupon != null) {
            order.setCoupon(coupon);
        }
        //cart.clearCart();
        return process(request, response, order);
    }


}

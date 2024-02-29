package org.morgan.bookstore.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.enums.Privacy;
import org.morgan.bookstore.model.Book;
import org.morgan.bookstore.model.Wishlist;
import org.morgan.bookstore.model.WishlistItem;
import org.morgan.bookstore.repository.WishlistItemRepository;
import org.morgan.bookstore.repository.WishlistRepository;
import org.morgan.bookstore.request.AddItemRequest;
import org.morgan.bookstore.request.CreateWishlistRequest;
import org.morgan.bookstore.response.ItemDTO;
import org.morgan.bookstore.response.WishlistListing;
import org.morgan.bookstore.response.WishlistResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserService userService;
    private final BookService bookService;
    private final WishlistItemRepository wishlistItemRepository;
    private final CartService cartService;
    public WishlistResponse createWishlist(CreateWishlistRequest createWishlistRequest) {
        checkIfWishlistExists(createWishlistRequest.getName(), userService.userId());
        Wishlist wishlist = Wishlist.builder()
                .name(createWishlistRequest.getName())
                .description(createWishlistRequest.getDescription())
                .privacy(createWishlistRequest.getPrivacy())
                .user(userService.getUserById(userService.userId()))
                .build();
        wishlist = wishlistRepository.save(wishlist);
        if(createWishlistRequest.getBookId() != null) {
            Book book = bookService.getBookById(createWishlistRequest.getBookId());
            WishlistItem wishlistItem = WishlistItem.builder()
                    .book(book)
                    .quantity(1)
                    .wishlist(wishlist)
                    .build();
            wishlistItemRepository.save(wishlistItem);
        }
        return buildWishlistResponse(wishlist);
    }

    public WishlistResponse addItem(Integer wishlistId,AddItemRequest request) {
        Wishlist wishlist = getWishlistByIdAndUserId(wishlistId, userService.userId());
        Book book = bookService.getBookById(request.getBookId());
        if(wishlistItemRepository.existsByWishlistAndBook(wishlist, book)) {
            wishlistItemRepository.updateWishlistItemQuantity(wishlist, book, request.getQuantity());
        } else {
            WishlistItem wishlistItem = WishlistItem.builder()
                    .book(book)
                    .quantity(request.getQuantity())
                    .wishlist(wishlist)
                    .build();
            wishlistItemRepository.save(wishlistItem);
        }
        return buildWishlistResponse(wishlist);
    }

    public void removeItem(Integer wishlistId, Integer itemId) {
        Wishlist wishlist = getWishlistByIdAndUserId(wishlistId, userService.userId());
        if(!wishlistItemRepository.existsByWishlistAndId(wishlist, itemId)) {
            throw new EntityNotFoundException("Item with id " + itemId + " not found in wishlist with id " + wishlistId);
        }
        wishlistItemRepository.deleteById(itemId);
    }

    public void deleteWishlist(Integer wishlistId) {
        Wishlist wishlist = getWishlistByIdAndUserId(wishlistId, userService.userId());
        wishlistItemRepository.deleteByWishlist(wishlist);
        wishlistRepository.delete(wishlist);
    }

    public WishlistResponse updateWishlist(Integer wishlistId, CreateWishlistRequest request) {
        Wishlist wishlist = getWishlistByIdAndUserId(wishlistId, userService.userId());
        wishlist.setName(request.getName());
        wishlist.setDescription(request.getDescription());
        wishlist.setPrivacy(request.getPrivacy());
        wishlist = wishlistRepository.save(wishlist);
        return buildWishlistResponse(wishlist);
    }

    public WishlistResponse getWishlist(Integer wishlistId) {
        Wishlist wishlist = getWishlistById(wishlistId);
        if(wishlist.getPrivacy().equals(Privacy.PRIVATE) &&
                (userService.isAnonymousUser() ||!wishlist.getUser().getId().equals(userService.userId()))) {
            throw new EntityNotFoundException("Wishlist with id " + wishlistId + " not found");
        }
        return buildWishlistResponse(wishlist);
    }

    public List<WishlistListing> getWishlists() {
        return wishlistRepository.findAllByUserId(userService.userId());
    }



    public Wishlist getWishlistById(Integer wishlistId) {
        return wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist with id " + wishlistId + " not found"));
    }

    public Wishlist getWishlistByIdAndUserId(Integer wishlistId, Integer userId) {
        return wishlistRepository.findByIdAndUserId(wishlistId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist with id " + wishlistId + " not found"));
    }



    public void checkIfWishlistExists(String name, Integer userId) {
        if(wishlistRepository.existsByNameAndUserId(name, userId)) {
            throw new EntityExistsException("Wishlist with name " + name + " already exists");
        }
    }

    public WishlistResponse buildWishlistResponse(Wishlist wishlist) {
        List<ItemDTO> items = wishlistItemRepository.getWishlistItems(wishlist);
        return WishlistResponse.builder()
                .wishlistId(wishlist.getId())
                .name(wishlist.getName())
                .description(wishlist.getDescription())
                .privacy(wishlist.getPrivacy())
                .items(items)
                .build();
    }


}

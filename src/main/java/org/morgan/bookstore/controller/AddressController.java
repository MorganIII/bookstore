package org.morgan.bookstore.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.enums.Government;
import org.morgan.bookstore.handler.ErrorResponse;
import org.morgan.bookstore.model.Address;
import org.morgan.bookstore.request.AddressRequest;
import org.morgan.bookstore.service.AddressService;
import org.morgan.bookstore.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name = "Address", description = "Address API")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    private final UserService userService;

    @Operation(summary = "Add new address", description = """
            Add new address to the user's addresses list. If the user has no addresses,
            the new address will be set as default. If the user has addresses, the new address will
            be added to the list and the default address will not be changed. If the user tries to add
             an address that already exists, the system will return an error message.
             """)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Address created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AddressRequest.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Address already exists",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AddressRequest addAddress(@RequestBody @Valid AddressRequest addressRequest) {
        addressService.addAddress(addressRequest);
        return addressRequest;
    }


    @Operation(summary = "Update address", description = """
           Update the user's address.
            """)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Address updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AddressRequest.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Address does not exist",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PutMapping("/{id}")
    public AddressRequest updateAddress(@PathVariable(name = "id") @PositiveOrZero Integer addressId,
                                        @RequestBody @Valid AddressRequest addressRequest) {
        addressService.updateAddress(addressId, addressRequest);
        return addressRequest;
    }

    @Operation(summary = "Delete address", description = """
            Delete the user's address.
            """)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Address deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Address does not exist",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )

    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable(name = "id") @PositiveOrZero Integer addressId) {
        addressService.deleteAddress(addressId);
    }

    @Operation(summary = "Get address", description = """
            Get the user's address.
            """)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Address retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Address.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Address does not exist",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public Address getAddress(@PathVariable(name = "id") @PositiveOrZero Integer addressId) {
        return addressService.getAddressByIdAndUserId(addressId, userService.userId());
    }

    @Operation(summary = "Get default address", description = """
            Get the user's default address.
            """)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Default address retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Address.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Default address does not exist",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/default")
    public Address getDefaultAddress() {
        return addressService.getDefaultAddress();
    }



    @Operation(summary = "Get all addresses", description = """
            Get all the user's addresses.
            """)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Addresses retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Address.class)
                            )
                    )
            }
    )
    @GetMapping
    public Set<Address> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @Operation(summary = "Set address as default", description = """
            Set the user's address as default.
            """)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Address set as default successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Address.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Address does not exist",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PatchMapping("/{id}/default")
    public Address setAddressAsDefault(@PathVariable(name = "id") @PositiveOrZero Integer addressId) {
        return addressService.setAddressAsDefault(addressId);
    }

    @Operation(summary = "Get governments", description = """
            Get all governments.
            """)
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Governments retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            }
    )
    @GetMapping("/governments")
    public List<String> getGovernments() {
        return Government.getGovernments();
    }
}

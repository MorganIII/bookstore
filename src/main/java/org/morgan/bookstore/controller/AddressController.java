package org.morgan.bookstore.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.enums.Government;
import org.morgan.bookstore.model.Address;
import org.morgan.bookstore.request.AddressRequest;
import org.morgan.bookstore.service.AddressService;
import org.morgan.bookstore.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    private final UserService userService;

    @PostMapping
    public AddressRequest addAddress(@RequestBody @Valid AddressRequest addressRequest) {
        addressService.addAddress(addressRequest);
        return addressRequest;
    }

    @PutMapping("/{id}")
    public AddressRequest updateAddress(@PathVariable(name = "id") Integer addressId,
                                        @RequestBody @Valid AddressRequest addressRequest) {
        addressService.updateAddress(addressId, addressRequest);
        return addressRequest;
    }


    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable(name = "id") Integer addressId) {
        addressService.deleteAddress(addressId);
    }

    @GetMapping("/{id}")
    public Address getAddress(@PathVariable(name = "id") Integer addressId) {
        return addressService.getAddressByIdAndUserId(addressId, userService.userId());
    }

    @GetMapping("/default")
    public Address getDefaultAddress() {
        return addressService.getDefaultAddress();
    }

    @GetMapping
    public Set<Address> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @PatchMapping("/{id}/default")
    public Address setAddressAsDefault(@PathVariable(name = "id") Integer addressId) {
        return addressService.setAddressAsDefault(addressId);
    }

    @GetMapping("/governments")
    public List<String> getGovernments() {
        return Government.getGovernments();
    }
}

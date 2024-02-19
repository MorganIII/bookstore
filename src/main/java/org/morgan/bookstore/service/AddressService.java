package org.morgan.bookstore.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.morgan.bookstore.model.Address;
import org.morgan.bookstore.model.User;
import org.morgan.bookstore.repository.AddressRepository;
import org.morgan.bookstore.request.AddressRequest;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public void addAddress(AddressRequest request) {
        Integer userId = userService.userId();
        User user = userService.getUserById(userId);
        Set<Address> addresses = user.getAddresses();
        Address address = modelMapper.map(request, Address.class);
        if(addresses.isEmpty()) {
            address.setIsDefault(true);
            address.setUser(user);
        } else if (addresses.contains(address)) {
            throw new EntityExistsException("the address you are trying to add is already exists");
        } else {
            address.setIsDefault(false);
            address.setUser(user);
        }
        addressRepository.save(address);
    }

    public void updateAddress(Integer addressId, AddressRequest request) {
        Integer userId = userService.userId();
        User user = userService.getUserById(userId);
        Set<Address> addresses = user.getAddresses();
        Address requiredAddress = modelMapper.map(request, Address.class);
        Address targetedAddress = getAddressByIdAndUserId(addressId, userId);
        if(addresses.contains(requiredAddress)) {
            throw new EntityExistsException("the address you are trying to add is already exists");
        }
        modelMapper.map(request, targetedAddress);
        addressRepository.save(targetedAddress);
    }

    public void deleteAddress(Integer addressId) {
        Integer userId = userService.userId();
        Address targetedAddress = getAddressByIdAndUserId(addressId, userId);
        addressRepository.delete(targetedAddress);
    }

    public Set<Address> getAllAddresses() {
        Integer userId = userService.userId();
        return addressRepository.getAddressByUserId(userId);
    }

    public Address getAddressByIdAndUserId(Integer addressId, Integer userId) {
        return addressRepository.findAddressByIdAndUserId(addressId, userId)
                .orElseThrow(()-> new EntityNotFoundException(String.format("Address with %d is not found",addressId)));
    }

    public Address setAddressAsDefault(Integer addressId) {
        Integer userId = userService.userId();
        User user = userService.getUserById(userId);
        Set<Address> addresses = user.getAddresses();
        Address targetedAddress = getAddressByIdAndUserId(addressId, userId);
        addresses.stream().filter(Address::getIsDefault).forEach(address -> {
            address.setIsDefault(false);
            addressRepository.save(address);
        });
        targetedAddress.setIsDefault(true);
        addressRepository.save(targetedAddress);
        return targetedAddress;
    }

    public Address getDefaultAddress() {
        Integer userId = userService.userId();
        return addressRepository.findAddressByUserIdAndIsDefault(userId, true)
                .orElseThrow(()-> new EntityNotFoundException("Default address is not found"));
    }
}
package org.morgan.bookstore.repository;

import org.morgan.bookstore.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface AddressRepository extends JpaRepository<Address,Integer> {


    Optional<Address> findAddressByIdAndUserId(Integer addressId, Integer userId);

    Set<Address> getAddressByUserId(Integer userId);


    Optional<Address> findAddressByUserIdAndIsDefault(Integer userId, Boolean isDefault);
}

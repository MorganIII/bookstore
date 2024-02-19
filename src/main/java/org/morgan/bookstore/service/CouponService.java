package org.morgan.bookstore.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.morgan.bookstore.exception.CouponException;
import org.morgan.bookstore.model.Coupon;
import org.morgan.bookstore.repository.CouponRepository;
import org.morgan.bookstore.request.CouponRequest;
import org.morgan.bookstore.response.CouponResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    private final ModelMapper modelMapper;

    public void addCoupon(CouponRequest request) {
        validateCouponDuplicate(request.getCode());
        Coupon coupon = modelMapper.map(request, Coupon.class);
        coupon.setActive(true);
        couponRepository.save(coupon);
    }


    public void updateCoupon(Integer id, CouponRequest request){
        Coupon coupon = getCouponById(id);
        modelMapper.map(request, coupon);
        couponRepository.save(coupon);
    }

    public void deleteCoupon(Integer id) {
        Coupon coupon = getCouponById(id);
        couponRepository.delete(coupon);
    }

    public void changeCouponStatus(Integer id, Boolean active) {
        Coupon coupon = getCouponById(id);
        coupon.setActive(active);
        couponRepository.save(coupon);
    }

    public Coupon getCouponById(Integer id) {

        return couponRepository.findById(id).
                orElseThrow(()-> new EntityNotFoundException(String.format("coupon with id %s not found", id)));
    }

    public List<CouponResponse> getAllCoupons(Boolean active) {
        return couponRepository.findAllCoupons(active);
    }

    public void validateCouponDuplicate(String code) {
        Optional<Coupon> coupon = couponRepository.findCouponByCode(code);
        if(coupon.isPresent()) {
            throw new EntityExistsException(String.format("coupon with code %s is already found",code));
        }
    }

    public Double calculateDiscount(Coupon coupon, Double subTotal, Double shippingPrice) {
        return switch (coupon.getType()) {
            case FREE_SHIPPING -> subTotal;
            case PERCENTAGE -> subTotal - (subTotal * coupon.getDiscount() / 100)+shippingPrice;
            case FIXED_AMOUNT -> subTotal - coupon.getDiscount()+shippingPrice;
        };
    }

    public void validateCoupon(Coupon coupon, Double subTotal) {
        if(!coupon.isCouponValid()) {
            throw new CouponException(String.format("coupon with code %s is expired",coupon.getCode()));
        } else if(coupon.getMinimumAmount() > subTotal) {
            throw new CouponException(String.format("coupon with code %s is not valid for this amount",coupon.getCode()));
        }
    }


    public Coupon getCouponByCode(String code) {
        return couponRepository.findCouponByCode(code)
                .orElseThrow(()-> new EntityNotFoundException(String.format("coupon with code %s not found", code)));
    }


}

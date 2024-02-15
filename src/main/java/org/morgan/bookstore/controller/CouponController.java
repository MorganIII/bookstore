package org.morgan.bookstore.controller;

import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.enums.CouponType;
import org.morgan.bookstore.model.Coupon;
import org.morgan.bookstore.request.CouponRequest;
import org.morgan.bookstore.response.CouponResponse;
import org.morgan.bookstore.service.CouponService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void addCoupon(@RequestBody CouponRequest request){
        couponService.addCoupon(request);

    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public void updateCoupon(@PathVariable(name = "id") Integer id, @RequestBody CouponRequest request){
        couponService.updateCoupon(id, request);
    }

    @PatchMapping("/{id}")
    public void changeCouponStatus(@PathVariable(name = "id") Integer id, @RequestParam(name ="active") Boolean active){
        couponService.changeCouponStatus(id, active);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCoupon(@PathVariable(name = "id") Integer id){
        couponService.deleteCoupon(id);
    }

    @GetMapping("/{id}")
    public Coupon getCouponById(@PathVariable(name = "id") Integer id){
        return couponService.getCouponById(id);
    }


    @GetMapping
    public List<CouponResponse> getAllCoupons(@RequestParam(name = "active", required = false) Boolean active){
        return couponService.getAllCoupons(active);
    }

    @GetMapping("/types")
    public List<String> getCouponsType(){
        return CouponType.getCouponTypes();
    }

}

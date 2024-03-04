package org.morgan.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.enums.CouponType;
import org.morgan.bookstore.model.Coupon;
import org.morgan.bookstore.request.CouponRequest;
import org.morgan.bookstore.response.CouponResponse;
import org.morgan.bookstore.service.CouponService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@Tag(name = "Coupon", description = "The coupon API")
@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @Operation(summary = "Add coupon", description = "Add new coupon")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Coupon added"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "409", description = "Coupon already exists")
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void addCoupon(@RequestBody CouponRequest request){
        couponService.addCoupon(request);

    }

    @Operation(summary = "Update coupon", description = "Update coupon")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Coupon updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Coupon not found")
            }
    )
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public void updateCoupon(@PathVariable(name = "id") Integer id, @RequestBody CouponRequest request){
        couponService.updateCoupon(id, request);
    }

    @Operation(summary = "Change coupon status", description = "Change coupon status")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Coupon status changed"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Coupon not found")
            }
    )
    @PatchMapping("/{id}")
    public void changeCouponStatus(@PathVariable(name = "id") Integer id, @RequestParam(name ="active") Boolean active){
        couponService.changeCouponStatus(id, active);
    }

    @Operation(summary = "Delete coupon", description = "Delete coupon from the system")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Coupon deleted"),
                    @ApiResponse(responseCode = "404", description = "Coupon not found")
            }
    )

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCoupon(@PathVariable(name = "id") @Positive Integer id){
        couponService.deleteCoupon(id);
    }

    @Operation(summary = "Get coupon", description = "Get coupon by id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Coupon found"),
                    @ApiResponse(responseCode = "404", description = "Coupon not found")
            }
    )
    @GetMapping("/{id}")
    public Coupon getCouponById(@PathVariable(name = "id") @Positive Integer id){
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


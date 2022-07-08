package com.practice.shoppingmall.domain.coupon.service;

import com.practice.shoppingmall.domain.user.facade.UserFacade;
import com.practice.shoppingmall.domain.coupon.presentation.dto.response.FindCouponGroupResponse;
import com.practice.shoppingmall.domain.coupon.domain.Coupon;
import com.practice.shoppingmall.domain.coupon.domain.repository.CouponRepository;
import com.practice.shoppingmall.domain.coupon.domain.UserCoupon;
import com.practice.shoppingmall.domain.user.domain.User;
import com.practice.shoppingmall.domain.user.domain.repository.UserRepository;
import com.practice.shoppingmall.global.exception.order.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCouponServiceImpl implements UserCouponService{

    private final UserFacade userFacade;

    private final CouponRepository couponRepository;

    private final UserRepository userRepository;

    @Override
    public FindCouponGroupResponse findMyCoupon() {

        User user = userFacade.nowUser();

        List<Coupon> coupons =  user.getCoupons()
                .stream()
                .map(UserCoupon::getCoupon)
                .collect(Collectors.toList());

        return FindCouponGroupResponse.of(coupons);
    }

    @Override
    public void addMyCoupon(UUID couponId) {

        User user = userFacade.nowUser();

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> OrderNotFoundException.EXCEPTION);

        user.addCoupon(coupon);

        userRepository.save(user);
    }
}
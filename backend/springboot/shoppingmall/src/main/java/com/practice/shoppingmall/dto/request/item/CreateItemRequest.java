package com.practice.shoppingmall.dto.request.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateItemRequest {
    @NotNull(message = "이름을 입력해주세요")
    private String name;

    @NotNull(message = "가격을 입력해주세요")
    private int price;

    @NotNull(message = "재고량을 입력해주세요")
    private int stock;
}
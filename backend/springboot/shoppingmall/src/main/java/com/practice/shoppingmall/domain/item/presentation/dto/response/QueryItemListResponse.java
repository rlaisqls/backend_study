package com.practice.shoppingmall.domain.item.presentation.dto.response;

import com.practice.shoppingmall.domain.item.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class QueryItemListResponse {
    List<FindItemResponse> itemList;

    public static QueryItemListResponse of(List<Item> itemList) {

        List<FindItemResponse> itemResponseList = itemList
                .stream()
                .map(FindItemResponse::of)
                .collect(Collectors.toList());

        return new QueryItemListResponse(itemResponseList);
    }

    @Getter
    @Builder
    private static class FindItemResponse {

        private Long itemId;

        private String itemName;

        private Integer price;

        private Integer stock;

        private Boolean isSoldOut;

        public static FindItemResponse of (Item item) {

            return FindItemResponse
                    .builder()
                    .itemId(item.getId())
                    .itemName(item.getName())
                    .price(item.getPrice())
                    .stock(item.getStock())
                    .isSoldOut(item.getStock() == 0)
                    .build();
        }
    }
}
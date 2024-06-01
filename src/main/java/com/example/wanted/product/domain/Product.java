package com.example.wanted.product.domain;

import com.example.wanted.module.exception.ResourceNotFoundException;
import com.example.wanted.user.domain.User;
import com.example.wanted.user.infrastucture.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Product {
    private Long id;
    private String name;
    private int price;
    private int quantity;
    private ProductSellingStatus sellingStatus;
    private User seller;

    @Builder
    public Product(
            Long id,
            String name,
            int price,
            int quantity,
            ProductSellingStatus sellingStatus,
            User seller
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.sellingStatus = sellingStatus;
        this.seller = seller;
    }

    public static Product from(ProductCreate productCreate, User seller) {
        return Product.builder()
                .name(productCreate.getName())
                .price(productCreate.getPrice())
                .quantity(productCreate.getQuantity())
                .seller(seller)
                .sellingStatus(ProductSellingStatus.SELLING)
                .build();
    }

    public void deductQuantity() {
        if(this.quantity == 1) {
            sellingStatus = ProductSellingStatus.RESERVATION;
        } else if (this.quantity < 1) {
            throw new IllegalStateException("차감할 재고 수량이 없습니다.");
        }
        this.quantity -=1;
    }

    public boolean checkSeller(User user) {
        return this.seller.getId().equals(user.getId());
    }

    public void complete() {
        if(this.quantity != 0 || !sellingStatus.equals(ProductSellingStatus.RESERVATION)) {
            throw new IllegalStateException("판매 완료가 가능한 생태가 아닙니다.");
        }
        sellingStatus = ProductSellingStatus.COMPLETE;
    }

}

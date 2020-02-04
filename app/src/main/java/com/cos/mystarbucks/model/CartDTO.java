package com.cos.mystarbucks.model;

import java.util.List;

import lombok.Data;

@Data
public class CartDTO {

    private List<Cart> carts;

    @Data
    public class Cart{
        private int id;
        private int userId;
        private String name;
        private int price;
        private int tempCount = 1;
    }
}

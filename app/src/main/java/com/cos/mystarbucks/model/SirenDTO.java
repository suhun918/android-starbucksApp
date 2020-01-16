package com.cos.mystarbucks.model;


import java.util.List;

import lombok.Data;


@Data
public class SirenDTO {
    private List<Food> foods;
    private List<Beverage> beverages;

    @Data
    public class Food{
        private int id;
        private String name;
        private String image;
        private int price;
        private String category;
    }

    @Data
    public class Beverage{
        private int id;
        private String name;
        private String image;
        private int price;
        private String category;
    }

}


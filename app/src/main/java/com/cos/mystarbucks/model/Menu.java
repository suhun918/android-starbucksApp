package com.cos.mystarbucks.model;

import java.util.List;

import lombok.Data;

@Data
public class Menu {
    private List<Coffee> coffees;
    private List<Beverage> beverages;

    @Data
    public class Coffee{
        private int id;
        private String detail;
        private String name;
        private String image;
        private int price;
        private String roast;
        private String flavor;
        private String feel;
        private String strong;
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

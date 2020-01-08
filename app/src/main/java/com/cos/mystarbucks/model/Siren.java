package com.cos.mystarbucks.model;

import java.util.List;

import lombok.Data;


@Data
public class Siren{
    private List<Coffee> coffees;
    private List<Beverage> beverages;

    @Data
    public class Coffee{
        private int id;
        private String detail;
        private String name;
        private String image;
    }

    @Data
    public class Beverage{
        private int id;
        private String name;
        private String image;
    }

}


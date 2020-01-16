package com.cos.mystarbucks.model;

import java.util.List;

import lombok.Data;

@Data
public class PointCardDTO {
    private List<Card> cards;

    @Data
    public class Card{
        private int id;
        private String name;
        private String image;
    }
}

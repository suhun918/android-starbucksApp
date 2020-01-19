package com.cos.mystarbucks.model;

import java.util.List;

import lombok.Data;

@Data
public class BoardDTO {
    private List<Board> boards;

    @Data
    public class Board{
        private int id;
        private String title;
        private String content;
        private int userId;
        private String createDate;
    }

}

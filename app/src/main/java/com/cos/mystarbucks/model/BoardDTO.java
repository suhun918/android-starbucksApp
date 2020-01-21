package com.cos.mystarbucks.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class BoardDTO {
    private List<Board> boards;

    @Data
    public class Board{
        private int id;
        private String title;
        private String content;
        private int userId;
        private Timestamp createDate;
    }

}

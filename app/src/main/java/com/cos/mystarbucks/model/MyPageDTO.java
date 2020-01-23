package com.cos.mystarbucks.model;

        import java.sql.Timestamp;

        import lombok.Data;

@Data
public class MyPageDTO {

    private int id;
    private int userId;
    private int cardId;
    private String cardName;
    private String cardImage;
    private int point;
    private Timestamp createDate;

}

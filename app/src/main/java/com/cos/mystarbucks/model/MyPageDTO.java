package com.cos.mystarbucks.model;

    import java.sql.Timestamp;
    import java.util.List;

    import lombok.Data;

@Data
public class MyPageDTO {

    private MyCard myCard;
    private List<MyMenu> myCoffeeList;
    private List<MyMenu> myBeverageList;
    private List<Trade> tradeList;

    @Data
    public class MyCard{
        private int id;
        private int userId;
        private int cardId;
        private String cardName;
        private String cardImage;
        private int point;
        private Timestamp createDate;
    }

    @Data
    public class MyMenu{
        private int id;
        private String name;
        private String image;
        private int price;
    }

    @Data
    public class Trade{
        private int id;
        private int userId;
        private String name;
        private int price;
        private int amount;
        private Timestamp createDate;
    }
}

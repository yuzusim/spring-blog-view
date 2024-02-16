package shop.mtcoding.blog.board;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

public class BoardResponse {

    @Data
    public static class DetailDTO {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        @Column(length = 30)
        private String title;
        private String content;
        private int userId;
        private String username;
    }


    // 화면을 위한 뷰는 필요한 것만 컬렉션으로 가져 오는게 좋다.
    // private int id;
    // private String title;

}


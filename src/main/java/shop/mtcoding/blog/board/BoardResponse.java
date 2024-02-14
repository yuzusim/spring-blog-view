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
}


package shop.mtcoding.blog.board;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class BoardResponse {

    @Data
    public static class DetailDTO {
//        @Id
//        @GeneratedValue(strategy = GenerationType.IDENTITY)
//        private int id;
//
//        @Column(length = 30)
//        private String title;
//        private String content;
//        private int userId;
//        private String username;

        // 댓글보기
        // 쿼리 조회 디테일 DTO뿌리기
        // Board

        private Integer id;
        private String title;
        private String content;
        private Integer userId;
        private String username;
        private Timestamp createdAt;
        private Boolean pageOwner; // 페이지 주인 여부

        private List<ReplyDTO> replies = new ArrayList<>();

        public void addReply(ReplyDTO reply) {
            replies.add(reply);
        }

        public DetailDTO() {
            this.id = id;
            this.title = title;
            this.content = content;
            this.userId = userId;
            this.username = username;
            this.createdAt = createdAt;
        }
    }

        // Reply
        @AllArgsConstructor
        @Data
        public static class ReplyDTO{
            private Integer rId;
            private Integer rUserId;
            private Integer rUsername;
            private String rComment;
            private Boolean rOwner; // 로그인 한 유저가 댓글의 주인인지 여부
        }


    }



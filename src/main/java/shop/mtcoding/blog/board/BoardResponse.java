package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import shop.mtcoding.blog.user.User;

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
        private Integer userId; // 게시글 작성자 아이디
        private String username;
        private Timestamp createdAt;
        private Boolean boardOwner; // 페이지 주인 여부 // 대문자 블리언 안만들면 세터 생성안됨
        // private Boolean boardOwner;
        // private boolean boardOwner; null


        public void isBoardOwner(User sessionUser) { // 세션값 받아서 바로처리 값이 안들어 // 가면 0 Integer변경
            if (sessionUser == null) boardOwner = false;
            else boardOwner = sessionUser.getId() == userId;
        }


    }

    // f릴레이션맵핑 (자바세상데이터 -> 나중에 하이버네이트가 대시ㅏㄴ 해줌)
    // private List<ReplyDTO> replies = new ArrayList<>();

    // public void addReply(ReplyDTO reply) {
//            replies.add(reply);
//        }


    // Reply
    @Data
    public static class ReplyDTO {
        private Integer id;
        private Integer userId;
        private String comment;
        private String username;

        public ReplyDTO(Object[] ob) {
            this.id = (Integer) ob[0];
            this.userId = (Integer) ob[1];
            this.comment = (String) ob[2];
            this.username = (String) ob[3];
        }
    }

}


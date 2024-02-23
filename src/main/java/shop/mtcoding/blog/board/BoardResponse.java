package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.NoArgsConstructor;
import shop.mtcoding.blog.user.User;

import java.util.ArrayList;
import java.util.List;

public class BoardResponse {

    @Data
    public static class DetailDTO {
        private Integer id;
        private String title;
        private String content;
        private Integer userId; // 게시글 작성자 id
        private String username;
        private Boolean boardOwner;

        public void isBoardOwner(User sessionUser){
            if(sessionUser == null) boardOwner = false;
            else boardOwner = sessionUser.getId() == userId;
        }
    }

    @NoArgsConstructor
    @Data
    public static class ReplyDTO {
        private Integer id;
        private Integer userId;
        private String comment;
        private String username;
        private Boolean replyOwner; // 게시글 주인 여부 (세션값과 비교)

        public ReplyDTO(Object[] ob, User sessionUser) {
            this.id = (Integer) ob[0];
            this.userId = (Integer) ob[1];
            this.comment = (String) ob[2];
            this.username = (String) ob[3];

            if(sessionUser == null){
                replyOwner = false;
            }else{
                replyOwner = sessionUser.getId() == userId;
            }
        }
    }
}

    // 화면을 위한 뷰는 필요한 것만 컬렉션으로 가져 오는게 좋다.
    // private int id;
    // private String title;




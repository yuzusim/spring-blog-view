package shop.mtcoding.blog.user;

import lombok.Data;

/**
 * 요청 DTO = Data Transfer Object
 */

public class UserRequest {
    // 항아리 만들기 -> 요청받는 데이터
    @Data
    public static class JoinDTO{ // join 할때 user한테 요청 되는 데이터
        private String username;
        private String password;
        private String email;
    }

    @Data
    public static class LoginDTO{ // login 할때 user한테 요청 되는 데이터
        private String username;
        private String password;
    }


}
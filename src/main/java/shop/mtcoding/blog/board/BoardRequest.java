package shop.mtcoding.blog.board;

import lombok.Data;

/**
 * 요청 DTO = Data Transfer Object
 */
public class BoardRequest{
    @Data
    public static class SaveDTO{
        private String title;
        private String content;
    }

}






package shop.mtcoding.blog._core.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.mtcoding.blog._core.util.Script;

@ControllerAdvice // 모든 응답 에러 컨트롤러 (뷰 리턴 == 파일을 찾아서 리턴)

public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class) // 모든 에러 다들어옴
    public @ResponseBody String error1(Exception e){
        return Script.back(e.getMessage());
    }
}

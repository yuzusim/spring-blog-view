package shop.mtcoding.blog.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog.board.Board;
import shop.mtcoding.blog.board.BoardRequest;


@RequiredArgsConstructor // final이 붙은 애들에 대한 생성자를 만들어줌
@Controller
public class UserController {

    // 자바는 final 변수는 반드시 초기화가 되어야함.
    private final UserRepository userRepository;
    private final HttpSession session;

    @GetMapping("/user/updateForm")
    public String updateForm(){
        return "/user/updateForm";
    }

    @PostMapping("/user/{id}/update")
    public String update(@PathVariable int id, UserRequest.UpdateDTO requestDTO, HttpServletRequest request) {
        // 인증 체크
        System.out.println(1);

        User sessionUser = (User) session.getAttribute("sessionUser");
//        if(sessionUser == null){
//            return "redirect:/loginForm";
//        }

        System.out.println(2);

        userRepository.update(requestDTO, id);
        System.out.println(3);

        // 권한 체크
        // 모델 위임(id로 board를 조회)
        // 조인, 서브쿼리, 오더바이를 사용하면 서버의 부하가 높아진다.
        User user = userRepository.findById(sessionUser.getId());


        System.out.println(user);

        if (sessionUser.getId() != user.getId()) {
            request.setAttribute("status", 403);
            request.setAttribute("msg", "게시글을 수정할 권한이 없습니다");
            return "error/40x"; // 리다이렉트 하면 데이터 사라지니까 하면 안됨
        }

        // 가방에 담기
        session.setAttribute("sessionUser", user);
        sessionUser = user;
        System.out.println(sessionUser);

        return "redirect:/";
    }


    // 왜 조회인데 post임? 민간함 정보는 body로 보낸다.
    // 로그인만 예외로 select인데 post 사용
    // select * from user_tb where username=? and password=?
    @PostMapping("/login")
    public String login(UserRequest.LoginDTO requestDTO){


        System.out.println(requestDTO); // toString -> @Data

        if(requestDTO.getUsername().length() < 3){
            return "error/400"; // ViewResolver 설정이 되어 있음. (앞 경로, 뒤 경로)
        }

        User user = userRepository.findByUsernameAndPassword(requestDTO);

        if(user == null){ // 조회 안됨 (401)
            return "error/401";
        }else{ // 조회 됐음 (인증됨)
            session.setAttribute("sessionUser", user); // 락카에 담음 (StateFul)
        }

        return "redirect:/"; // 컨트롤러가 존재하면 무조건 redirect 외우기
    }

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO requestDTO){
        System.out.println(requestDTO);

        userRepository.save(requestDTO); // 모델에 위임하기
        return "redirect:/loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }



    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }


}
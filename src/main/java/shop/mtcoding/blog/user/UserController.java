package shop.mtcoding.blog.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.mtcoding.blog._core.util.Script;


@RequiredArgsConstructor // final이 붙은 애들에 대한 생성자를 만들어줌
@Controller
public class UserController {

    // 자바는 final 변수는 반드시 초기화가 되어야함.
    private final UserRepository userRepository;
    private final HttpSession session;

    // 왜 조회인데 post임? 민간함 정보는 body로 보낸다.
    // 로그인만 예외로 select인데 post 사용
    // select * from user_tb where username=? and password=?
    @PostMapping("/login")
    public String login(UserRequest.LoginDTO requestDTO) {

        System.out.println(requestDTO); // toString -> @Data

        if (requestDTO.getUsername().length() < 3) {
            //return "error/400"; // ViewResolver 설정이 되어 있음. (앞 경로, 뒤 경로)
            throw new RuntimeException("유저네임 길이가 너무 짧아요");
        }

        //
        User user = userRepository.findByUsername(requestDTO.getUsername());

        // 패스워드 검증 필요
        // ! 쓰지 않으면 비정상일때는 else 해야하는데 별로 좋지 않은 코드 필터링 하듯 코드 짜면 가독성이 좋아짐(if 들어가지 않는 로직은 정상임)
        // 분기 로직을 줄일 수 있음
        if (!BCrypt.checkpw(requestDTO.getPassword(), user.getPassword())) { // 역치환해서 스로우로 날림
            throw new RuntimeException("패스워드가 틀렸습니다.");
        }
        session.setAttribute("sessionUser", user); // 락카에 담음 (StateFul)


        return "redirect:/"; // 컨트롤러가 존재하면 무조건 redirect 외우기
    }

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO requestDTO) {
        // @ResponseBody 붙이면 메세지 자체를 리턴함
        // return "error/400"; 이부분 메세지를 리턴
        System.out.println(requestDTO);

        String rawPassword = requestDTO.getPassword();
        String encPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt()); // 인수가 2개 필요, 레인보우 테이블에 털리지 않음

        requestDTO.setPassword(encPassword);


        try {
            userRepository.save(requestDTO); // 모델에 위임하기
        }catch (Exception e){
            throw  new RuntimeException("아이디가 중복 되었어요");
        }
        
        return "redirect:/loginForm";
       // return Script.href("loginForm"); // 스크립트로 제어
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm() {
        return "user/updateForm";
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }
}
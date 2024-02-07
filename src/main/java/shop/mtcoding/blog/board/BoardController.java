package shop.mtcoding.blog.board;

import com.fasterxml.jackson.annotation.JacksonInject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final HttpSession session;
    private final BoardRepository boardRepository;

    @GetMapping("/board/{id}/updateForm")
    public String updateFrom(@PathVariable int id, HttpServletRequest request){
        // 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null){
            return "redirect:/loginForm";
        }

        // 권한 체크
        // 모델 위임(id로 board를 조회)
        Board board = boardRepository.findById(id);
        if(board.getUserId() != sessionUser.getId()){
            return "error/403";
        }

        // 가방에 담기
        request.setAttribute("board", board);



        // 조인, 서브쿼리, 오더바이를 사용하면 서버의 부하가 높아진다.
        return "board/updateForm";
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable int id, HttpServletRequest request){

        // 1. 인증체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null){ // 401
            return "redirect:/loginForm";
        }

//            request.setAttribute("status", 401);
//            request.setAttribute("msg", "인증 되지 않았습니다.");
//            return "error/40x";

        // 2. 권한 체크 (권한 없으면 나가)
        // 게시물 존재 여부 확인 (조회 한번 하고 삭제 해야 됨)
            Board board = boardRepository.findById(id);

            if(board.getUserId() != sessionUser.getId()){
                request.setAttribute("status", 403);
                request.setAttribute("msg", "게시글을 삭제할 권한이 없습니다");
                return "error/40x";
            }
            boardRepository.deleteById(id);

        return "redirect:/";
    }

    @GetMapping({ "/", "/board" })
    public String index(HttpServletRequest request) {

        List<Board> boardList = boardRepository.findAll();
        request.setAttribute("boardList", boardList);

        return "index";
    }

    @PostMapping("/board/save")
        public String save(BoardRequest.SaveDTO requestDTO, HttpServletRequest request){
        // 1. 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 2. 바디 데이터 확인 및 유효성 검사
        System.out.println(requestDTO);

        if (requestDTO.getTitle().length() > 30) {
            request.setAttribute("ststus", 400);
            request.setAttribute("msg", "title의 길이가 30자를 초과해서는 안되요");
            return  "erro/400"; // BodyRequest
        }
        // 3. 모델 위임
        // insert into board_tb (title, content, user_id, created_at) values (?,?,?, now());
        boardRepository.save(requestDTO, sessionUser.getId());

        return "redirect:/";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {

        // session 영역에 sessionUser 키값에 user 객체 있는지 체크
        User sessionUser = (User) session.getAttribute("sessionUser"); //꺼내는 것은 get

        // 값이 null 이면 로그인 페이지로 리다이렉션
        // 값이 null 이 아니면, /board/saveForm 으로 이동
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        return "board/saveForm";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, HttpServletRequest request) {
        //System.out.println("id : "+id);

        // 1. 모델 진입 - 상세보기 데이터 가져오기
        BoardResponse.DetailDTO responseDTO = boardRepository.findByIdWithUser(id);

        // 2. 페이지 주인 여부 체크 (board의 userId와 sessionUser의 id를 비교)
        User sessionUser = (User) session.getAttribute("sessionUser");

        boolean pageOwner = false;
        if (sessionUser.getId() == responseDTO.getUserId()) {
            pageOwner = true;
        }

        request.setAttribute("board", responseDTO);
        request.setAttribute("pageOwner", pageOwner);
        return "board/detail";
    }
}










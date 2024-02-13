package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.config.security.MyLoginUser;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final HttpSession session;
    private final BoardRepository boardRepository;

    //?title=제목1@
    //title
    //파싱방법이 똑같아서 바로 받을 수 있다 String title, String content 이런식으로

    @GetMapping("/board/{id}/update")
    public String update(@PathVariable int id, BoardRequest.UpdateDTO requestDTO){
        //1. 인증체크
        User sessionUser=(User) session.getAttribute("sessionUser");
//        if(sessionUser==null){
//            return "redirect:/loginForm";
//        }
        //2. 권한체크
        Board board = boardRepository.findById(id);

        if(board.getUserId()!=sessionUser.getId()){
            return "error/403";
        }
        //3. 핵심로직
        //update board_tb set title = ?, content = ? where id = ?;
        boardRepository.update(requestDTO, id);

        return "redirect:/board/{id}";
    }
    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id, HttpServletRequest request){
        //인증체크
        User sessionUser=(User) session.getAttribute("sessionUser");
//        if(sessionUser==null){
//            return "redirect:/loginForm";
//        }
        //권한 체크
        Board board = boardRepository.findById(id);
        if(board.getUserId()!=sessionUser.getId()){
            return "error/403";
        }
        //모델 위임(id로 board를 조회)
        request.setAttribute("board", board);

        return "board/updateForm";
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable int id, HttpServletRequest request){
        //1. 인증 안되면 나가
        User sessionUser=(User) session.getAttribute("sessionUser");
//        if(sessionUser==null){
//            return "redirect:/loginForm";
//        }

        //2. 권한 없으면 나가
        Board board = boardRepository.findById(id);
        if(board.getUserId() != sessionUser.getId()){
            request.setAttribute("status", 403);
            request.setAttribute("msg", "ddd");
            return "error/40x";
        }

        boardRepository.delete(id);

        return "redirect:/";
    }

    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO requestDTO, HttpServletRequest request){
        //1. 인증체크
        User sessionUser = (User) session.getAttribute("sessionUser");
//        if(sessionUser == null){
//            return "redirect:/loginform";
//        }

        //2. 바디데이터를 확인 및 유효성 검사
        System.out.println(requestDTO);


        if (requestDTO.getTitle().length()>30){
            request.setAttribute("status", 400);
            request.setAttribute("msg", "title의 길이가 30자를 초과하면 안돼요");
            return "error/40x"; //badrequest
        }

        //insert into board.tb(title, content, user_id, created_at) values(?, ?, ?, now());
        boardRepository.save(requestDTO, sessionUser.getId());
        return "redirect:/";
    }

    @GetMapping("/board/saveForm")
    public String saveForm( HttpServletRequest request) {
        //jsession 영역에 sessionUser 키값에 user 객체 있는지 체크
        User sessionUser = (User) session.getAttribute("sessionUser");


        //값이 null이면 로그인 페이지로 리다이렉션
        if(sessionUser == null){
            return "redirect:/loginForm";
        }

        //값이 null이 아니면 /board/saverForm으로 이동
        return "board/saveForm";
    }
    @GetMapping("/board/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }
    @GetMapping({"/" })
    public String index(HttpServletRequest request, @AuthenticationPrincipal MyLoginUser myLoginUser) {
        System.out.println("로그인 되었나? : "+myLoginUser.getUsername());

        List<Board> boardList = boardRepository.findAll();
        request.setAttribute("boardList", boardList);

        return "index";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, HttpServletRequest request) {

        //1. 모델 진입 - 상세보기 데이터 가져오기
        BoardResponse.DetailDTO responseDTO = boardRepository.findByIdWithUser(id);

        //2. 페이지 주인 여부 체크 (board와 userId와 sessionUser의 id를 비교)
        User sessionUser = (User) session.getAttribute("sessionUser");
        boolean pageOwner =false;
        if(sessionUser == null){
            pageOwner = false;
        }else{
            int 게시글작성자번호 = responseDTO.getUserId();
            int 로그인한사람의번호 = sessionUser.getId();
            pageOwner = 게시글작성자번호 == 로그인한사람의번호;
        }


        request.setAttribute("board", responseDTO);
        request.setAttribute("pageOwner", pageOwner);
        return "board/detail";
    }
}

package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog.reply.ReplyRepository;
import shop.mtcoding.blog.user.User;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final HttpSession session;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    // ?title=제목1&content=내용1
    // title=제목1&content=내용1
    @PostMapping("/board/{id}/update")
    public String update(@PathVariable int id, BoardRequest.UpdateDTO requestDTO) {
        // 1. 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 2. 권한 체크
        Board board = boardRepository.findById(id);
        if (board.getUserId() != sessionUser.getId()) {
            return "error/403";
        }

        // 3. 핵심 로직
        // update board_tb set title = ?, content = ? where id = ?;
        boardRepository.update(requestDTO, id);

        return "redirect:/board/" + id;
    }


    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id, HttpServletRequest request) {
        // 1. 인증 안되면 나가
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 2. 권한 없으면 나가
        // 모델 위임 (id로 board를 조회)
        Board board = boardRepository.findById(id);

        if (board.getUserId() != sessionUser.getId()) {
            return "error/403";
        }

        // 3. 가방에 담기
        request.setAttribute("board", board);

        return "board/updateForm";
    }


    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable int id, HttpServletRequest request) {
        // 1. 인증 안되면 나가
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) { // 401
            return "redirect:/loginForm";
        }

        // 2. 권한 없으면 나가
        Board board = boardRepository.findById(id);


        if (board.getUserId() != sessionUser.getId()) {
            request.setAttribute("status", 403);
            request.setAttribute("msg", "게시글을 삭제할 권한이 없습니다");
            return "error/40x";
        }

        boardRepository.deleteById(id);

        return "redirect:/";
    }


    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO requestDTO, HttpServletRequest request) {
        // 1. 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 2. 바디 데이터 확인 및 유효성 검사
        System.out.println(requestDTO);

        if (requestDTO.getTitle().length() > 30) {
            request.setAttribute("status", 400);
            request.setAttribute("msg", "title의 길이가 30자를 초과해서는 안되요");
            return "error/40x"; // BadRequest
        }

        // 3. 모델 위임
        // insert into board_tb(title, content, user_id, created_at) values(?,?,?, now());
        boardRepository.save(requestDTO, sessionUser.getId());

        return "redirect:/";
    }

    // localhost:8080?page=0
    // localhost:8080 -> page 값이 0
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        @RequestParam(value = "page", defaultValue = "0") Integer page, // @RequestParam 안쓰면 int 뒤에 이름 맞춰야 함
                        @RequestParam(defaultValue = "") String keyword) {

        // isEmpty() -> null, 공백
        // isBlank() -> null, 공백, 화이트 스페이스
        if (keyword.isBlank()) {
            List<Board> boardList = boardRepository.findAll(page);

            // 전체 페이지 개수
            int count = boardRepository.count().intValue();
            // 5 -> 2page
            // 6 -> 2page
            // 7 -> 3page
            // 8 -> 3page
            int namerge = count % 3 == 0 ? 0 : 1;
            int allPageCount = count / 3 + namerge;

            // boardList 다 넣어서 가져가는게 좋지만 지금은 board 객체라서(모든 뷰쪽으로 응답하는 것을 엔티티가 되어서는 안된다.)
            // 원래는 boardList에 한번에 담아서 가져오는 것이 맞음 (모든 view로 보내는 ENTITY는 다 DTO로 바꿔서 보내야)
            request.setAttribute("boardList", boardList);

            // 페이징의 핵심 변수
            request.setAttribute("first", page == 0);
            request.setAttribute("last", allPageCount == page + 1); // 현재 페이지가 첫 페이지인지 마지막 페이지인지 확인하기 위함
            request.setAttribute("prev", page - 1);
            request.setAttribute("next", page + 1);
            request.setAttribute("keyword", "");

        } else {
            List<Board> boardList = boardRepository.findAll(page, keyword);

            int count = boardRepository.count(keyword).intValue();

            // 5 -> 2page
            // 6 -> 2page
            // 7 -> 3page
            // 8 -> 3page
            int namerge = count % 3 == 0 ? 0 : 1;
            int allPageCount = count / 3 + namerge;

            // boardList 다 넣어서 가져가는게 좋지만 지금은 board 객체라서(모든 뷰쪽으로 응답하는 것을 엔티티가 되어서는 안된다.)
            // 원래는 boardList에 한번에 담아서 가져오는 것이 맞음 (모든 view로 보내는 ENTITY는 다 DTO로 바꿔서 보내야)
            request.setAttribute("boardList", boardList);

            // 페이징의 핵심 변수
            request.setAttribute("first", page == 0);
            request.setAttribute("last", allPageCount == page + 1);
            request.setAttribute("prev", page - 1);
            request.setAttribute("next", page + 1);
            request.setAttribute("keyword", keyword);
        }

        // 에러 -> 자바스크립트응답

        return "index";
    }

    //   /board/saveForm 요청(Get)이 온다
    @GetMapping("/board/saveForm")
    public String saveForm() {

        //   session 영역에 sessionUser 키값에 user 객체 있는지 체크
        User sessionUser = (User) session.getAttribute("sessionUser");

        //   값이 null 이면 로그인 페이지로 리다이렉션
        //   값이 null 이 아니면, /board/saveForm 으로 이동
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        return "board/saveForm";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, HttpServletRequest request) {
        // 2. 페이지 주인 여부 체크 (board의 userId와 sessionUser의 id를 비교)
        User sessionUser = (User) session.getAttribute("sessionUser");

        // 1. 모델 진입 - 상세보기 데이터 가져오기
        BoardResponse.DetailDTO boardDTO = boardRepository.findByIdWithUser(id);
        boardDTO.isBoardOwner(sessionUser);

        List<BoardResponse.ReplyDTO> replyDTOList = replyRepository.findByBoardId(id, sessionUser);

        request.setAttribute("board", boardDTO);
        request.setAttribute("replyList", replyDTOList);

        return "board/detail";



//        boolean pageOwner;
//        if (sessionUser == null) {
//            pageOwner = false;
//        } else {
//            int 게시글작성자번호 = responseDTO.getUserId();
//            int 로그인한사람의번호 = sessionUser.getId();
//            pageOwner = 게시글작성자번호 == 로그인한사람의번호;
//        }
//



    }
}
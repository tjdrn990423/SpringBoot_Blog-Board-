package iducs.springboot.kskboard.controller;

import iducs.springboot.kskboard.domain.BoardDTO;
import iducs.springboot.kskboard.domain.MemberDTO;
import iducs.springboot.kskboard.domain.PageRequestDTO;
import iducs.springboot.kskboard.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/boards")
public class BoardController {
    // 생성자 주입 : Spring Framework <- Autowired (필드 주입)
    private final BoardService boardService;
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/regform")
    public String getRegform(PageRequestDTO pageRequestDTO, Model model){
        model.addAttribute("dto", BoardDTO.builder().build()); // 빈 Board 객체 생성
        return "/boards/regform"; // boards/regform.html 전달
    }

    @PostMapping("")
    public String post(@ModelAttribute("dto") BoardDTO dto, Model model) {
        // Login 처리하면 그냥 관계 없음
        /*
        Long seqLong = Long.valueOf(new Random().nextInt(50));
        seqLong = (seqLong == 0)? 1L:seqLong;
        dto.setWriterSeq(seqLong);
         */
        Long bno = boardService.register(dto);
        return "redirect:/boards/" + bno; //+ dto.getBno(); // 등록 후 상세보기
    }

    @GetMapping("")
    public String getList(PageRequestDTO pageRequestDTO, Model model){
        model.addAttribute("list", boardService.getList(pageRequestDTO));
        return "/boards/list"; // boards/list.html 전달
    }


    //조회
    @GetMapping("/{bno}")
    public String getBoard(@PathVariable("bno") Long bno, Model model) {

        BoardDTO board = boardService.getById(bno);
        boardService.updateView(bno);
        model.addAttribute("dto", board);

        return "/boards/read";
    }

/*
    @GetMapping("/{bno}/upform") //업데이트폼
    public String getUpform(@PathVariable("bno") Long bno, Model model){
        BoardDTO boardDTO = boardService.getById(bno);
        model.addAttribute("board", boardDTO); //입력한 객체를 전달, DB로부터 가져온 것 아님
        return "/boards/upform"; //view resolving : upform.html

    }

    @PutMapping("/{bno}") //업데이트 구현
    public String putMember(@ModelAttribute("board") Long bno, Model model){
        // html에서 model 객체를 전달 받음 : memberDTO (애드트리뷰트 명으로 접근, th:object 애트리뷰트 값)
        boardService.modifyById(bno);
        model.addAttribute(bno);
        return "redirect:/boards/read";

    }

*/


    @GetMapping("/{bno}/upform") //업데이트폼
    public String getUpform(@PathVariable("bno") Long bno, Model model){
        BoardDTO boardDTO = boardService.getById(bno);
        model.addAttribute("board", boardDTO); //입력한 객체를 전달, DB로부터 가져온 것 아님
        return "/boards/upform"; //view resolving : upform.html

    }

    @PutMapping("/{bno}") //업데이트 구현
    public String putMember(@PathVariable Long bno, BoardDTO boardDTO){
        // html에서 model 객체를 전달 받음 : memberDTO (애드트리뷰트 명으로 접근, th:object 애트리뷰트 값)
        boardService.update(bno, boardDTO);
        //return "redirect:/boards/read";
        return "redirect:/boards";
    }

    @GetMapping("/{bno}/delform") //삭제폼
    public String getDelform(@ModelAttribute("bno") Long bno, Model model){
        // html에서 model 객체를 전달 받음 : memberDTO (애드트리뷰트 명으로 접근, th:object 애트리뷰트 값)
        BoardDTO boardDTO = boardService.getById(bno);
        model.addAttribute("board", boardDTO);
        //return "members/delform";
        return "/boards/delform";

    }
    @DeleteMapping("/{bno}") //삭제 구현
    public String deleteMember(@PathVariable Long bno){
        boardService.deleteByBno(bno);
        return "redirect:/boards"; //'/members' 요청을 함,
    }



}

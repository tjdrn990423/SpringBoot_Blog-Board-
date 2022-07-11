package iducs.springboot.kskboard.controller;

import iducs.springboot.kskboard.config.CheckEmailValidator;
import iducs.springboot.kskboard.domain.MemberDTO;
import iducs.springboot.kskboard.domain.PageRequestDTO;
import iducs.springboot.kskboard.entity.MemberEntity;
import iducs.springboot.kskboard.service.BoardService;
import iducs.springboot.kskboard.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

/*
Restful API
Rest 방식의 활용을 위한 API

Open API
공개된 API : Rest 방식을 활용

Domain : 업무 중심
    DTO(Data Transfer object) - 객체
    client -> dto -> Controler -> dto -> Service

Service : dto -> entity, entity -> dto

Entity : 데이터 중심
    Entity, 개체 - table, record, field
    Service -> entity -> repository - jpa(hibernate) -> dbms


    SpringDataJPA는 쿼리 몰라두됌(쿼리가 제한적)

    SpringDataJDBC
 */
@Controller
@RequestMapping("/members")
public class MemberController {
    //원래 프로젝트에서는 @RestController or @ Controller 하나 만 사용
    // thymeleaf or jsp 도 한 프로젝트에 하나 만사용

    private final MemberService     memberService;
    private final CheckEmailValidator checkEmailValidator;
    //생성자 주입 : (Constructor Injection) vs. @Autowired
    public MemberController(MemberService memberService, CheckEmailValidator checkEmailValidator){
        this.memberService = memberService;
        this.checkEmailValidator = checkEmailValidator;
    }

    @InitBinder // 커스텀 검증을 위해 추가
    public void validatorBinder(WebDataBinder binder){
        binder.addValidators(checkEmailValidator);
    }

    @GetMapping("/regform") //등록폼
    public String getRegform(Model model){
        //정보를 전달받을 객체를 보냄
        model.addAttribute("member", MemberDTO.builder().build());
        //new MemberDTO() domain의 MemberDTO @Bullder 사용하지 않았을 때
        //MemoDTO.builder().build() domain의 @Bullder사용했을 때
        //return "/members/regform";
        return "/members/regform";
    }

    @PostMapping("") //등록 구현
    public String postMember(@Validated MemberDTO memberDTO, Errors errors, Model model){
        if(errors.hasErrors()){
            model.addAttribute("member",memberDTO);
            Map<String,String> validatorResult = memberService.validateHandling(errors);
            for(String key : validatorResult.keySet()){
                model.addAttribute(key,validatorResult.get(key));
            }
            return "/members/regform";
        }
        memberService.create(memberDTO);
        return "redirect:/";
    }
    /*
    @PostMapping("") //등록 구현
    public String postMember(@ModelAttribute("member") MemberDTO memberDTO, Model model){
        memberService.create(memberDTO);
        //model.addAttribute("memberDTO", memberDTO); //입력한 객체를 전달, DB로부터 가져온 것 아님
        //return "/members/memberDTO";
        //return "/members/"+ memberDTO.getClass() +"/upform";

        //return "redirect:/members";
        return "redirect:/";
    }

     */

    @GetMapping("/{idx}")        //readOne 1개 정보 가져오기
    public String getMember(@PathVariable("idx") Long seq, Model model){
        MemberDTO memberDTO = memberService.readById(seq);
        model.addAttribute("member", memberDTO); //입력한 객체를 전달, DB로부터 가져온 것 아님
        //return "/members/memberDTO"; //view resolving : memberDTO.html
        return "/pages/examples/contacts";
    }

    //http://localhost:8888/members?page=1&size=10 이런것도가능
    @GetMapping("")        //전체 정보
    //public String getMembers(Model model)
    public String getMembers(PageRequestDTO pageRequestDTO, Model model){ // 1과 10이 넘어옴(PageResultDTO)
        //정보를 전달받을 빈(empty) 객체를 보냄
        //List<MemberDTO> members = memberService.readAll();
        //model.addAttribute("list", members);
        model.addAttribute("list", memberService.readListBy(pageRequestDTO));
        //return "/members/members"; //view resolving : members.html
        //return "/pages/tables/simple";
        return "/members/members";
    }

    @GetMapping("/{idx}/upform") //업데이트폼
    public String getUpform(@PathVariable("idx") Long seq, Model model){
        MemberDTO memberDTO = memberService.readById(seq);
        model.addAttribute("member", memberDTO); //입력한 객체를 전달, DB로부터 가져온 것 아님
        //return "/members/upform";
        return "/members/upform"; //view resolving : upform.html

    }

    @PutMapping("/{idx}") //업데이트 구현
    public String putMember(@ModelAttribute("member") MemberDTO memberDTO, Model model){
        // html에서 model 객체를 전달 받음 : memberDTO (애드트리뷰트 명으로 접근, th:object 애트리뷰트 값)
        memberService.update(memberDTO);
        model.addAttribute(memberDTO);
        //return "/members/memberDTO"; //view resolving : upform.html : updated info 확인
        return "/pages/examples/contacts";
    }

    @GetMapping("/{idx}/delform") //삭제폼
    public String getDelform(@ModelAttribute("idx") Long seq, Model model){
        // html에서 model 객체를 전달 받음 : memberDTO (애드트리뷰트 명으로 접근, th:object 애트리뷰트 값)
        MemberDTO memberDTO = memberService.readById(seq);
        model.addAttribute("member", memberDTO);
        //return "members/delform";
        return "/members/delform";

    }

    @DeleteMapping("/{idx}") //삭제 구현
    public String deleteMember(@PathVariable Long idx, HttpSession session) {
        if (session.getAttribute("isadmin") != null) {
            memberService.removeWithBoards(memberService.readById(idx).getSeq());
            memberService.delete(memberService.readById(idx));
            return "redirect:/members/";
        } else if (((MemberDTO) session.getAttribute("login")).getSeq() == idx) {
            memberService.removeWithBoards(memberService.readById(idx).getSeq());
            memberService.delete(memberService.readById(idx));
        }

        session.invalidate();
        return "redirect:/";
    }



    @GetMapping("/login")
    public String getLoginform(Model model){
        model.addAttribute("member", MemberDTO.builder().build());
        return "/members/login";
    }
    @PostMapping("/login")
    public String postLogin(@ModelAttribute("member") MemberDTO member, HttpServletRequest request){
        MemberDTO dto = null;
        if((dto = memberService.loginByEmail(member)) != null){
            HttpSession session = request.getSession();
            session.setAttribute("login",dto);
            session.setAttribute("block",dto.getBlock());
            if(dto.getId().contains("admin"))
                session.setAttribute("isadmin", dto.getId());
            return "redirect:/";
        }
        else
            return "/members/loginfail";
    }
    @GetMapping("/logout")
    public String getLogout(HttpSession session){
        session.invalidate();
        return "redirect:/"; //view resolving
    }


/*
    @GetMapping("/jsp")
    public String getJSP(){
        return "jsp-page";
    }

    @GetMapping("buttons")
    public String getButtons(){
        return "buttons";
    }

    @GetMapping("/")
    public String getSbadmin(){
        return "index";
    }

    //@GetMapping("/home?servicekey=value&key1=value1")이런것도 가능
    @GetMapping("/home")
    public String getHome(Model model){
        System.out.println("this");
        ArrayList<String> names = new ArrayList<>();
        names.add("induk");
        names.add("comso");
        names.add("sunkoo");
        model.addAttribute("list",names); //return에 넘겨주는 객체(model)
        return "home1";
    }

    @GetMapping("/cards")
    public String getCards(){
        return "cards";
    }
    @GetMapping("/layout")
    public String getLayout(){
        return "layout";
    }
    @GetMapping("/tables")
    public String getTables(){
        return "tables";
    }
    /*
    /*
    @GetMapping("/")
    public String getHome(Model model){
        ArrayList<String> names = new ArrayList<>();
        names.add("induk");
        names.add("comso");
        names.add("sunkoo");
        model.addAttribute("list",names);
        return "index";
    }
    */
    /*
        DBMS 확인
            MariaDB 10.7.3
            HeidiSQL로 Session 관리
        DB(데이터베이스):조직화된 정보(자료구조)
        DBMS:DB를 관리하는 프로그램

        설정하는 패키지:config(controller나 다른 패키지 설정)

        resources/static/
        정적 자원의 저장 공간
        resouces/templates/
        동적 자원의 저장 공간

        temlpates과 static은 레벨이 같아 css , js 는 경로가 같음 ex) ../static/ 이거 지워버리면 같아짐

        주의: 디렉토리에 html이 있어도 controller를 통해 mapping해줘야 사이트가 나온다.
     */
    /*
        @Controller,@RestController을 통해 요청함 그 후 포함되어 있는 메소드를 맵핑해서 보냄
     */
    /*
     방명록 애플리케이션
            기능,URL,Get/POST, 설명
            목록조회, /guestbook/list, GET, 목록/검색 정렬
            등록, /guestbook/register, GET, 등록화면
                /guestbook/register, POST, 등록
           ㅡㅡㅡㅡ 위에는 옛날 아래는 이런식으로 함  ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
            /guestbook/  --- 방명록 전체 조회
            /guestbook/1 , get : 방명록 1번
            /guestbook/1, post : 방명록 1번 등록
            /guestbook/1, put : 방명록 1번 수정
            /guestbook/1, delete : 방명록 1번 삭제
     */
}

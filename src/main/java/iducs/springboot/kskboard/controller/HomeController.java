package iducs.springboot.kskboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//localhost:8888/index.html 을 치면 원래 안나와야하는데 나오면 static밑에 html을 불러오는거고
//localhost:8888/index 는  template에 있는 것을 호출(controller)
@Controller //controller는 어노테이션 사용
//컨트롤로 어노테이션은 2가지가 있다.
// @Controller : 데이터(처리결과)를 View에게 전달
// @RestController  자원의 상태(응답)를 Client에게 전달,
// OpenAPIs, Restful(Representational State Transfer) 데이터를 전달하는것밖에 못함
@RequestMapping("/")
public class HomeController {

    @GetMapping("")
    public String getHome(){
        return "index";
    }
    @GetMapping("index2")
    public String getIndex2(){
        return "index2"; //View name
    }
    @GetMapping("index3")
    public String getIndex3(){
        return "index3";
    }

    @GetMapping("/pages/tables/simple")
    public String getSimpleTables(){
        return "/pages/tables/simple";
    }

    @GetMapping("/pages/examples/contacts")
    public String getContacts(){
        return "/pages/examples/contacts";
    }

    @GetMapping("create")
    public String getCreate(){
        return "create";
    }

    @GetMapping("upform")
    public String getUpform(){
        return "upfrom";
    }

}

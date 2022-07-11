package iducs.springboot.kskboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@SpringBootApplication
@EnableJpaAuditing
public class Board201812045Application {

    public static void main(String[] args) {
        SpringApplication.run(Board201812045Application.class, args);
    }

    @Bean //메소드를 호출하여 bean 객체 생성
    public HiddenHttpMethodFilter hiddenHttpMethodFilter(){ // put , delete 이게 없으면 get,post만 가능
        return new HiddenHttpMethodFilter();
    }
    //put, delete 사용가능하게 해줌  이게 없으면 받는쪽(Spring)에서 제대로 작동안함

}

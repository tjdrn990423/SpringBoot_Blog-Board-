package iducs.springboot.kskboard;

import iducs.springboot.kskboard.entity.MemberEntity;
import iducs.springboot.kskboard.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.stream.IntStream;

@SpringBootTest
class Board201812045ApplicationTests {
    // POJO (Plain Old Java Object) : 가장 기본적인 자바 객체 형태 - 필드, getter, setter
    // Beans 규약을 준수 , 생성자가 복잡하지 않음
    //DI (Dependency Injection) : 의존성 주입, Spring Framework 가 의존성을 해결하는 방법
    @Autowired
    MemberRepository memberRepository; // MemoRepository 클래스 형 객체를 Spring 생성

    //데이터를 만들기위한 테스트
    @Test
    void testMemberInitialize() {
        // Integer 데이터 흐름 , Lambda 식 - 함수형 언어의 특징을 활용
        //IntStream.rangeClosed(1,100).forEach 반복문 (1부터 100까지 반복)
        //range: 1~99까지 끝자리 미포함  rangeClosed:1~100까지 끝자리 포함
        IntStream.rangeClosed(1,50).forEach(i->{  //<-----람다식      1~50번 반복 1->induk comso1 ~~~~ 50 -> induk comso50
            MemberEntity entity = MemberEntity.builder()
                    .id("id" + i) //builder= 생성자?
                    .pw("pw" + i)
                    .name("name" + i)
                    .email("email" + new Random().nextInt(100)+"@induk.ac.kr")
                    .phone("phone" + new Random().nextInt(50)) //max
                    .address("address" + i)
                    .block("unblock")
                    .level("2")
                    .build();
            memberRepository.save(entity);  //repository의 save기능

        });
    }
}

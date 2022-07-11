package iducs.springboot.kskboard;

import iducs.springboot.kskboard.entity.MemberEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
class MemberTests {
    // POJO (Plain Old Java Object) : 가장 기본적인 자바 객체 형태 - 필드, getter, setter
    // Beans 규약을 준수 , 생성자가 복잡하지 않음
    //DI (Dependency Injection) : 의존성 주입, Spring Framework 가 의존성을 해결하는 방법

     // MemoRepository 클래스 형 객체를 Spring 생성
    @Test
    void contextLoads() {
        // Integer 데이터 흐름 , Lambda 식 - 함수형 언어의 특징을 활용
        //IntStream.rangeClosed(1,100).forEach 반복문 (1부터 100까지 반복)
        //range: 1~99까지 끝자리 미포함  rangeClosed:1~100까지 끝자리 포함
        IntStream.rangeClosed(1,50).forEach(i->{  //1~100번 반복 1->induk comso1 ~~~~ 100 -> induk comso100
            MemberEntity memo = MemberEntity.builder()
                    .id("id-" + i)
                    .pw("pw-" + i)
                    .name("name-" + i)
                    .email("email" + i + "induk.ac.kr")
                    .build(); //builer를 통해 객체를 쉽게 만들수 있다
            //보일러플레이트코드 : 프로그래밍 언어에 사용구 코드를지 지칭하는 말로, 예를 들어 getter, setter 메서드가 있다.
                    //이런 메서드는 꼭 필요하지만 코드의 길이가 길어지게 한다는 단점이 있다.
            //Lombok의 장점 :재사용 구문 좀 더 손쉽게 만들 수 있다.
            //memoRepository.save(memo);
        });
    }

}

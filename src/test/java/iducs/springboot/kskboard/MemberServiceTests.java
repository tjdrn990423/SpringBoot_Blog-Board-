package iducs.springboot.kskboard;


import iducs.springboot.kskboard.entity.MemberEntity;
import iducs.springboot.kskboard.repository.MemberRepository;
import iducs.springboot.kskboard.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberServiceTests {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testAdmin(){
        // Integer 데이터 흐름, Lamda식 - 함수형 언어의 특징을 활용
        String str = "admin";
        MemberEntity entity = MemberEntity.builder()
                .id(str)
                .pw(str)
                .name("name-" + str )
                .email(str + "@induk.ac.kr")
                .phone("phone-" + new Random().nextInt(50))
                .address("address-" + new Random().nextInt(50))
                .block("unblock")
                .level("1")
                .build();
        memberRepository.save(entity);
    }


}

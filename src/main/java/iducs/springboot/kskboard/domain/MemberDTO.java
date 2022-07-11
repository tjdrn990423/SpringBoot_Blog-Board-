package iducs.springboot.kskboard.domain;

import lombok.Builder;
import lombok.Data;

@Data //@Getter, @Setter, @EqualsAndHash, @RequiredArgsConstructor
@Builder
//MemberDTO : 자바객체에서 사용하는것 , 객체
public class MemberDTO { //DTO(Data Transfer Object):
                    // Client <-> Controller <-> Service 사이에서
    private Long seq; // 시퀀스 번호, 자동 증가하는 유일키
    private String id;
    private String pw;
    private String name;
    private String email;
    private String phone;
    private String address;

    private String block = "unblock";

    private String level;
}

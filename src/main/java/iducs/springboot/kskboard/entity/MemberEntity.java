package iducs.springboot.kskboard.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// Spring Data 관련 Annotations (JPA Annotations)
// JPA = ORM (오브젝트 리 맵핑)
@Entity // Spring Data JPA의 엔티티(entity,객체)임을 나타냄
@Table(name="member201812045")
//Lombok 관련 Annotations
@ToString //toString() 생성
@Getter // getter()생성
@Setter // setter()추가
@Builder
@AllArgsConstructor //모든 매개변수를 갖는 생성자
@NoArgsConstructor //디폴트 생성자(아무런 매개변수가 없는)
//Entity : DB와 연결하기위해 사용하는 것,개체
public class MemberEntity extends BaseEntity {
    //기본키를 자동으로 생성할 때에는 @Id와 @GenerratedValue 어노테이션이 함께 사용되어야 한다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //기본키 생성을 데이터베이스에게 위임하는 방식으로 id값을 따로 할당하지 않아도 데이터베이스가 자동으로 AUTO_INCREMENT를 하여 기본키를 생성해준다.
    private Long seq;

    private String level;

    @Column(length = 30,nullable = false)   //nullable : 널생성여부
    private String id;
    @Column(length = 30,nullable = false)   //nullable : 널생성여부
    private String pw;
    @Column(length = 30,nullable = false)   //nullable : 널생성여부
    private String name;
    @Column(length = 50,nullable = false)   //nullable : 널생성여부
    private String email;
    @Column(length = 30, nullable = true)   //nullable : 널생성여부
    private String phone;
    @Column(length = 100)   //nullable : 널생성여부
    private String address;

    @OneToMany(mappedBy = "writer") //값을 수정하고 삭제 할수없음 조회만 가능
    private List<BoardEntity> boardEntities = new ArrayList<>();

    private String block = "unblock";


}

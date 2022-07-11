package iducs.springboot.kskboard.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tb_reply201812045")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board")
public class ReplyEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String text;
    private String replier;

    @ManyToOne //데이터베이스에서 join한다는 의미
    private BoardEntity board; // 연관관계 지정
}

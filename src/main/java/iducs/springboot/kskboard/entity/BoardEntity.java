package iducs.springboot.kskboard.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="board201812045")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "writer")
public class BoardEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String category;

    @Column(columnDefinition = "BIGINT(20) default 0", nullable = false)
    private Long views;

    private String title;
    private String content;

    private String block = "unblock";

    @ManyToOne(fetch = FetchType.LAZY) //지연로딩
    private MemberEntity writer; // 연관관계 지정 (조인)

}

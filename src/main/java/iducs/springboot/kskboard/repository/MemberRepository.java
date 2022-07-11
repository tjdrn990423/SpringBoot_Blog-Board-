package iducs.springboot.kskboard.repository;

import iducs.springboot.kskboard.entity.MemberEntity;
import iducs.springboot.kskboard.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<MemberEntity, Long>,
        QuerydslPredicateExecutor<MemberEntity>
{
    @Query("select m from MemberEntity m where m.email = :email and m.pw = :pw")
    Object getMemberByEmail(@Param("email") String email, @Param("pw") String pw);

    boolean existsByEmail(String email);

    //extends JpaRepository<MemberEntity , Long>만으로도 CRUD 사용 가능

    // JDBC 프로그래밍 순서
    // 사용할 자원 선언(Connection 선언, Statement 선언 , ResultSet 선언)
    //  원래는 Connection 객체 선언 및 생성 - 연결 설정 (application.properties)
    // Statement 생성 (Statement, PreparedStatement, CallableStatement)
    // ResultSet 생성 (entity - entities : records)
    // ResultSet -> DTO로 변환후 반환  / 또 다른 방법으로는 : 영향 받은 record 수를 반환
    // (List<DTO> - readList, DTO - readOne , int (row 수) - create , update, delete

    //super 예약어 : 상위 클래스로부터 생성된 객체 vs this 예약어 : 객체 자신을 가리키는 예약어
}

package iducs.springboot.kskboard.repository;


import iducs.springboot.kskboard.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity, Long>, SearchBoardRepository {
    // JPQL : Java Persistence Query Language
    @Query("select b, w from BoardEntity b left join b.writer w where b.bno =:bno")
    Object getBoardWithWriter(@Param("bno") Long bno);

    @Query(value = "select b, w , count(r) " +
            "from BoardEntity b left join b.writer w "  +
            "left join ReplyEntity r on r.board = b " +
            "group by b",
            countQuery = "select count(b) from BoardEntity b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

    @Query(value = "select b, w , count(r) " +
            "from BoardEntity b left join b.writer w "  +
            "left join ReplyEntity r on r.board = b " +
            "where b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);

    @Modifying
    @Query("update BoardEntity b set b.views = b.views + 1 where b.bno = :bno")
    int updateView(@Param("bno") Long bno);

    /*
    @Modifying
    @Query("update BoardEntity b set b.views = b.views + 1 where b.bno = :bno")
    Long updateCount(Long id);

     */

}

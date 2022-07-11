package iducs.springboot.kskboard.repository;

import iducs.springboot.kskboard.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchBoardRepository {
    BoardEntity searchBoard();

    Page<Object[]> searchPage(String type, String keyword,String categorys, Pageable pageable);
}

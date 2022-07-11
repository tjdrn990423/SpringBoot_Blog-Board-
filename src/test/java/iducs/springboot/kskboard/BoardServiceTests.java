package iducs.springboot.kskboard;

import iducs.springboot.kskboard.domain.BoardDTO;
import iducs.springboot.kskboard.domain.PageRequestDTO;
import iducs.springboot.kskboard.domain.PageResultDTO;
import iducs.springboot.kskboard.entity.BoardEntity;
import iducs.springboot.kskboard.repository.BoardRepository;
import iducs.springboot.kskboard.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardServiceTests {
    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;

    @Test
    public void testRegister() { //47개의 BoardDTO 데이터 만들기
        IntStream.rangeClosed(1, 47).forEach(i -> {
            BoardDTO dto = BoardDTO.builder()
                    .title("Test.")
                    .content("Content")
                    .block("unblock")
                    .views(0L)
                    .writerSeq(Long.valueOf("" + i))
                    .category("인문계")
                    .build();
            Long bno = boardService.register(dto);
        });
    }

    @Test
    public void testRegisterOne(){
        BoardDTO dto = BoardDTO.builder()
                .title("Title-")
                .content("Content-")
                .block("unblock")
                .writerSeq(10L)
                .build();
        Long bno = boardService.register(dto);
    }

    @Test
    public void testList(){ //리스트 확인 하는걸 출력(페이지 단위로 5개 설정되어있으니 5개씩 출력)
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setPage(3);
        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);
        for(BoardDTO dto : result.getDtoList()){
            System.out.println("ylist" + dto);
        }
    }

    @Transactional //어노테이션의 중요!!
    @Test
    public void testLazyloading(){
        Optional<BoardEntity> result = boardRepository.findById(10L);
        BoardEntity boardEntity = result.get();
        System.out.println(boardEntity.getTitle());
        System.out.println(boardEntity.getContent());
        System.out.println(boardEntity.getWriter());
    }
}

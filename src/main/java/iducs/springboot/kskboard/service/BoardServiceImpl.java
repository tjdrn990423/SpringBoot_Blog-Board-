package iducs.springboot.kskboard.service;


import iducs.springboot.kskboard.domain.BoardDTO;
import iducs.springboot.kskboard.domain.MemberDTO;
import iducs.springboot.kskboard.domain.PageRequestDTO;
import iducs.springboot.kskboard.domain.PageResultDTO;
import iducs.springboot.kskboard.entity.BoardEntity;
import iducs.springboot.kskboard.entity.MemberEntity;
import iducs.springboot.kskboard.repository.BoardRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public Long register(BoardDTO dto) { // Controller -> 객체 -> Service
        log.info(dto);
        BoardEntity boardEntity = dtoToEntity(dto);
        boardRepository.save(boardEntity);
        return boardEntity.getBno();// 게시물 번호
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        log.info(">>>>" + pageRequestDTO);

        Function<Object[], BoardDTO> fn = (entity -> entityToDto((BoardEntity) entity[0],
                (MemberEntity) entity[1],(Long) entity[2]));
        String type = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();
        String categorys = pageRequestDTO.getCategorys();
        Pageable pageable = null;
        String page = pageRequestDTO.getSort();
        String asc = "asc";
        pageable = pageRequestDTO.getPageable(Sort.by("bno").descending());
        if(page != null) {
            if(page.equals(asc)) {
                pageable = pageRequestDTO.getPageable(Sort.by("bno").ascending());
            } else if (page.equals("vdesc")){
                pageable = pageRequestDTO.getPageable(Sort.by("views").descending());
            } else if(page.equals("vasc")){
                pageable = pageRequestDTO.getPageable(Sort.by("views").ascending());
            }
        }

        Page<Object[]> result = boardRepository.searchPage(type, keyword,categorys, pageable);

        return new PageResultDTO<>(result, fn);
    }


    @Override
    public BoardDTO getById(Long bno) {
        Object result = boardRepository.getBoardByBno(bno);
        Object[] en = (Object[]) result;
        return entityToDto((BoardEntity) en[0], (MemberEntity) en[1], (Long) en[2]);
    }



    @Override
    public Long modifyById(Long bno) {
        return null;
    }

    @Override
    public void deleteById(BoardDTO dto) {
        BoardEntity entity = dtoToEntity(dto);
        boardRepository.deleteById(entity.getBno());
    }


    @Override
    public BoardDTO readById(Long seq) {
        BoardDTO board = null;
        Optional<BoardEntity> result = boardRepository.findById(seq);
        if (result.isPresent()) {
            board = entityToDto(result.get());
        }
        return board;
    }

    @Override
    public void deleteByBno(Long bno) {
        BoardEntity boardEntity = boardRepository.findById(bno).orElseThrow(()->
                new IllegalArgumentException("해당 게시글이 없습니다. bno = "+ bno));
        boardRepository.delete(boardEntity);
    }

    @Override
    public Long update(Long bno, BoardDTO boardDTO) {
        BoardEntity boardEntity = dtoToEntity(boardDTO);
        boardRepository.save(boardEntity);
        return bno;
    }

    @Override
    @Transactional
    public Long updateCount(Long bno,BoardDTO boardDTO) {
        BoardEntity boardEntity = boardRepository.findById(bno).orElseThrow((()->
                new IllegalStateException("해당 게시글이 없습니다.")));
        //boardEntity.(boardDTO.getViews());
        return bno;
    }


    private BoardDTO entityToDto(BoardEntity entity) {
        BoardDTO board = BoardDTO.builder()
                .bno(entity.getBno())
                .modDate(entity.getModDate())
                .regDate(entity.getRegDate())
                .content(entity.getContent())
                .title(entity.getTitle())
                .writerSeq(entity.getWriter().getSeq())
                .category(entity.getCategory())
                .build();
        return board;
    }
    @Transactional
    @Override
    public int updateView(Long bno) {
        return boardRepository.updateView(bno);
    }

}


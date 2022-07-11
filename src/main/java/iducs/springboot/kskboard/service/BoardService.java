package iducs.springboot.kskboard.service;

import iducs.springboot.kskboard.domain.BoardDTO;
import iducs.springboot.kskboard.domain.MemberDTO;
import iducs.springboot.kskboard.domain.PageRequestDTO;
import iducs.springboot.kskboard.domain.PageResultDTO;
import iducs.springboot.kskboard.entity.BoardEntity;
import iducs.springboot.kskboard.entity.MemberEntity;

public interface BoardService {
    Long register(BoardDTO dto);  // Board : DTO or Domain

    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);
    BoardDTO getById(Long bno);
    Long modifyById(Long bno);
    void deleteById(BoardDTO dto);
    BoardDTO readById(Long seq);

    void deleteByBno(Long bno);
    Long update(Long bno, BoardDTO boardDTO);

    Long updateCount(Long bno,BoardDTO boardDTO);

    int updateView(Long bno);



    default BoardEntity dtoToEntity(BoardDTO dto) {
        MemberEntity member = MemberEntity.builder()
                .seq(dto.getWriterSeq())
                .build();
        BoardEntity board = BoardEntity.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .block(dto.getBlock())
                .views(dto.getViews())
                .category(dto.getCategory())
                .build();
        return board;
    }
    default BoardDTO entityToDto(BoardEntity entity, MemberEntity member, Long replyCount){
        BoardDTO dto = BoardDTO.builder()
                .bno(entity.getBno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writerSeq(member.getSeq())
                .writerId(member.getId())
                .writerName(member.getName())
                .writerEmail(member.getEmail())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .replyCount(replyCount.intValue())
                .block(entity.getBlock())
                .views(entity.getViews())
                .category(entity.getCategory())
                .build();
        return dto;
    }
}
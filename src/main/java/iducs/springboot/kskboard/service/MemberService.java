package iducs.springboot.kskboard.service;


import iducs.springboot.kskboard.domain.MemberDTO;
import iducs.springboot.kskboard.domain.PageRequestDTO;
import iducs.springboot.kskboard.domain.PageResultDTO;
import iducs.springboot.kskboard.entity.MemberEntity;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface MemberService {
    void create(MemberDTO memberDTO);
    MemberDTO readById(Long seq);
    List<MemberDTO> readAll();
    PageResultDTO<MemberDTO, MemberEntity> readListBy(PageRequestDTO pageRequestDTO);
    //PageRequestDTO pageRequestDTO요청을 담고있는 객체를  PageResultDTO<MemberDTO, MemberEntity>로 반환
    void update(MemberDTO memberDTO);
    void delete(MemberDTO memberDTO);
    void removeWithBoards(Long seq);
    MemberDTO loginByEmail(MemberDTO member);
    Map<String, String> validateHandling(Errors errors);

    MemberDTO readByName(MemberDTO memberDTO);
    MemberDTO readByEmail(String member);



}

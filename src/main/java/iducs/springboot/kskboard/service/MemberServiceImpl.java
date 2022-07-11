package iducs.springboot.kskboard.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import iducs.springboot.kskboard.domain.MemberDTO;
import iducs.springboot.kskboard.domain.PageRequestDTO;
import iducs.springboot.kskboard.domain.PageResultDTO;
import iducs.springboot.kskboard.entity.BoardEntity;
import iducs.springboot.kskboard.entity.MemberEntity;
import iducs.springboot.kskboard.entity.QMemberEntity;
import iducs.springboot.kskboard.repository.BoardRepository;
import iducs.springboot.kskboard.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.*;
import java.util.function.Function;

//@Component , @Configuration , @Beans
//@Service , @Repository
@Service
public class MemberServiceImpl implements MemberService {

    final MemberRepository memberRepository;
    final BoardRepository boardRepository;
    //Injection using Constructor
    //생성자 주입
    public MemberServiceImpl(BoardRepository boardRepository, MemberRepository memberRepository){
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void create(MemberDTO memberDTO) {
        MemberEntity entity = dtoToEntity(memberDTO);
        //스트림 api사용
        //new MemberEntity(seq,id,pw,name,email,phone,address);
        memberRepository.save(entity);
    }

    private MemberEntity dtoToEntity(MemberDTO memberDTO){
        //dto를 entity로 만들어주는 메소드
        //만든이유: create find등의 메소드의 여러번(반복) 쓰기 귀찮고 편하게 쓰기 위해서
        MemberEntity entity = MemberEntity.builder()
                .seq(memberDTO.getSeq())
                .id(memberDTO.getId())
                .pw(memberDTO.getPw())
                .name(memberDTO.getName())
                .email(memberDTO.getEmail())
                .phone(memberDTO.getPhone())
                .address(memberDTO.getAddress())
                .block(memberDTO.getBlock())
                .level(memberDTO.getLevel())
                .build();
        return entity;
    }

    //Service -> Controller : entity -> dto로 변환후 반환
    private MemberDTO entityToDto(MemberEntity entity){
        //entity를 dto로 만들어주는 메소드
        //만든이유: create find등의 메소드의 여러번(반복) 쓰기 귀찮고 편하게 쓰기 위해서
        MemberDTO memberDTO = MemberDTO.builder()
                .seq(entity.getSeq())
                .id(entity.getId())
                .pw(entity.getPw())
                .name(entity.getName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .block(entity.getBlock())
                .level(entity.getLevel())
                .build();
        return memberDTO;
    }
    @Override
    public MemberDTO readById(Long seq) {
        MemberDTO memberDTO = null;
        Optional<MemberEntity> result = memberRepository.findById(seq);
        if(result.isPresent()) {
            memberDTO = entityToDto(result.get());
        }
        return memberDTO;
    }

    public PageResultDTO<MemberDTO, MemberEntity> readListBy(PageRequestDTO pageRequestDTO){

        Sort sort = Sort.by("seq").descending();
        if(pageRequestDTO.getSort() == null)
            sort = Sort.by("seq").descending();
        else if(pageRequestDTO.getSort().equals("asc"))
            sort = Sort.by("seq").ascending();

        Pageable pageable = pageRequestDTO.getPageable(sort); //descending 생성된순서?? 아닐까
        BooleanBuilder booleanBuilder = findByCondition(pageRequestDTO);
        Page<MemberEntity> result = memberRepository.findAll(booleanBuilder, pageable);
        //Page<MemberEntity> result = memberRepository.findAll(pageable);
        Function<MemberEntity, MemberDTO> fn = (entity -> entityToDto(entity));
        return new PageResultDTO<>(result, fn);
        //페이지 처리
        //조건에 맞게 뽑아오는것


    }

    private BooleanBuilder findByCondition(PageRequestDTO pageRequestDTO){
        //querydsl 설정이 있어야 BooleanBuilder 사용 가능
        String type = pageRequestDTO.getType();

        BooleanBuilder booleanBuilder = new BooleanBuilder(); // 검색 조건을 만들어주는 놈
        //BooleanExpression  :  검색 조건 추가

        QMemberEntity qMemerEntity = QMemberEntity.memberEntity;

        BooleanExpression expression = qMemerEntity.seq.gt(0L); // where seq > 0 and title == "title"
        booleanBuilder.and(expression);

        if(type == null || type.trim().length() == 0){
            return booleanBuilder;
        }


        String keyword = pageRequestDTO.getKeyword();

        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if(type.contains("e")){ // email로 검색
            //http://localhost:8888/members?type=e&keyword=9     이메일이 9가있는것만 나옴
            conditionBuilder.or(qMemerEntity.email.contains(keyword));
        }
        if(type.contains("p")){ //phone 검색
            conditionBuilder.or(qMemerEntity.phone.contains(keyword));
        }
        if(type.contains("a")){ //adress로 검색
            conditionBuilder.or(qMemerEntity.address.contains(keyword));
        }
        if(type.contains("l")){
            conditionBuilder.or(qMemerEntity.level.contains(keyword));
        }



        booleanBuilder.and(conditionBuilder);
        return booleanBuilder;

    }



    @Override
    public List<MemberDTO> readAll() {

        List<MemberDTO> memberDTOS = new ArrayList<MemberDTO>(); // 반환 리스트 객체
        List<MemberEntity> entities = memberRepository.findAll(); // entity들
        for(MemberEntity entity : entities){
            MemberDTO memberDTO = entityToDto(entity);
            memberDTOS.add(memberDTO);
        }
        return memberDTOS;
    }

    @Override
    public void update(MemberDTO memberDTO) {
        MemberEntity entity = dtoToEntity(memberDTO);
        memberRepository.save(entity);
    }

    @Override
    public void delete(MemberDTO memberDTO) {
        MemberEntity entity = dtoToEntity(memberDTO);
        memberRepository.deleteById(entity.getSeq());
    }

    @Override
    public void removeWithBoards(Long seq) {
        MemberEntity byId = memberRepository.getById(seq);

        List<BoardEntity> boardEntities = byId.getBoardEntities(); //게시물이 여러개 있을 수 있으니 list

        Iterator b = boardEntities.iterator(); //List를 하나씩 받아올수있게하는것
        while (b.hasNext()){ //값이 있으면 true 없으면 false
            Object next = b.next();
            boardRepository.delete((BoardEntity)next);
        }

    }

    @Override
    public MemberDTO loginByEmail(MemberDTO member) {
        MemberDTO memberDTO = null;
        Object result = memberRepository.getMemberByEmail(member.getEmail(), member.getPw());
        if(result != null){
            memberDTO = entityToDto((MemberEntity) result);
        }
        return memberDTO;
    }

    @Transactional(readOnly = true)
    @Override
    public Map<String, String> validateHandling(Errors errors) {
        Map<String,String> validatorResult = new HashMap<>();

        for(FieldError error : errors.getFieldErrors()){
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName,error.getDefaultMessage());
        }
        return validatorResult;
    }

    @Override
    public MemberDTO readByName(MemberDTO memberDTO) {
        return null;
    }

    @Override
    public MemberDTO readByEmail(String member) {
        return null;
    }


}

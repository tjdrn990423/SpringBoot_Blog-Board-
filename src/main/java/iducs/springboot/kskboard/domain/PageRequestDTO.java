package iducs.springboot.kskboard.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

// 페이지 요청할 때는 Request
@Builder
@Data
@AllArgsConstructor //@Getter, @Setter, @EqualsAndHash, @RequiredArgsConstructor
public class PageRequestDTO {
    private int page; // 요청하는 페이지
    private int size; // 한 페이지에 나타나는 수
    private String sort; // asc, desc
    //private String sort1; //소트 객체를 넘겨주는 방식
    private String type; // 검색 조건, setType(), getType()
    private String keyword; // 검색어, setKeyword(), getKeyword()
    private String categorys;

    public PageRequestDTO(){
        this.page = 1;
        this.size = 5;
    }
    public Pageable getPageable(Sort sort){
        return PageRequest.of(page -1,size,sort);
    }
}

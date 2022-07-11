package iducs.springboot.kskboard.domain;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// 페이지 받을때는 result
@Data
public class PageResultDTO<DTO, EN> { //Generics

    private List<DTO> dtoList;
    private int totalPage; // 총 페이지 수

    private int currentPage; //현재 페이지
    private int sizeOfPage; // 페이지 당 크기

    private int startPage, endPage; // 페이지 목록의 시작 페이지 번호, 마지막 페이지 번호
    private boolean prevPage, nextPage; // 이전 페이지 또는 다음 페이지 존재 유무

    //페이지 번호 목록
    private List<Integer> pageList;

    public PageResultDTO(Page<EN> result, Function<EN,DTO> fn){  //페이지 처리용 객체 Page<EN> result
        dtoList = result.stream().map(fn).collect(Collectors.toList());  //가져온 결과를 순차적으로 진행
        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    public void makePageList(Pageable pageable){
        this.currentPage = pageable.getPageNumber() + 1; // 페이지를 0으로 넘겨서 + 1을 해줘야 1페이지부터 시작
        this.sizeOfPage = pageable.getPageSize();
        double pageDouble = (double) sizeOfPage;
        // sizeOfPage =10, PgaeDouble = 10.0, currentPage = 2
        // 2/10.0 = 0.2 -> Math.ceil(0.2) 올림 : 1 * 10 = 10 (일시적인 마지막 페이지)
        // currnet = 12, 12 / 10.0 = 1.2 -> Math.ceil(1.2) 올림 2 * 10 : 20
        // 반올림인데 무조건 올리는 올림
        //startPage 11 , end 20
        int tempEnd = (int)(Math.ceil(currentPage/pageDouble)) * sizeOfPage;

        startPage = tempEnd - (sizeOfPage - 1);
        endPage = (totalPage > tempEnd) ? tempEnd: totalPage;
        prevPage = startPage > 1;
        nextPage = totalPage > tempEnd;

        //아래쪽 Pagination 처리 시 사용
        pageList = IntStream.rangeClosed(startPage, endPage).boxed().collect(Collectors.toList());
    }
}

package iducs.springboot.kskboard.config;

import iducs.springboot.kskboard.domain.MemberDTO;
import iducs.springboot.kskboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@RequiredArgsConstructor
@Component //doValidate를 구현해 검증로직을 작성하고 bean으로 등록될 수 있도록
public class CheckEmailValidator extends AbstractValidator<MemberDTO> {
    private final MemberRepository memberRepository;

    @Override
    protected void doValidate(MemberDTO dto, Errors errors) {
        if(memberRepository.existsByEmail(dto.getEmail())){
            errors.rejectValue("email", "이메일 중복 오류", "이미 사용중인 이메일 입니다.");
        }
    }
}

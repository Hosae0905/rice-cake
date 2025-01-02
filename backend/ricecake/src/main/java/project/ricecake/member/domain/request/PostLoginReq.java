package project.ricecake.member.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostLoginReq {

    @NotBlank(message = "회원 아이디는 공백일 수 없습니다.")
    @Pattern(regexp = "^[a-z0-9]{8,20}$", message = "아이디 형식이 잘못되었습니다.")
    private String memberId;

    @NotBlank(message = "회원 비밀번호는 공백일 수 없습니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-z0-9!@#$%^&*]{8,20}$", message = "비밀번호 형식이 잘못되었습니다.")
    private String memberPw;
}

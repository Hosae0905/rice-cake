package project.ricecake.member.domain.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * PostSignupReq
 * 클라이언트의 회원가입 요청 정보를 담을 DTO 클래스
 */
@AllArgsConstructor
@Getter
public class PostSignupReq {

    @NotBlank(message = "회원 아이디는 공백일 수 없습니다.")
    @Pattern(regexp = "^[a-z0-9]{8,20}$", message = "아이디 형식이 잘못되었습니다.")
    private String memberId;

    @NotBlank(message = "회원 비밀번호는 공백일 수 없습니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-z0-9!@#$%^&*]{8,20}$", message = "비밀번호 형식이 잘못되었습니다.")
    private String memberPw;

    @NotBlank(message = "회원 이름은 공백일 수 없습니다.")
    @Size(min = 2, max = 20, message = "회원의 이름은 최소 2자부터 최대 20자까지 가능합니다.")
    private String memberName;

    @NotNull(message = "회원 나이는 공백일 수 없습니다.")
    @Min(value = 10, message = "회원의 나이는 최소 10세부터 가능합니다.")
    @Max(value = 100, message = "회원의 나이는 최대 100세까지 가능합니다.")
    private Integer memberAge;
}

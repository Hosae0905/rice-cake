package project.ricecake.member.domain.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostSignupReq {

    @NotBlank
    @Pattern(regexp = "^[a-z0-9]{8,20}$")
    private String memberId;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-z0-9!@#$%^&*]{8,20}$")
    private String memberPw;

    @NotBlank
    @Size(min = 2, max = 20)
    private String memberName;

    @NotNull
    @Min(10) @Max(100)
    private Integer memberAge;
}

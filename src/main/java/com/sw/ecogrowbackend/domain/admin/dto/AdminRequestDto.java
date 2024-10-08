package com.sw.ecogrowbackend.domain.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AdminRequestDto(

    @NotBlank(message = "ID는 필수 사항입니다.")
    @Size(min = 6, max = 20, message = "ID는 최소 6자 이상, 20자 이하로 작성해주세요.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "ID는 소문자 + 숫자로 구성되어야 합니다.")
    String username,

    @NotBlank(message = "비밀번호는 필수 사항입니다.")
    @Size(min = 6, max = 255, message = "비밀번호는 최소 6자 이상, 255자 이하이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_])\\S{6,}$", message = "비밀번호는 알파벳 대소문자 + 숫자 + 특수문자로만 구성되어야 합니다.")
    String password,

    @NotBlank(message = "이메일은 필수 사항입니다.")
    @Size(min = 10, max = 255, message = "이메일은 최소 10글자에서 255글자까지 입력 가능합니다.")
    String email
) {}
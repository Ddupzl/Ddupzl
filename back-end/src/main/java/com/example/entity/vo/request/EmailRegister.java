package com.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class EmailRegister {
    String role;
    @Email
    String email;
    @Length(max=4,min = 4)
    String code;
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
    @Length(max = 10,min = 1)
    String username;
    @Length(min=6,max=20)
    String password;
}

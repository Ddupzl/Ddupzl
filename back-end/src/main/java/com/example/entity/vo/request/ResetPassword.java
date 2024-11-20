package com.example.entity.vo.request;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ResetPassword {
    @Email
    String email;
    @Length(min=4,max = 4)
    String code;
    @Length(min=6,max=20)
    String password;
}

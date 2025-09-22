package com.deena.BankApplication.Util;

import com.deena.BankApplication.UserEntity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    private String userName;
    private String email;
    private String password;
    private Role role; // ADMIN or USER
}

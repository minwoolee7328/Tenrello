package com.example.tenrello.user.service;

import com.example.tenrello.entity.User;
import com.example.tenrello.user.CheckPasswordDto;

public interface UserService {
    /**
     * 회원 탈퇴
     *
     * @param user        요청한 user
     * @param passwordDto 입력받은 pw
     */
    void deleteUser(User user, CheckPasswordDto passwordDto);
}

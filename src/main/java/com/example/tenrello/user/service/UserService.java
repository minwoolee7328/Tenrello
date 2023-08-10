package com.example.tenrello.user.service;

import com.example.tenrello.entity.User;
import com.example.tenrello.user.CheckPasswordDto;
import com.example.tenrello.user.dto.NicknameRequestDto;
import com.example.tenrello.user.dto.PasswordRequestDto;
import com.example.tenrello.user.dto.ProfileResponseDto;

public interface UserService {
    /**
     * 회원 탈퇴
     *
     * @param user        요청한 user
     * @param passwordDto 입력받은 pw
     */
    void deleteUser(User user, CheckPasswordDto passwordDto);

    /**
     * 닉네임 변경
     *
     * @param user       요청한 user
     * @param requestDto 새로운 닉네임
     * @return 닉네임 비교 dto
     */
    ProfileResponseDto updateNickname(User user, NicknameRequestDto requestDto);

    /**
     * 비밀번호 변경
     *
     * @param user       요청한 user
     * @param requestDto 현재 비밀번호와 새로운 비밀번호
     */
    void updatePassword(User user, PasswordRequestDto requestDto);

    /**
     * 수정할 user 객체 가져오기
     *
     * @param userId 찾을 userId
     * @return user
     */
    User findUser(Long userId);
}

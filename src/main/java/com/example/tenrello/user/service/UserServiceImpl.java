package com.example.tenrello.user.service;

import com.example.tenrello.entity.User;
import com.example.tenrello.user.CheckPasswordDto;
import com.example.tenrello.user.dto.*;
import com.example.tenrello.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "UserService")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void deleteUser(User user, CheckPasswordDto passwordDto) {
        if (!passwordEncoder.matches(passwordDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        userRepository.delete(user);
    }

    @Override
    @Transactional
    public ProfileResponseDto updateNickname(User user, NicknameRequestDto requestDto) {
        log.info("닉네임 변경 서비스");
        String nicknameBefore = user.getNickname();
        String nicknameAfter = requestDto.getNewNickname();

        if (userRepository.findByNickname(nicknameAfter).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        User foundUser = findUser(user.getId());
        foundUser.updateNickname(nicknameAfter);

        return new ProfileResponseDto(foundUser.getUsername(), nicknameBefore, foundUser.getNickname());
    }

    @Override
    @Transactional
    public void updatePassword(User user, PasswordRequestDto requestDto) {
        log.info("비밀번호 변경 서비스");
        // 새로운 비밀번호
        String newPassword = requestDto.getNewPassword();

        // 비밀번호 확인
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호를 다시 확인하세요.");
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 사용하고 있는 비밀번호입니다.");
        }

        User foundUser = findUser(user.getId());
        foundUser.updatePassword(passwordEncoder.encode(newPassword));
    }

    @Override
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자 아이디입니다.")
        );
    }

    @Override
    @Transactional(readOnly = true)
    public SearchUserResponseDto searchUserByUsername(UserSearchCond userSearchCond) {
        List<User> searchByUsername
                = userRepository.searchUserByKeyword(userSearchCond);


        List<User> searchByNickname
                = userRepository.searchNickByKeyword(userSearchCond);

        List<SimpleUserInfoDto> result = mergeUserResultLists(searchByUsername, searchByNickname)
                .stream()
                .map(SimpleUserInfoDto::new)
                .toList();

        return new SearchUserResponseDto(result);
    }

    private List<User> mergeUserResultLists(List<User> list1, List<User> list2) {
        Map<Long, User> map = new HashMap<>();

        for (User user1 : list1) {
            map.put(user1.getId(), user1);
        }

        for (User user2 : list2) {
            map.putIfAbsent(user2.getId(), user2);
        }

        return new ArrayList<>(map.values());
    }
}

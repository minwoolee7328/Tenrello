package com.example.tenrello.user.service;

import com.example.tenrello.entity.User;
import com.example.tenrello.user.CheckPasswordDto;
import com.example.tenrello.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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
}

package com.example.tenrello.board.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MemberUserListResponseDto {
    private List<MemberUserDto> memberList;

    public MemberUserListResponseDto(List<MemberUserDto> memberList) {
        this.memberList = memberList;
    }
}

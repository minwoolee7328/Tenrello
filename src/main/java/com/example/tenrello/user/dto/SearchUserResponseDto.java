package com.example.tenrello.user.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class SearchUserResponseDto {
    private List<SimpleUserInfoDto> searchUserResults;

    public SearchUserResponseDto(List<SimpleUserInfoDto> searchUserResults) {
        this.searchUserResults = searchUserResults;
    }
}

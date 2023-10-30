package com.example.tenrello.user.dto;

import lombok.Getter;

@Getter
public class UserSearchCond {
    private String keyword;

    public UserSearchCond(String keyword) {
        this.keyword = keyword;
    }
}

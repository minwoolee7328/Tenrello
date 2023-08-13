package com.example.tenrello.user.repository;

import com.example.tenrello.entity.User;
import com.example.tenrello.user.dto.UserSearchCond;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.tenrello.entity.QUser.user;

@Component
@RequiredArgsConstructor
public class UserRepositoryQueryImpl implements UserRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<User> searchUserByKeyword(UserSearchCond cond) {
        return searchByKeyword(cond, user.username);
    }

    @Override
    public List<User> searchNickByKeyword(UserSearchCond cond) {
        return searchByKeyword(cond, user.nickname);
    }

    private List<User> searchByKeyword(UserSearchCond cond, StringPath field) {
        var query = jpaQueryFactory.selectFrom(user)
                .where(containsKeyword(field, cond.getKeyword()));
        return query.fetch();
    }

    private BooleanExpression containsKeyword(StringPath field, String keyword) {
        return StringUtils.hasText(keyword) ? field.containsIgnoreCase(keyword) : null;
    }
}

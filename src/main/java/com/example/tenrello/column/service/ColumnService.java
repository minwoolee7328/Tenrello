package com.example.tenrello.column.service;

import com.example.tenrello.column.dto.ColumnRequestDto;
import com.example.tenrello.column.dto.ColumnResponseDto;
import com.example.tenrello.common.dto.ApiResponseDto;
import com.example.tenrello.entity.ColumnEntity;
import com.example.tenrello.entity.User;
import com.example.tenrello.security.details.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ColumnService {

    ColumnResponseDto createcolumn(Long boardId, ColumnRequestDto columnRequestDto);

    ResponseEntity<ApiResponseDto> updateColumnName(Long columnId, ColumnRequestDto columnRequestDto, User user);

    ResponseEntity<ApiResponseDto> deleteColumnName(Long columnId, UserDetailsImpl userDetails);

    List<ColumnResponseDto> getBoardColumn(Long boardId);
    ResponseEntity<ApiResponseDto> updateColumnPosition(Long PositionId, Long targetPositionId);
}

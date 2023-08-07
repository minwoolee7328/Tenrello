package com.example.tenrello.column.controller;


import com.example.tenrello.column.dto.ColumnResponseDto;
import com.example.tenrello.column.service.ColumnService;
import com.example.tenrello.column.dto.ColumnRequestDto;
import com.example.tenrello.common.dto.ApiResponseDto;
import com.example.tenrello.entity.ColumnEntity;
import com.example.tenrello.security.details.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ColumnController {
    private final ColumnService columnService;

    @GetMapping("/board/{boardId}/column")
    public List<ColumnResponseDto> getBoardColumn(@PathVariable Long boardId){//board별 컬럼 전체 조회
        return columnService.getBoardColumn(boardId);
    }
    @PostMapping("/board/{boardId}/column")
    public ResponseEntity<ApiResponseDto> createColumn(@PathVariable Long boardId, @RequestBody ColumnRequestDto columnRequestDto){
        return columnService.createcolumn(boardId,columnRequestDto);
    }
    @PutMapping("/column/{columnId}")
    public ResponseEntity<ApiResponseDto> updateColumnName(@PathVariable Long columnId, @RequestBody ColumnRequestDto columnRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return columnService.updateColumnName(columnId, columnRequestDto, userDetails.getUser());
    }
    @DeleteMapping("/column/{columnId}")
    public ResponseEntity<ApiResponseDto> deleteColumnName(@PathVariable Long columnId,@AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        return columnService.deleteColumnName(columnId,userDetails);
    }
}

package com.sw.ecogrowbackend.domain.waste.controller;

import com.sw.ecogrowbackend.domain.waste.dto.WasteReductionTipResponseDto;
import com.sw.ecogrowbackend.domain.waste.service.WasteReductionTipService;
import com.sw.ecogrowbackend.common.ApiResponse;
import com.sw.ecogrowbackend.common.ResponseText;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/waste/tips")
@RequiredArgsConstructor
public class WasteReductionTipController {

    private final WasteReductionTipService tipService;

    /**
     * 특정 사용자의 쓰레기 감소 팁 조회 API
     *
     * @param userId 팁을 조회할 사용자의 ID
     * @return ApiResponse 형태의 사용자의 쓰레기 감소 팁 목록
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse> getTipsForUser(@PathVariable Long userId) {
        List<WasteReductionTipResponseDto> tips = tipService.getTipsForUser(userId);
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.WASTE_REDUCTION_TIP_FETCH_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(tips)
            .build();
        return ResponseEntity.ok(response);
    }
}
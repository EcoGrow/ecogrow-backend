package com.sw.ecogrowbackend.domain.waste.controller;

import com.sw.ecogrowbackend.domain.waste.dto.WasteRecordRequestDto;
import com.sw.ecogrowbackend.domain.waste.dto.WasteRecordResponseDto;
import com.sw.ecogrowbackend.domain.waste.entity.WasteRecord;
import com.sw.ecogrowbackend.domain.waste.service.WasteRecordService;
import com.sw.ecogrowbackend.common.ApiResponse;
import com.sw.ecogrowbackend.common.ResponseText;
import com.sw.ecogrowbackend.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/waste/record")
public class WasteRecordController {

    private final WasteRecordService wasteRecordService;

    /**
     * 쓰레기 기록 생성 API
     *
     * @param requestDto  쓰레기 기록 요청 데이터
     * @param userDetails 인증된 사용자 정보
     * @return 쓰레기 기록 응답 데이터
     */
    @PostMapping
    public ResponseEntity<ApiResponse> recordWaste(
        @Valid @RequestBody WasteRecordRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        WasteRecord wasteRecord = wasteRecordService.saveWasteRecord(userDetails.getUser(),
            requestDto);
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.WASTE_RECORD_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.CREATED.value()))
            .data(wasteRecord)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 사용자별 쓰레기 기록 전체 조회 API
     * @param pageable 페이지 정보
     * @return 쓰레기 기록 목록 응답 데이터
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getAllWasteRecords(
        Pageable pageable) {
        // 인증된 사용자의 ID를 이용하여 기록 조회
        Page<WasteRecordResponseDto> records = wasteRecordService.getAllWasteRecords(pageable);
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.WASTE_RECORD_FETCH_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(records) // 조회된 기록 리스트를 응답 데이터에 포함
            .build();
        return ResponseEntity.ok(response);
    }

    /**
     * 사용자별 쓰레기 기록 조회 API
     *
     * @return 쓰레기 기록 목록 응답 데이터
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getWasteRecords(
        @PathVariable Long userId) {
        // 인증된 사용자의 ID를 이용하여 기록 조회
        List<WasteRecordResponseDto> records = wasteRecordService.getWasteRecordsByUserId(userId);
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.WASTE_RECORD_FETCH_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(records) // 조회된 기록 리스트를 응답 데이터에 포함
            .build();
        return ResponseEntity.ok(response);
    }

    /**
     * 쓰레기 기록 수정 API
     *
     * @param recordId    수정할 기록의 ID
     * @param requestDto  수정할 내용이 담긴 요청 데이터
     * @param userDetails 인증된 사용자 정보
     * @return 수정된 쓰레기 기록 응답 데이터
     */
    @PutMapping("/{recordId}")
    public ResponseEntity<ApiResponse> updateWasteRecord(
        @PathVariable Long recordId,
        @Valid @RequestBody WasteRecordRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        WasteRecord updatedRecord = wasteRecordService.updateWasteRecord(recordId,
            userDetails.getUser(), requestDto);
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.WASTE_RECORD_UPDATE_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .data(updatedRecord)
            .build();
        return ResponseEntity.ok(response);
    }

    /**
     * 쓰레기 기록 삭제 API
     *
     * @param recordId    삭제할 기록의 ID
     * @param userDetails 인증된 사용자 정보
     * @return 삭제 성공 응답 데이터
     */
    @DeleteMapping("/{recordId}")
    public ResponseEntity<ApiResponse> deleteWasteRecord(
        @PathVariable Long recordId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        wasteRecordService.deleteWasteRecord(recordId, userDetails.getUser());
        ApiResponse response = ApiResponse.builder()
            .msg(ResponseText.WASTE_RECORD_DELETE_SUCCESS.getMsg())
            .statuscode(String.valueOf(HttpStatus.OK.value()))
            .build();
        return ResponseEntity.ok(response);
    }
}
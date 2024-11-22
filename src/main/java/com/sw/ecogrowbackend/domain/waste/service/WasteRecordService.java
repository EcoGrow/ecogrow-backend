package com.sw.ecogrowbackend.domain.waste.service;

import com.sw.ecogrowbackend.common.exception.CustomException;
import com.sw.ecogrowbackend.common.exception.ErrorCode;
import com.sw.ecogrowbackend.domain.auth.entity.User;
import com.sw.ecogrowbackend.domain.auth.repository.UserRepository;
import com.sw.ecogrowbackend.domain.waste.dto.WasteItemRequestDto;
import com.sw.ecogrowbackend.domain.waste.dto.WasteRecordRequestDto;
import com.sw.ecogrowbackend.domain.waste.dto.WasteRecordResponseDto;
import com.sw.ecogrowbackend.domain.waste.entity.WasteItem;
import com.sw.ecogrowbackend.domain.waste.entity.WasteRecord;
import com.sw.ecogrowbackend.domain.waste.entity.WasteTypeUtils;
import com.sw.ecogrowbackend.domain.waste.repository.WasteRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WasteRecordService {

    private final UserRepository userRepository;
    private final WasteRecordRepository wasteRecordRepository;

    // 쓰레기 기록 저장 메서드
    public WasteRecord saveWasteRecord(User user, WasteRecordRequestDto requestDto) {
        WasteRecord wasteRecord = new WasteRecord(user);

        for (WasteItemRequestDto itemDto : requestDto.getWasteItems()) {
            double standardizedAmount = convertToKilograms(itemDto.getAmount(), itemDto.getUnit());
            WasteItem wasteItem = new WasteItem();
            wasteItem.setWasteType(itemDto.getWasteType());
            wasteItem.setAmount(standardizedAmount);
            wasteItem.setUnit("kg");
            wasteItem.setRecyclable(WasteTypeUtils.isRecyclable(itemDto.getWasteType()));

            wasteRecord.addWasteItem(wasteItem);
        }
        return wasteRecordRepository.save(wasteRecord);
    }

    // 쓰레기 기록 단건 조회
    @Transactional(readOnly = true)
    public WasteRecordResponseDto getWasteRecord(Long recordId) {
        WasteRecord wasteRecord = wasteRecordRepository.findById(recordId)
            .orElseThrow(() -> new CustomException(ErrorCode.WASTE_RECORD_NOT_FOUND));
        return new WasteRecordResponseDto(wasteRecord);
    }

    // QueryDSL을 사용하여 필터링된 전체 쓰레기 기록 조회
    @Transactional(readOnly = true)
    public Page<WasteRecordResponseDto> getAllWasteRecords(Long userId, String sortOption,
        String startDate, String endDate, Pageable pageable) {
        Page<WasteRecord> wasteRecords = wasteRecordRepository.findFilteredWasteRecords(userId,
            sortOption, startDate, endDate, pageable);
        return wasteRecords.map(WasteRecordResponseDto::new);
    }

    // QueryDSL을 사용하여 특정 사용자의 필터링된 쓰레기 기록 조회
    @Transactional(readOnly = true)
    public Page<WasteRecordResponseDto> getWasteRecordsByUserId(Long userId, String sortOption,
        String startDate, String endDate, Pageable pageable) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Page<WasteRecord> wasteRecords = wasteRecordRepository.findFilteredWasteRecords(
            user.getId(), sortOption, startDate, endDate, pageable);
        return wasteRecords.map(WasteRecordResponseDto::new);
    }

    @Transactional
    public WasteRecord updateWasteRecord(Long recordId, User user,
        WasteRecordRequestDto requestDto) {
        WasteRecord wasteRecord = wasteRecordRepository.findByIdAndUser(recordId, user)
            .orElseThrow(() -> new CustomException(ErrorCode.WASTE_RECORD_NOT_FOUND));

        wasteRecord.getWasteItems().clear();

        for (WasteItemRequestDto itemDto : requestDto.getWasteItems()) {
            double standardizedAmount = convertToKilograms(itemDto.getAmount(), itemDto.getUnit());
            WasteItem wasteItem = new WasteItem();
            wasteItem.setWasteType(itemDto.getWasteType());
            wasteItem.setAmount(standardizedAmount);
            wasteItem.setUnit("kg");

            wasteRecord.addWasteItem(wasteItem); // Associate updated items with the record
        }
        return wasteRecordRepository.save(wasteRecord);
    }

    @Transactional
    public void deleteWasteRecord(Long recordId, User user) {
        WasteRecord wasteRecord = wasteRecordRepository.findByIdAndUser(recordId, user)
            .orElseThrow(() -> new CustomException(ErrorCode.WASTE_RECORD_NOT_FOUND));

        wasteRecordRepository.delete(wasteRecord);
    }

    private double convertToKilograms(double amount, String unit) {
        if (unit.equalsIgnoreCase("g")) {
            return amount / 1000.0;  // 그램을 킬로그램으로 변환
        } else if (unit.equalsIgnoreCase("kg")) {
            return amount;
        } else {
            throw new CustomException(ErrorCode.INVALID_UNIT);
        }
    }
}
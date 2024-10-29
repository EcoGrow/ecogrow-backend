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
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WasteRecordService {

    private final UserRepository userRepository;
    private final WasteRecordRepository wasteRecordRepository;

    @Autowired
    public WasteRecordService(UserRepository userRepository,
        WasteRecordRepository wasteRecordRepository) {
        this.userRepository = userRepository;
        this.wasteRecordRepository = wasteRecordRepository;
    }

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

    @Transactional(readOnly = true)
    public Page<WasteRecordResponseDto> getAllWasteRecords(Pageable pageable) {

        Page<WasteRecord> wasteRecords = wasteRecordRepository.findAllByOrderByCreatedAtDesc(
            pageable);
        return wasteRecords.map(WasteRecordResponseDto::new);
    }

    // 사용자별 쓰레기 기록 조회 메서드
    @Transactional(readOnly = true)
    public List<WasteRecordResponseDto> getWasteRecordsByUserId(Long userId) {

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<WasteRecord> wasteRecords = wasteRecordRepository.findAllByUser(user);

        return wasteRecords.stream()
            .map(WasteRecordResponseDto::fromEntity)
            .collect(Collectors.toList());
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
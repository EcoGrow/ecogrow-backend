package com.sw.ecogrowbackend.domain.waste.service;

import com.sw.ecogrowbackend.common.exception.CustomException;
import com.sw.ecogrowbackend.common.exception.ErrorCode;
import com.sw.ecogrowbackend.domain.auth.entity.User;
import com.sw.ecogrowbackend.domain.auth.repository.UserRepository;
import com.sw.ecogrowbackend.domain.waste.dto.WasteRecordRequestDto;
import com.sw.ecogrowbackend.domain.waste.dto.WasteRecordResponseDto;
import com.sw.ecogrowbackend.domain.waste.entity.WasteRecord;
import com.sw.ecogrowbackend.domain.waste.repository.WasteRecordRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WasteRecordService {

    private final UserRepository userRepository;
    private final WasteRecordRepository wasteRecordRepository;

    @Autowired
    public WasteRecordService(UserRepository userRepository, WasteRecordRepository wasteRecordRepository) {
        this.userRepository = userRepository;
        this.wasteRecordRepository = wasteRecordRepository;
    }

    // 쓰레기 기록 저장 메서드
    public WasteRecord saveWasteRecord(User user, WasteRecordRequestDto requestDto) {
        WasteRecord wasteRecord = new WasteRecord(
            requestDto.getWasteType(),
            requestDto.getAmount(),
            user
        );
        return wasteRecordRepository.save(wasteRecord);
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

        wasteRecord.setWasteType(requestDto.getWasteType());
        wasteRecord.setAmount(requestDto.getAmount());

        return wasteRecordRepository.save(wasteRecord);
    }

    @Transactional
    public void deleteWasteRecord(Long recordId, User user) {
        WasteRecord wasteRecord = wasteRecordRepository.findByIdAndUser(recordId, user)
            .orElseThrow(() -> new CustomException(ErrorCode.WASTE_RECORD_NOT_FOUND));

        wasteRecordRepository.delete(wasteRecord);
    }
}
package com.sw.ecogrowbackend.domain.admin.mapper;

import com.sw.ecogrowbackend.domain.admin.dto.AdminResponseDto;
import com.sw.ecogrowbackend.domain.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * 관리자 관련 DTO와 엔티티 간의 변환을 담당하는 Mapper 인터페이스
 */
@Mapper(componentModel = "spring")
public interface AdminMapper {

    /**
     * User 엔티티를 AdminResponseDto로 변환합니다.
     *
     * @param user 변환할 User 엔티티
     * @return 변환된 AdminResponseDto
     */
    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "username", target = "username"),
        @Mapping(source = "name", target = "name"),
        @Mapping(source = "email", target = "email")
    })
    AdminResponseDto toAdminResponseDto(User user);
}
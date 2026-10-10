package com.lcyl.code.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @ClassName AssignNurseDto
 * @Description TODO
 * @Author hfy
 * @Date 2026-03-25 17:51
 * @Version 1.0
 */
@Data
public class AssignNurseDto {
    private Long elderId;
    private Long roomId;
    private List<Long> nurseIds;
}
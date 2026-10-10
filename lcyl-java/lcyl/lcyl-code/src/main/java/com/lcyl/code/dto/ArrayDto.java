package com.lcyl.code.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lcyl.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName ArrayDto
 * @Description TODO
 * @Author GuiGui
 * @Date 2026-03-23 16:46
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArrayDto {
    private Long id ;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "到院时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date visitTime;
}

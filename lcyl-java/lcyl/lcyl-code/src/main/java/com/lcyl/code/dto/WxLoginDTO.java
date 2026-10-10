package com.lcyl.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxLoginDTO {
    private String code;

    //public String getCode() { return code; }
    //public void setCode(String code) { this.code = code; }
}
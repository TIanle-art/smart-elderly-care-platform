package com.lcyl.system.domain.dto;

import com.lcyl.system.domain.Bed;
import com.lcyl.system.domain.Elder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BedDTO extends Bed {
    // 老人ID
    private Long elderId;
    private Elder elder;
    /** 房型照片 */
    private String photo;
}
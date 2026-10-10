package com.lcyl.system.domain.dto;

import com.lcyl.system.domain.Contract;
import com.lcyl.system.domain.Elder;
import com.lcyl.system.domain.RetreatContract;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class ContractDTO extends Contract {
    private Elder elder;
    private RetreatContract  retreatContract;
}

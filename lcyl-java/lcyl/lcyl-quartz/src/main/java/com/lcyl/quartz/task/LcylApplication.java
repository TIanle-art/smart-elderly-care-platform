package com.lcyl.quartz.task;

import com.lcyl.system.mapper.ContractMapper;
import com.lcyl.system.service.IContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LcylApplication {
    @Autowired
    private IContractService contractService;

    public void ContractTime() {
        contractService.batchUpdateContractStatus ();
    }
}
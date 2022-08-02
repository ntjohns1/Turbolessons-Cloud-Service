package com.noslen.lsaloginservice.feignInterface;

import com.noslen.lsaloginservice.constants.MicroServiceConstants;
import com.noslen.lsaloginservice.constants.MicroServiceConstants.*;
import com.noslen.lsaloginservice.requestDTO.AdminRequestDTO;
import com.noslen.lsaloginservice.responseDTO.AdminResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "lsa-admin-service", url = "http://localhost:2121")
@Service
//@RequestMapping(value = MicroServiceConstants.BASE_API)
public interface AdminInterface {

    @GetMapping(value = "/api/search")
    AdminResponseDTO searchAdmin(@RequestBody AdminRequestDTO requestDTO);

    @PostMapping(value = MicroServiceConstants.BASE_API + AdminMicroServiceConstants.UPDATE_ADMIN)
    void updateAdmin(@RequestBody AdminResponseDTO responseDTO);
}

package com.noslen.lsaauthservice.feignInterface;

import com.noslen.lsaauthservice.responseDTO.AdminResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

import static com.noslen.lsaauthservice.constants.MicroServiceConstants.*;

@FeignClient(name = "lsa-admin-service", url = "http://localhost:2121")
@Service
//@RequestMapping(value = BASE_API)
public interface AdminInterface {

    @GetMapping(value = "/api/fetch-admin/{username}")
    Optional<AdminResponseDTO> fetchAdminByUsername(@PathVariable("username") String username);
}

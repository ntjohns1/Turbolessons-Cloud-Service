package com.noslen.lsaadminservice.service;

import com.noslen.lsaadminservice.entities.Admin;
import com.noslen.lsaadminservice.dto.request.AdminRequestDTO;
import com.noslen.lsaadminservice.dto.response.AdminResponseDTO;
import com.noslen.lsaadminservice.dto.response.ResponseDTO;

import java.util.List;

/**
 * @author smriti
 */
public interface AdminService {

    void saveAdmin(AdminRequestDTO requestDTO);

    AdminResponseDTO searchAdmin(AdminRequestDTO requestDTO);

    Admin updateAdmin(AdminRequestDTO requestDTO);

    Admin fetchAdminByUsername(String username);




    ResponseDTO adminsToSendEmails();


    List<Admin> fetchAllAdmins();
}

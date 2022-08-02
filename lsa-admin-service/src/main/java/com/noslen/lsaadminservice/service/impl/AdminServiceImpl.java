package com.noslen.lsaadminservice.service.impl;

import com.noslen.lsaadminservice.dto.request.AdminRequestDTO;
import com.noslen.lsaadminservice.dto.response.AdminResponseDTO;
import com.noslen.lsaadminservice.dto.response.ResponseDTO;
import com.noslen.lsaadminservice.entities.Admin;
import com.noslen.lsaadminservice.exceptions.DataDuplicationException;
import com.noslen.lsaadminservice.exceptions.NoContentFoundException;
import com.noslen.lsaadminservice.repository.AdminRepository;
import com.noslen.lsaadminservice.service.AdminService;
import com.noslen.lsaadminservice.utility.AdminUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.noslen.lsaadminservice.constants.ErrorMessageConstants.*;
import static com.noslen.lsaadminservice.query.AdminQuery.createQueryToFetchAdminDetails;
import static com.noslen.lsaadminservice.query.AdminQuery.createQueryToFetchAdminsToSendEmail;
import static com.noslen.lsaadminservice.utility.AdminUtils.convertToAdminResponse;

/**
 * @author smriti
 */

@Service
@Transactional
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }


    @Override
    public void saveAdmin(AdminRequestDTO requestDTO) {

        log.info(":::: SAVE ADMIN PROCESS STARTED::::");
        validateAdminRequestDTO.accept(requestDTO);

        System.out.println("VALIDATION DONE");
    }

    public Consumer<AdminRequestDTO> validateAdminRequestDTO = (requestDTO) -> {
        adminRepository.fetchAdminByUsername(requestDTO.getUsername()).ifPresent(admin -> {
            throw new DataDuplicationException(DUPLICATE_USERNAME_MESSAGE, DUPLICATE_USERNAME_DEVELOPER_MESSAGE);
        });

        adminRepository.fetchAdminByEmailAddress(requestDTO.getEmailAddress()).ifPresent(admin -> {
            throw new DataDuplicationException(DUPLICATE_EMAILADDRESS_MESSAGE, DUPLICATE_EMAILADDRESS_DEVELOPER_MESSAGE);
        });
    };


    //    @Override
//    public void saveAdmin(AdminRequestDTO requestDTO) {
////        Admin admin = MapperUtility.map(requestDTO, Admin.class);
////
////        admin.setPassword(BCrypt.hashpw(requestDTO.getPassword(), BCrypt.gensalt()));
////        admin.setLoginAttempt(0);
////
////        admin.setRoles(Arrays.asList("ROLE_USER"));
////        adminRepository.save(admin);
//    }

    /*SEARCH ADMIN FOR LOGIN VALIDATION*/
    @Override
    public AdminResponseDTO searchAdmin(AdminRequestDTO requestDTO) throws NoContentFoundException {
        System.out.println("-------------- ping --------------");
        List<Object[]> results = entityManager.createNativeQuery(
                createQueryToFetchAdminDetails.apply(requestDTO)).getResultList();

        if (ObjectUtils.isEmpty(results))
            throw new NoContentFoundException(ADMIN_NOT_FOUND_MESSAGE, ADMIN_NOT_FOUND_DEVELOPER_MESSAGE);

        return convertToAdminResponse.apply(results);
    }

    /*FOR UPDATING LOGIN ATTEMPTS */
    @Override
    public Admin updateAdmin(AdminRequestDTO requestDTO) {

        Admin admin = this.adminRepository.getAdminById(requestDTO.getId()).orElseThrow(() -> {
            return new NoContentFoundException(ADMIN_NOT_FOUND_MESSAGE, ADMIN_NOT_FOUND_DEVELOPER_MESSAGE);
        });

        admin.setStatus(requestDTO.getStatus());
        admin.setLoginAttempt(requestDTO.getLoginAttempt());

        return adminRepository.save(admin);
    }

    /*USED BY AUTH-SERVICE AFTER SUCCESSFUL TOKEN VALIDATION*/
    @Override
    public Admin fetchAdminByUsername(String username) {
        return adminRepository.fetchAdminByUsername(username).orElseThrow(() ->
                new NoContentFoundException(ADMIN_NOT_FOUND_MESSAGE, ADMIN_NOT_FOUND_DEVELOPER_MESSAGE));
    }


    @Override
    public ResponseDTO adminsToSendEmails() {

        List<Object[]> results = entityManager.createNativeQuery(
                createQueryToFetchAdminsToSendEmail.get()).getResultList();

        List<AdminResponseDTO> responseDTOS = results.stream().map(AdminUtils.convertToResponse)
                .collect(Collectors.toList());

        return ResponseDTO.builder().adminResponseDTOS(responseDTOS).build();
    }
    
    @Override
    public List<Admin> fetchAllAdmins() {
        return null;
    }
    
    
}

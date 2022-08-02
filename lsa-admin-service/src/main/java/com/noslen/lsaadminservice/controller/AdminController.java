package com.noslen.lsaadminservice.controller;

import com.noslen.lsaadminservice.dto.request.AdminRequestDTO;
import com.noslen.lsaadminservice.dto.response.ResponseDTO;
import com.noslen.lsaadminservice.service.AdminService;
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.noslen.lsaadminservice.constants.WebResourceKeyConstants.*;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author smriti
 */
@RestController
//@RequestMapping(value = BASE_API)
@Api(value = "This is admin controller")
public class AdminController {
	
	private final AdminService adminService;
	
	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}
	
	@PostMapping(value = "/api/save")
	@ApiOperation(value = "Save new admin")
	@ResponseBody
	public ResponseEntity<?> saveAdmin(@RequestBody AdminRequestDTO requestDTO) {
		adminService.saveAdmin(requestDTO);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/api/search")
	@ApiOperation(value = "Search admin by given parameters in request")
	public ResponseEntity<?> searchAdmin(@ApiParam(value = "Parameter value is admin username")
										 @RequestBody AdminRequestDTO requestDTO) {
		return ok().body(adminService.searchAdmin(requestDTO));
	}
	
	@PostMapping(value = UPDATE)
	@ApiOperation(value = "Update admin information")
	public ResponseEntity<?> updateAdmin(@RequestBody AdminRequestDTO requestDTO) {
		return ok().body(adminService.updateAdmin(requestDTO));
	}
	
	@GetMapping(value = FETCH_ALL_ADMINS)
	@ApiOperation(value = "Fetch list of all active admins")
	public ResponseEntity<?> fetchAllAdmins() {
		return ok().body(adminService.fetchAllAdmins());
	}
	
	
	@GetMapping(value = "/api/fetch-admin/{username}")
	public ResponseEntity fetchAdminByUsername(@PathVariable("username") String username) {
		return ok(adminService.fetchAdminByUsername(username));
	}
	
	@GetMapping("/api/admins")
	public ResponseEntity<ResponseDTO> getAdminsToSendEmail() {
		return new ResponseEntity<>(adminService.adminsToSendEmails(), HttpStatus.OK);
	}
	
	
	@GetMapping("/api/sayHello/{name}")
	@CircuitBreaker(name = "breaker", fallbackMethod = "fallBackHello")
	public String sayHello(@PathVariable("name") String name) {
		if (name.equalsIgnoreCase("hello"))
			throw new RuntimeException();
		return "success METHOD";
		
	}
	
	public String fallBackHello(Exception ex) {
		return "fallback hello";
	}
	
	
	@GetMapping("/hello")
	public String hello() {
		return "Hello world!!!";
	}
	
	
}

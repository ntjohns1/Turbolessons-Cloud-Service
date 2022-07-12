//package com.noslen.lsaauthservice.UserDetails;
//
//import com.noslen.lsaauthservice.util.feign.AdminClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//public class Filter {
//
//    @Autowired
//    private final AdminClient adminClient;
//
//    Filter(AdminClient adminClient) {this.adminClient = adminClient; }
//
//    //GET USER BY USERNAME
//    @GetMapping("/{username}")
//    public Object getUserByUsername(@PathVariable String username) {
//        Object obj = adminClient.getUserByUsername(username);
//        return obj;
//    }
//}

package com.warpedcitadel.warpedcitadelauth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/test", version = "1.0")
public class TestController {

    @GetMapping("/user")
    public String testAuthentication() {

        return "This is a user endpoint";
    }
}

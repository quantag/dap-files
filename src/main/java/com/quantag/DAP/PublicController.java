package com.quantag.DAP;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {

    @GetMapping("/public/getVersion")
    public String getVersion() {
        return "version 1.2.4 build 28.07.2025 11:40";
    }
}
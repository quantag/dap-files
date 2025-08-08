package com.quantag.DAP;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {

    @GetMapping("/public/getVersion")
    public String getVersion() {
        return "version 1.2.4 build 07.08.2025 13:40";
    }

}
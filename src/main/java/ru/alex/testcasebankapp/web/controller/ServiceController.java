package ru.alex.testcasebankapp.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/service")
@RestController
public class ServiceController {

    @GetMapping("/get")
    public String getService() {
        return null;
    }


    @PostMapping("/put_service")
    public ResponseEntity<String> putService(@RequestParam String service) {
        return null;
    }
}

package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.dto.QrCodeDto;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.services.IBoardAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class BoardAdmin {

    @Autowired
    IBoardAdmin iBoardAdmin;

    @GetMapping("/allUsers")
    public ResponseEntity<List<User>> getAllUsers()  {
        return this.iBoardAdmin.getAlUsers();
    }

    @GetMapping("/lock")
    public void lock(@RequestParam("id") String id)  {
        this.iBoardAdmin.lock(Long.parseLong(id));
    }

    @GetMapping("/open")
    public void open(@RequestParam("id") String id)  {
        this.iBoardAdmin.open(Long.parseLong(id));
    }

    @GetMapping("/getCodes")
    public ResponseEntity<List<QrCodeDto>> getCodes()  {
        return this.iBoardAdmin.getCodes();
    }

    @GetMapping("/getCode")
    public ResponseEntity<List<QrCodeDto>> getCode(@RequestParam("id") String id)
    {
        return this.iBoardAdmin.getCode(Long.parseLong(id));
    }

}

package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.dto.QrCodeDto;
import com.bezkoder.springjwt.models.Viewer;
import com.bezkoder.springjwt.services.IBoardUser;
import com.bezkoder.springjwt.utils.ParamsPath;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class BoardUser {

    @Autowired
    IBoardUser iBoardUser;

    @PostMapping("/saveMenu")
    @ResponseStatus(HttpStatus.OK)
    public void saveMenu(@RequestParam(ParamsPath.FILE) MultipartFile file,
                         @RequestParam(ParamsPath.NAME) String name) throws IOException, WriterException {
        this.iBoardUser.saveMenu(file, name);
    }

    @PostMapping("/editMenu")
    @ResponseStatus(HttpStatus.OK)
    public void editMenu(@RequestParam(ParamsPath.FILE) MultipartFile file,
                         @RequestParam(ParamsPath.NAME) String name,
                         @RequestParam(ParamsPath.ID) String id) throws IOException, WriterException {
        this.iBoardUser.editMenu(file, name, Long.parseLong(id));
    }


    @GetMapping("/getCodes")
    public ResponseEntity<List<QrCodeDto>> getCodes()  {
        return this.iBoardUser.getCodes();
    }

    @DeleteMapping("/delete")
    public void deleteCode(@RequestParam("id") String codeId) {
        this.iBoardUser.delete(Long.valueOf(codeId));
    }

    @GetMapping("/getCode")
    public ResponseEntity<QrCodeDto> getCode(@RequestParam("id") String id)
    {
        return this.iBoardUser.getCode(Long.parseLong(id));
    }

    @GetMapping("/getCodeForScan")
    public ResponseEntity<QrCodeDto> getCodeForScan(@RequestParam("id") String id)
    {
        return this.iBoardUser.getCodeForScan(Long.parseLong(id));
    }

    @PostMapping("/setTime")
    @ResponseStatus(HttpStatus.OK)
    public Viewer setViewedTime(@RequestParam(ParamsPath.TIME) String time,
                                                @RequestParam(ParamsPath.ID) String viewerId,
                                                @RequestParam(ParamsPath.QR_CODE_ID) String qrCodeId) {
        return this.iBoardUser.saveViewingTime(Long.parseLong(viewerId), Long.parseLong(qrCodeId), Long.parseLong(time));
    }

    @GetMapping("/newViewer")
    public Viewer newViewer()
    {
        return this.iBoardUser.newViewer();
    }
}

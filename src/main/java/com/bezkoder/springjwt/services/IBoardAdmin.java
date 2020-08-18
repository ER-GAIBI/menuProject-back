package com.bezkoder.springjwt.services;

import com.bezkoder.springjwt.dto.QrCodeDto;
import com.bezkoder.springjwt.models.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IBoardAdmin {

    ResponseEntity<List<User>> getAlUsers();

    void lock(long userId);

    void open(long userId);

    ResponseEntity<List<QrCodeDto>> getCodes();

    ResponseEntity<List<QrCodeDto>> getCode(long id);
}

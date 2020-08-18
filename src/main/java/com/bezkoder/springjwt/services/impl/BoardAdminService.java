package com.bezkoder.springjwt.services.impl;

import com.bezkoder.springjwt.dto.QrCodeDto;
import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.QrCode;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.repository.QrCodeRepository;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;
import com.bezkoder.springjwt.services.IBoardAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BoardAdminService implements IBoardAdmin {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    QrCodeRepository qrCodeRepository;

    @Override
    public ResponseEntity<List<User>> getAlUsers() {
        Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElse(new Role());
        List<User> users = userRepository.findByRolesIs(userRole);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Override
    public void lock(long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null && user.getId() != null) {
            user.setEnabled(false);
        }
    }

    @Override
    public void open(long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null && user.getId() != null) {
            user.setEnabled(true);
        }
    }

    @Override
    public ResponseEntity<List<QrCodeDto>> getCodes() {
        List<QrCodeDto> qrCodeDtos = new ArrayList<>();
        List<QrCode> qrCodes = qrCodeRepository.findAll();
        qrCodes.forEach(qrCode -> {
            Path path = Paths.get(qrCode.getLocation());
            Path pathFileToDisplay = Paths.get(qrCode.getFilePath());
            QrCodeDto qrCodeDto = new QrCodeDto(qrCode, readFileToByteArray(path.toFile()),
                    readFileToByteArray(pathFileToDisplay.toFile()));
            qrCodeDtos.add(qrCodeDto);
        });
        return new ResponseEntity<>(qrCodeDtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<QrCodeDto>> getCode(long id) {
        List<QrCodeDto> qrCodeDtos = new ArrayList<>();
        List<QrCode> qrCodes = qrCodeRepository.findAllByUserId(id);
        qrCodes.forEach(qrCode -> {
            Path path = Paths.get(qrCode.getLocation());
            Path pathFileToDisplay = Paths.get(qrCode.getFilePath());
            QrCodeDto qrCodeDto = new QrCodeDto(qrCode, readFileToByteArray(path.toFile()),
                    readFileToByteArray(pathFileToDisplay.toFile()));
            qrCodeDtos.add(qrCodeDto);
        });
        return new ResponseEntity<>(qrCodeDtos, HttpStatus.OK);
    }

    private static byte[] readFileToByteArray(File file){
        FileInputStream fis = null;
        // Creating a byte array using the length of the file
        // file.length returns long which is cast to int
        byte[] bArray = new byte[(int) file.length()];
        try{
            fis = new FileInputStream(file);
            fis.read(bArray);
            fis.close();
        }catch(IOException ioExp){
            ioExp.printStackTrace();
        }
        return bArray;
    }
}

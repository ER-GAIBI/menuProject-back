package com.bezkoder.springjwt.services;

import com.bezkoder.springjwt.dto.QrCodeDto;
import com.bezkoder.springjwt.models.Viewer;
import com.google.zxing.WriterException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IBoardUser {

    void saveMenu(MultipartFile file, String name) throws IOException, WriterException;

    ResponseEntity<List<QrCodeDto>> getCodes();

    ResponseEntity<QrCodeDto> getCode(long id);

    ResponseEntity<QrCodeDto> getCodeForScan(long id);

    void delete(Long idQrCode);

    void editMenu(MultipartFile file, String name, long id) throws IOException, WriterException;

    Viewer saveViewingTime(long idViewer, long idQrCode, long qrCode);

    Viewer newViewer();

}

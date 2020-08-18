package com.bezkoder.springjwt.services.impl;

import com.bezkoder.springjwt.dto.QrCodeDto;
import com.bezkoder.springjwt.models.QrCode;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.models.Viewer;
import com.bezkoder.springjwt.repository.QrCodeRepository;
import com.bezkoder.springjwt.repository.ViewerRepository;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;
import com.bezkoder.springjwt.services.IBoardUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

@Service
@Transactional
public class BoardUserService implements IBoardUser {

    private static final String ROOT = "./generatedQrCode/";

    @Autowired
    QrCodeRepository qrCodeRepository;

    @Autowired
    ViewerRepository viewerRepository;

    @Autowired
    ResourceLoader resourceLoader;


    @Override
    public void saveMenu(MultipartFile file, String name) throws IOException, WriterException {
        if (file != null) {
            String qrCodeText  = file.getOriginalFilename();
            if (qrCodeText != null) {
                qrCodeText = qrCodeText.substring(0, qrCodeText.lastIndexOf('.'));
            }
            // String filePath = ROOT + qrCodeText + ".png";
            String filePath = qrCodeText + ".png";
            int size = 200;
            String fileType = "png";
            File qrFile = new File(filePath);
            UserDetailsImpl userDetail = (UserDetailsImpl) getAuthentication().getPrincipal();
            User user = userDetail.getUser();
            String fileToSave = this.saveUploadedFile(file);
            QrCode qrCode = new QrCode(filePath, user, name, fileToSave);
            QrCode svedQrCode = qrCodeRepository.save(qrCode);
            createQRImage(qrFile, "http://46.101.151.85:4200/getCode?id=" + svedQrCode.getId(), size, fileType);
        } else {
            // return null;
        }
    }

    @Override
    public void editMenu(MultipartFile file, String name, long id) throws IOException, WriterException {
        QrCode qrCode = qrCodeRepository.findById(id).orElse(null);
        if (file != null) {
            String qrCodeText  = file.getOriginalFilename();
            if (qrCodeText != null) {
                qrCodeText = qrCodeText.substring(0, qrCodeText.lastIndexOf('.'));
            }
            // String filePath = ROOT + qrCodeText + ".png";
            String filePath = qrCodeText + ".png";
            int size = 200;
            String fileType = "png";
            File qrFile = new File(filePath);
            String fileToSave = this.saveUploadedFile(file);
            qrCode.setName(name);
            qrCode.setScannedTime(0);
            qrCode.setLocation(filePath);
            qrCode.setFilePath(fileToSave);
            createQRImage(qrFile, "http://46.101.151.85:4200/getCode?id=" + qrCode.getId(), size, fileType);
        } else {
            // return null;
        }
    }

    private String saveUploadedFile(MultipartFile file) throws IOException {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss-"));
        String fileName = date + file.getOriginalFilename();

        Files.copy(file.getInputStream(), Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private static void createQRImage(File qrFile, String qrCodeText, int size, String fileType)
            throws WriterException, IOException {
        // Create the ByteMatrix for the QR-Code that encodes the given String
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
        // Make the BufferedImage that are to hold the QRCode
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        // Paint and save the image using the ByteMatrix
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        ImageIO.write(image, fileType, qrFile);
    }

    @Override
    public ResponseEntity<List<QrCodeDto>> getCodes() {
        List<QrCodeDto> qrCodeDtos = new ArrayList<>();
        UserDetailsImpl userDetail = (UserDetailsImpl) getAuthentication().getPrincipal();
        User user = userDetail.getUser();
        List<QrCode> qrCodes = qrCodeRepository.findAllByUserId(user.getId());
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
    public ResponseEntity<QrCodeDto> getCode(long id) {
        QrCode qrCode = qrCodeRepository.findById(id).orElse(null);
        assert qrCode != null;
        Path path = Paths.get(qrCode.getLocation());
        QrCodeDto qrCodeDto = new QrCodeDto(qrCode, readFileToByteArray(path.toFile()));
        return new ResponseEntity<>(qrCodeDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<QrCodeDto> getCodeForScan(long id) {
        QrCode qrCode = qrCodeRepository.findById(id).orElse(null);
        assert qrCode != null;
        Path pathFileToDisplay = Paths.get(qrCode.getFilePath());
        qrCode.setScannedTime(qrCode.getScannedTime() + 1);
        Viewer viewer = viewerRepository.save(new Viewer(new Date()));
        QrCodeDto qrCodeDto = new QrCodeDto(qrCode, readFileToByteArray(pathFileToDisplay.toFile()), viewer);
        return new ResponseEntity<>(qrCodeDto, HttpStatus.OK);
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

    @Override
    public void delete(Long idQrCode) {
        this.qrCodeRepository.deleteById(idQrCode);
    }

    @Override
    public Viewer saveViewingTime(long idViewer, long idQrCode, long time) {
        Viewer viewer = viewerRepository.findById(idViewer).orElse(new Viewer());
        if (viewer.getId() != null) {
            QrCode qrCode = qrCodeRepository.findById(idQrCode).orElse(null);
            viewer.setViewingTime(time);
            viewer.setQrCode(qrCode);
        }
        return viewer;
    }

    @Override
    public Viewer newViewer() {
        return viewerRepository.save(new Viewer(new Date()));
    }
}

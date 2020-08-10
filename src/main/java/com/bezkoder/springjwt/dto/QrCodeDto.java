package com.bezkoder.springjwt.dto;

import com.bezkoder.springjwt.models.QrCode;

public class QrCodeDto {

    private QrCode qrCode;
    private byte[] file;
    private byte[] fileToDispaly;

    public QrCodeDto() {
    }

    public QrCodeDto(QrCode qrCode, byte[] file, byte[] fileToDispaly) {
        this.qrCode = qrCode;
        this.file = file;
        this.fileToDispaly = fileToDispaly;
    }

    public QrCode getQrCode() {
        return qrCode;
    }

    public void setQrCode(QrCode qrCode) {
        this.qrCode = qrCode;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public byte[] getFileToDispaly() {
        return fileToDispaly;
    }

    public void setFileToDispaly(byte[] fileToDispaly) {
        this.fileToDispaly = fileToDispaly;
    }
}

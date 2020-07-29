package com.bezkoder.springjwt.dto;

import com.bezkoder.springjwt.models.QrCode;

public class QrCodeDto {

    private QrCode qrCode;
    private byte[] file;

    public QrCodeDto() {
    }

    public QrCodeDto(QrCode qrCode, byte[] file) {
        this.qrCode = qrCode;
        this.file = file;
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
}

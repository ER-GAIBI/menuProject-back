package com.bezkoder.springjwt.dto;

import com.bezkoder.springjwt.models.QrCode;
import com.bezkoder.springjwt.models.Viewer;

public class QrCodeDto {

    private QrCode qrCode;
    private byte[] file;
    private byte[] fileToDispaly;
    private Viewer viewer;

    public QrCodeDto() {
    }

    public QrCodeDto(QrCode qrCode, byte[] file, byte[] fileToDispaly) {
        this.qrCode = qrCode;
        this.file = file;
        this.fileToDispaly = fileToDispaly;
    }

    public QrCodeDto(QrCode qrCode, byte[] file) {
        this.qrCode = qrCode;
        this.file = file;
    }

    public QrCodeDto(QrCode qrCode, byte[] fileToDispaly, Viewer viewer) {
        this.qrCode = qrCode;
        this.fileToDispaly = fileToDispaly;
        this.viewer = viewer;
    }

    public QrCodeDto(byte[] fileToDispaly) {
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

    public Viewer getViewer() {
        return viewer;
    }

    public void setViewer(Viewer viewer) {
        this.viewer = viewer;
    }
}

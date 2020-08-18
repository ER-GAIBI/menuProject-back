package com.bezkoder.springjwt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Viewer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private long viewingTime;

    private Date startDate;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_qrCode_id")
    private QrCode qrCode;


    public Viewer() {
    }

    public Viewer(Date startDate) {
        this.startDate = startDate;
    }

    public Viewer(Long viewingTime) {
        this.viewingTime = viewingTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getViewingTime() {
        return viewingTime;
    }

    public void setViewingTime(long viewingTime) {
        this.viewingTime = viewingTime;
    }

    public QrCode getQrCode() {
        return qrCode;
    }

    public void setQrCode(QrCode qrCode) {
        this.qrCode = qrCode;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}

package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, Long> {

    List<QrCode> findAllByUserId(Long userId);
}

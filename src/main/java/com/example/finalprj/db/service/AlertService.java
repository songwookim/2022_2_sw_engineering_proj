package com.example.finalprj.db.service;

import com.example.finalprj.db.domain.Alert;
import com.example.finalprj.db.domain.Notice;
import com.example.finalprj.db.repository.AlertRepository;
import com.example.finalprj.db.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository alertRepository;

    public Optional<Alert> findById(Long id) {
        return alertRepository.findById(id);
    }

    public void updateAlert(Alert alert) {
        alertRepository.updateById(alert.getId(), alert.getStatus());
    }
}

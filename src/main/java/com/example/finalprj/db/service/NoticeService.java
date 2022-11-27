package com.example.finalprj.db.service;

import com.example.finalprj.db.domain.Notice;
import com.example.finalprj.db.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public void save(Notice notice) {
        noticeRepository.save(notice);
        noticeRepository.indexing();
    }

    public List<Notice> findAll() {
        if(noticeRepository.findAll().isEmpty()) {
            return null;
        }
        return noticeRepository.findAll();
    }

    public void deleteById(Long id) {
        noticeRepository.deleteById(id);
        noticeRepository.indexing();
    }

    public Optional<Notice> findById(Long id) {
        return noticeRepository.findById(id);
    }

    public void updateNotice(Notice notice) {
        noticeRepository.updateById(notice.getId(), notice.getSubject(), notice.getContent());
        noticeRepository.indexing();
    }
}

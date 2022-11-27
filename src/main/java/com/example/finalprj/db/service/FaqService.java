package com.example.finalprj.db.service;

import com.example.finalprj.db.domain.Faq;
import com.example.finalprj.db.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FaqService {
    private final FaqRepository faqRepository;

    public void save(Faq faq) {
        faqRepository.save(faq);
        faqRepository.indexing();
    }

    public List<Faq> findAll() {
        if(faqRepository.findAll().isEmpty()) {
            return null;
        }
        return faqRepository.findAll();
    }

    public void deleteById(Long id) {
        faqRepository.deleteById(id);
        faqRepository.indexing();
    }

    public Optional<Faq> findById(Long id) {
        return faqRepository.findById(id);
    }

    public void updateFaq(Faq faq) {
        faqRepository.updateById(faq.getId(), faq.getSubject(), faq.getContent());
        faqRepository.indexing();
    }
}

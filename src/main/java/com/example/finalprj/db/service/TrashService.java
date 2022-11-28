package com.example.finalprj.db.service;

import com.example.finalprj.db.domain.Trash;
import com.example.finalprj.db.repository.TrashRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrashService {
    private final TrashRepository trashRepository;

    public Optional<Trash> findById(Long id) {
        return trashRepository.findById(id);
    }

    public void updateTrash(Trash trash) {
        trashRepository.updateById(trash.getId(), trash.getStatus());
    }
}

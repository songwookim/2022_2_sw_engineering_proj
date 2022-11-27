package com.example.finalprj.db.service;

import com.example.finalprj.db.domain.Photo;
import com.example.finalprj.db.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;

    public List<Photo> getPhotos() {
        return photoRepository.findAll();
    }

    public void deleteById(Long id) {
        photoRepository.deleteById(id);
    }
}

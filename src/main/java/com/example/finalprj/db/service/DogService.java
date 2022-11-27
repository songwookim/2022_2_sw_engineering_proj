package com.example.finalprj.db.service;

import com.example.finalprj.db.domain.Dog;
import com.example.finalprj.db.repository.DogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DogService {
    private final DogRepository dogRepository;

    public void save(Dog dog) {
        dogRepository.save(dog);
    }
}

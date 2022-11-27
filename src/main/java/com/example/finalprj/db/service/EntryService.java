package com.example.finalprj.db.service;

import com.example.finalprj.db.domain.Entry;
import com.example.finalprj.db.repository.EntryRepository;
import com.example.finalprj.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class
EntryService {
    private final EntryRepository entryRepository;
    private final UserRepository userRepository;
    private Long id;

    public List<Long> findAllUserIdByPlaygroundIdAndStatusEqual(long playground_id, int status) {
        return entryRepository.findAllUserIdByPlaygroundIdAndStatusEqual(playground_id, status);
    }

    public List<Entry> findAllByPlaygroundIdAndStatusEqual(long playground_id, int status) {
        return entryRepository.findAllByPlaygroundIdAndStatusEqual(playground_id, status);
    }

    @Transactional
    public void deleteUserIdStatusEquals(long userId, long playgroundId, int status) {
        try {
            entryRepository.deleteByUserIdAndPlaygroundIdAndStatus(userId, playgroundId, status);
        } catch (Exception e) {
            System.out.println("아직 없음");
        }
        ;
    }

    public void entryUser(long userId, long playgroundId) {
        entryRepository.updateStatusRevToUsing(userId, playgroundId);
    }

    public void exitUser(long userId, long playgroundId) {
        Entry entryTemp = entryRepository.findEntryWhereUserPlaygroundStatus(userId, playgroundId, 2).orElse(null);

        entryRepository.saveNative(userId, playgroundId, 3,entryTemp.getUpdatedAt());

        deleteUserIdStatusEquals(userId, playgroundId, 2);  // 삭제


    }

    public List<Entry> findAllByUserIdAndStatus(Long id, int status) {
        return entryRepository.findAllByUserIdAndStatus(id, status);
    }

    public void deleteByIdAndStatus(long entryId, int i) {
        entryRepository.deleteByIdAndStatus(entryId, i);
    }

    public Integer countByPlaygroundIdAndStatus(long pgId, int i) {
        return entryRepository.countByPlaygroundIdAndStatus(pgId, i);
    }

    public void save(long userId, long playgroundId, int status) {
        entryRepository.saveByUserIdAndPlaygroundIdAndStatus(userId, playgroundId, status);
    }

    public Optional<Entry> findByUserIdAndPlaygroundIdAndStatus(long id, long playgroundId, int i) {
        return entryRepository.findByUserIdAndPlaygroundIdAndStatus(id, playgroundId, i);
    }

    public Long findUserIdById(long entryId) {
        return entryRepository.findUserIdById(entryId);
    }


    public Optional<Entry> findByUserIdAndStatus(Long userId, int i) {
        return entryRepository.findByUserIdAndStatus(userId, i);
    }
}

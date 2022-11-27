package com.example.finalprj.db.repository;

import com.example.finalprj.db.domain.Entry;
import com.example.finalprj.db.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.format.annotation.DateTimeFormat;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EntryRepository extends JpaRepository<Entry, Long> {

    @Query("select userId from Entry where status = :status and playgroundId = :playgroundId order by createdAt desc")
    List<Long> findAllUserIdByPlaygroundIdAndStatusEqual(long playgroundId, int status);

    @Query("SELECT e from Entry e WHERE e.playgroundId = :playgroundId and e.status = :status order by e.updatedAt desc")
    List<Entry> findAllByPlaygroundIdAndStatusEqual(long playgroundId, int status);

    @Query("delete from Entry where status = :status and playgroundId = :playgroundId and userId = :userId")
    @Transactional
    @Modifying(clearAutomatically = true)
    void deleteByUserIdAndPlaygroundIdAndStatus(long userId, long playgroundId, int status);

    @Query("select e from Entry as e where e.status = :status and e.playgroundId = :playgroundId and e.userId = :userId")
    Optional<Entry> findEntryWhereUserPlaygroundStatus(long userId, long playgroundId, int status);

    @Query("update Entry e SET e.status = 2 where e.status = 1 and e.playgroundId = :playgroundId and e.userId = :userId")
    @Modifying
    @Transactional
    void updateStatusRevToUsing(long userId, long playgroundId);

    @Query(value = "INSERT INTO entry_list(id, user_id, playground_id,status, updated_at) values (FLOOR(RAND()*(999999)+100000), :userId, :playgroundId ,:status, :updatedAt)", nativeQuery = true)
    @Modifying(clearAutomatically = true)
    @Transactional
    void saveNative(long userId, long playgroundId, int status, LocalDateTime updatedAt);

    List<Entry> findAllByUserIdAndStatus(Long id, int status);

    @Transactional
    @Modifying(clearAutomatically = true)
    void deleteByIdAndStatus(long entryId, int i);

    List<Entry> findAllByStatusOrderByPlaygroundId(long i);

    int countByPlaygroundIdAndStatus(long pgId, int i);

    @Query(value = "INSERT INTO entry_list(id, user_id, playground_id,status) values (FLOOR(RAND()*(999999)+100000), :userId, :playgroundId ,:status)", nativeQuery = true)
    @Modifying(clearAutomatically = true)
    @Transactional
    void saveByUserIdAndPlaygroundIdAndStatus(long userId, long playgroundId, int status);

    Optional<Entry> findByUserIdAndPlaygroundIdAndStatus(long id, long playgroundId, int i);


    @Query("select e.userId from Entry e where e.id = ?1")
    Long findUserIdById(long entryId);

    //@Query("select e from Entry e where e.id = ?1 and e.status = 1")
    Optional<Entry> findByUserIdAndStatus(Long userId, int i);
}


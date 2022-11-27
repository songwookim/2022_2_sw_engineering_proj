package com.example.finalprj.api.controller;

import com.example.finalprj.api.dao.PhotoDao;
import com.example.finalprj.api.dao.TrashDao;
import com.example.finalprj.api.dto.Photo;
import com.example.finalprj.api.dto.Trash;
import com.example.finalprj.db.domain.Dog;
import com.example.finalprj.db.domain.Entry;
import com.example.finalprj.db.domain.User;
import com.example.finalprj.db.service.DogService;
import com.example.finalprj.db.service.EntryService;
import com.example.finalprj.db.service.PlaygroundService;
import com.example.finalprj.db.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {
    private final PlaygroundService playgroundService;
    private final EntryService entryService;
    private final UserService userService;

    @PostMapping("/photo")
    public void insert(@RequestBody Photo photo) {
        PhotoDao photoDao = new PhotoDao();
        photoDao.photoInsert(photo);
    }

    @GetMapping("/photo")
    public String getList() {
        PhotoDao photoDao = new PhotoDao();
        return photoDao.json();
    }

    @GetMapping("/trash/get")
    public String getTrash() {
        TrashDao dao = new TrashDao();

        return dao.json();
    }

    @GetMapping("/trash/put")
    public void updateTrash(@RequestParam Integer status, @RequestParam Long id) {
        Trash trash = Trash.builder()
                .id(id)
                .status(status)
                .build();

        TrashDao dao = new TrashDao();
        dao.updateTrash(trash);
    }

    @GetMapping("/playground/get")
    public String getGeo() {
        return playgroundService.json();
    }

    @PutMapping("/playground/put")
    public String setGeo() throws IOException, ParserConfigurationException, SAXException, SQLException, ClassNotFoundException {
        return playgroundService.playgroundInsert() +"건 실행 완료 !";
    }

    @DeleteMapping("/playground/delete")
    public String deleteGeo() {
        return playgroundService.deleteAll() +"건 실행 완료 !";
    }


    @GetMapping("/reservation/get")
    public String reservation(String reservationNumber) {
        long entryId = Long.valueOf(reservationNumber);
        Long userId = entryService.findUserIdById(entryId);
        System.out.println(userId);
        if(userId == null) {
            return userService.getJson(new User());
        }
        User user = userService.findById(userId).orElse(null);
        entryService.entryUser(user.getId(),1);
        return userService.getJson(user);
    }

    @GetMapping("/reservationStatus/get/")
    public Entry reservationStatus(String email) {
        Long userId = userService.findIdByEmail(email);
        Entry entry = null;

        if(userId != null) {
            entry = entryService.findByUserIdAndStatus(userId,1).orElse(null); // 예약중
//            if(entry != null) {
//                return entry;
//            }
        }
        return entry;
    }
}

package com.example.finalprj.api.controller;

import com.example.finalprj.api.dao.PhotoDao;
import com.example.finalprj.api.dto.Photo;
import com.example.finalprj.db.domain.*;
import com.example.finalprj.db.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final TrashService trashService;
    private final AlertService alertService;

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

//    @GetMapping("/trash/get")
//    public String getTrash() {
//        TrashDao dao = new TrashDao();
//
//        return dao.json();
//    }
//
//    @GetMapping("/trash/put")
//    public void updateTrash(@RequestParam Integer status, @RequestParam Long id) {
//        Trash trash = Trash.builder()
//                .id(id)
//                .status(status)
//                .build();
//
//        TrashDao dao = new TrashDao();
//        dao.updateTrash(trash);
//    }

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


    @GetMapping("/trash")
    public ResponseEntity<Integer> trash_sensor() {
        Trash trash = trashService.findById(1L).get();
        Integer flag = trash.getStatus();
        if (flag == 1) {
            trash.setStatus(0);
            trashService.updateTrash(trash);
            return ResponseEntity.ok()
                    .header("this is Header")
                    .body(1);
        }
        return ResponseEntity.ok()
                .header("this is Header")
                .body(0);
    }


    @GetMapping("/alert")
    public ResponseEntity<Integer> basic_sensor() {
        Alert alert = alertService.findById(1L).get();
        int flag = alert.getStatus();
        if (flag == 1) {
            alert.setStatus(0);
            alertService.updateAlert(alert);
            return ResponseEntity.ok()
                    .header("this is Header")
                    .body(1);
        }
        return ResponseEntity.ok()
                .header("this is Header")
                .body(0);
    }

    /////////// not JSON /////////////
    // 1. 시스템
    
    // (시스템) 배변봉투감지센서
    @GetMapping("/trash/{status}")
    public String updateTrashStatus(@PathVariable int status) {
        Trash trash = new Trash();
        trash.setId(1L);
        trash.setStatus(status);
        trashService.updateTrash(trash);
        return "1건이 실행되었습니다.";
    }

    // (앱) 사용자 호출 
    @GetMapping("/alert/{status}")
    public String updateAlertStatus(@PathVariable int status) {
        Alert alert = new Alert();
        alert.setId(1L);
        alert.setStatus(status);
        alertService.updateAlert(alert);

        return "1건이 실행되었습니다.";
    }
//
//    // (시스템) RFID 입장 (dogregNum으로 회원/비회원 구분)
//    @GetMapping("/entry/{dogregNum}")
//    public String saveEntryByDogRegNum(@PathVariable int dogregNum) {
//        // entry에 이용중인 상태일 경우 퇴장처리
//        return "1건이 실행되었습니다.";
//    }
//
//    // (시스템) 회원가입(회원,비회원) 입장처리 API
//    @GetMapping("/entry/{dogregNum}")
//    public void registUser() {
//        // 회원가입(회원,비회원) 입장처리 API + QueryParamter
//    }
//
//    // (앱) 'RFID 입력 해주세요' 에서 Get으로 계속해서 요청
//    @GetMapping("/entry/{dogregNum}")
//    public int checkEntryIn(@PathVariable int dogregNum) {
//        // 성공 시 1 반환
//        return 1;
//    }
}

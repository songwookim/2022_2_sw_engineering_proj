package com.example.finalprj.web.user.controller;

import com.example.finalprj.db.domain.*;
import com.example.finalprj.db.service.EntryService;
import com.example.finalprj.db.service.NoticeService;
import com.example.finalprj.db.service.PhotoService;
import com.example.finalprj.db.service.UserService;
import com.example.finalprj.web.user.controller.vo.NoticeForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final UserService userService;
    private final EntryService entryService;
    private final PhotoService photoService;
    private final NoticeService noticeService;

    @GetMapping("/usage")
    public String usage(@AuthenticationPrincipal User user, Model model) {
        long playgroundId = user.getPlayground().getId();

        List<Long> userIds = entryService.findAllUserIdByPlaygroundIdAndStatusEqual(playgroundId, 2); // 이용중인 사람들 목록
        List<User> users = new ArrayList<>();
        for (Long id : userIds) {
            users.add(userService.findById(id).orElse(null));
        }

        model.addAttribute("users", users);
        model.addAttribute("site", "usage");
        model.addAttribute("url", "manager");
        return "usage";
    }

    @PutMapping("/usage")
    public String usage(@AuthenticationPrincipal User user, long userId) {
        long playgroundId = user.getPlayground().getId();
        entryService.exitUser(userId, playgroundId);

        return "redirect:/manager/usage";
    }

    /////////////////////예약////////////////////////////////////

    @GetMapping("/reservation")
    public String reservation(@AuthenticationPrincipal User user, Model model) {
        long playgroundId = user.getPlayground().getId();

        List<Long> userIds = entryService.findAllUserIdByPlaygroundIdAndStatusEqual(playgroundId, 1);
        List<User> users = new ArrayList<>();
        for (Long id : userIds) {
            users.add(userService.findById(id).orElse(null));
        }
        model.addAttribute("users", users);
        model.addAttribute("site", "reservation");
        model.addAttribute("url", "manager");

        return "reservation";
    }

    @PutMapping("/reservation")
    public String reservation1(@AuthenticationPrincipal User user, long userId) {
        long playgroundId = user.getPlayground().getId();
        entryService.entryUser(userId, playgroundId);

        return "redirect:/manager/reservation";
    }

    @DeleteMapping("/reservation")
    public String reservation2(@AuthenticationPrincipal User user, long userId) {
        long playgroundId = user.getPlayground().getId();

        entryService.deleteUserIdStatusEquals(userId, playgroundId, 1); // status가 1인 userid 찾아서 지우기

        return "redirect:/manager/reservation";
    }

    //////////////////////사진/////////////////////////////////
    @GetMapping("/images")
    public String images(@AuthenticationPrincipal User user, Model model) {
        List<Photo> images = photoService.getPhotos();

        model.addAttribute("images", images);
        model.addAttribute("site", "images");
        model.addAttribute("url", "manager");

        return "images";
    }

    @DeleteMapping("/images")
    public String images(Long id) {
        photoService.deleteById(id);

        return "redirect:/manager/images";
    }

    //////////////////////////////////////////////////////////
    //////////////////////공지사항/////////////////////////////////
    @GetMapping("/notices")
    public String notices(Model model) {
        List<Notice> notices = noticeService.findAll();

        model.addAttribute("notices", notices);
        model.addAttribute("site", "notices");
        model.addAttribute("url", "manager");
        return "notices";
    }

    @PostMapping(value = "/notices", consumes = {"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String notices(NoticeForm noticeForm, Model model) {
        Notice notice = Notice.builder()
                .subject(noticeForm.getSubject())
                .content(noticeForm.getContent())
                .build();
        noticeService.save(notice);

        model.addAttribute("site", "notices");
        model.addAttribute("complete", "완료");
        return "redirect:/manager/notices";
    }

    @DeleteMapping("/notices")
    public String notices(Long id) {
        noticeService.deleteById(id);
        return "redirect:/manager/notices";
    }

    @PutMapping(value = "/notices")
    public String notices(Notice notice) {
        noticeService.updateNotice(notice);
        return "redirect:/manager/notices";
    }

    //////////////////////////////////////////////////////////
    @GetMapping("/list")
    public String list(@AuthenticationPrincipal User user, Model model) {

        long playgroundId = user.getPlayground().getId();

        List<Entry> entries = entryService.findAllByPlaygroundIdAndStatusEqual(playgroundId,3);  // 놀이터 1에 출입한 기록 있는 user_id 시산 순서로 불러오기
        List<User> users = new ArrayList<>();

        entries.forEach(e -> {
            long tempId = e.getUserId();
            User tempUser = userService.findById(tempId).orElse(null);
            users.add(tempUser);
        });

        model.addAttribute("users", users);
        model.addAttribute("entries", entries);
        model.addAttribute("site", "list");
        model.addAttribute("url", "manager");
        model.addAttribute("myId", playgroundId);
        return "list";
    }

}

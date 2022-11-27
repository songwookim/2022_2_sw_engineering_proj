package com.example.finalprj.web.controller;


import com.example.finalprj.db.domain.*;
import com.example.finalprj.db.repository.PlaygroundRepository;
import com.example.finalprj.db.service.*;
import com.example.finalprj.web.controller.vo.UserSignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final PasswordEncoder passwordEncoder;
    private final PlaygroundService playgroundService;
    private final UserService userService;
    private final PlaygroundRepository playgroundRepository;
    private final EntryService entryService;
    private final FaqService faqService;
    private final DogService dogService;

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Value("${api.key.kakao.map}")
    private String kakaokey;
    @Value("${api.key.public.dog}")
    private String publickey;

    @GetMapping("/")
    public String main() {
        return "index";
    }

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal User user, @RequestParam(value = "error", defaultValue = "false") Boolean error, HttpServletRequest request, Model model) {


        if (user != null && user.isEnabled()) {
            if (user.getAuthorities().contains(Authority.ADMIN_AUTHORITY)) {
                return "redirect:/admin";
            } else if (user.getAuthorities().contains(Authority.MANAGER_AUTHORITY)) {
                Long id = user.getPlayground().getId();
                return String.format("redirect:/main/") + id; // 각각 관리하는 페이지로 이동
            } else {
                return "redirect:/main";
            }
        }
        SavedRequest savedRequest = requestCache.getRequest(request, null);

        model.addAttribute("error", error);
        return "loginForm";
    }

    @GetMapping("/signup")
    public String signUp(@RequestParam String site, @RequestParam(value = "error", defaultValue = "false") Boolean error, Model model) {
        if (site.equals("manager")) {
            List<Playground> pg = playgroundService.getPlaygroundList();
            model.addAttribute("pgList", pg);
        }
        model.addAttribute("error", error);
        model.addAttribute("site", site);
        return "signUp";
    }


    @PostMapping(value = "/signup", consumes = {"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String signUp(@RequestParam String site, UserSignUpForm form, Model model) {
        final User user = User.builder()
                .name(form.getName())
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .enabled(true)
                .build();
        if (site.equals("manager")) {
            userService.findPlaygroundByPgName(form.getPgName()).ifPresent(pg -> {
                user.setPlayground(pg);
                User saved = userService.save(user);

                Playground playground = playgroundRepository.findById(saved.getPlayground().getId()).get();
                playground.setUser(saved);
                playgroundRepository.save(playground);

                userService.addAuthority(saved.getId(), Authority.ROLE_MANAGER);
            });
        } else {
            String dogNum = form.getDogNum();
            String url = "http://openapi.animal.go.kr/openapi/service/rest/animalInfoSrvc/animalInfo?serviceKey=7kqT2Nea62adPrDr0V1FXr0WoXDiLx4zaGcMwyBf70HABw69mhV0ysvLGtZM6EQvWun91SrKFIoN1lV6zH1lFA%3D%3D&";
            String info = "dog_reg_no=%s&rfid_cd=%s&owner_birth=%s";

            info = String.format(info, dogNum, dogNum, form.getBirth());
            url += info;
            if(userService.get(url)== null) {
                return "redirect:/signup?site=user&error=true";
            }
            Dog dog = userService.get(url);
            dogService.save(dog);
            user.setDogNum(dogNum);
            user.setDog(dog);
            User saved = userService.save(user);

            userService.addAuthority(saved.getId(), Authority.ROLE_USER);
        }

        model.addAttribute("register", true);
        return "loginForm";
    }

    @GetMapping("/main")
    public String mainPage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("site", "main");
        List<Faq> faqs = faqService.findAll();

        if (user.getPlayground() != null) {
            long playgroundId = user.getPlayground().getId();
            return "redirect:/main/" + playgroundId;
        }

        model.addAttribute("faqs", faqs);
        model.addAttribute("url", "main");
        model.addAttribute("data", playgroundService.json());
        model.addAttribute("kakaokey", kakaokey);
        return "main";
    }

    @GetMapping("/main/{id}")
    public String mainPage(@AuthenticationPrincipal User user, @PathVariable(name = "id") long playgroundId, Model model) {
        Playground playground = playgroundService.findById(playgroundId).get();

        if (user.getAuthorities().contains(Authority.ADMIN_AUTHORITY)) { // 관리자페이지
            user.setPlayground(playground);
            userService.save(user);
        }

        List<Entry> entriesUsing = entryService.findAllByPlaygroundIdAndStatusEqual(playgroundId, 2);
        List<Entry> entriesReservation = entryService.findAllByPlaygroundIdAndStatusEqual(playgroundId, 1);

        Entry entryUserHavingReservation = entryService.findByUserIdAndPlaygroundIdAndStatus(user.getId(), playgroundId, 1).orElse(null);

        model.addAttribute("entry", entryUserHavingReservation);
        model.addAttribute("reservationNum", entriesReservation.size());
        model.addAttribute("userNum", entriesUsing.size());
        model.addAttribute("pg", playground);
        model.addAttribute("site", "main");
        model.addAttribute("url", "manager");
        model.addAttribute("id", playgroundId);
        model.addAttribute("kakaokey", kakaokey);
        return "main";
    }

    @GetMapping("/search")
    public String search(String search) {
        Playground playground = playgroundService.findByPgName(search).orElse(null);
        if(playground == null) {
            return "noSearchResult";
        }

        return "redirect:/main/"+ playground.getId();
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "accessDenied";
    }
}

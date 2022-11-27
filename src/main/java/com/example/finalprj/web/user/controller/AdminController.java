package com.example.finalprj.web.user.controller;

import com.example.finalprj.db.domain.Faq;
import com.example.finalprj.db.domain.Playground;
import com.example.finalprj.db.domain.User;
import com.example.finalprj.db.repository.PlaygroundRepository;
import com.example.finalprj.db.service.FaqService;
import com.example.finalprj.db.service.PlaygroundService;
import com.example.finalprj.db.service.UserService;
import com.example.finalprj.web.user.controller.vo.FaqForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final FaqService faqService;
    private final UserService userService;
    private final PlaygroundService playgroundService;
    private final PlaygroundRepository playgroundRepository;

    @GetMapping("")
    public String admin(Model model) {
        List<Playground> playgrounds = playgroundService.findAll();
        model.addAttribute("playgrounds", playgrounds);
        model.addAttribute("site", "admin");
        model.addAttribute("url", "admin");
        return "admin";
    }

    @GetMapping("/faqs")
    public String faqs(Model model) {
        List<Faq> faqs = faqService.findAll();

        model.addAttribute("faqs", faqs);
        model.addAttribute("site", "faqs");
        model.addAttribute("url", "admin");
        return "faqs";
    }

    @PostMapping(value = "/faqs", consumes = {"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String faqs(FaqForm faqForm, Model model) {
        Faq faq = Faq.builder()
                .subject(faqForm.getSubject())
                .content(faqForm.getContent())
                .build();
        faqService.save(faq);

        model.addAttribute("site", "faqs");
        model.addAttribute("complete", "완료");
        return "redirect:/admin/faqs";
    }

    @DeleteMapping("/faqs")
    public String faqs(Long id) {
        faqService.deleteById(id);
        return "redirect:/admin/faqs";
    }

    @PutMapping(value = "/faqs")
    public String faqs(Faq faq) {
        faqService.updateFaq(faq);
        return "redirect:/admin/faqs";
    }

    @GetMapping("/accounts")
    public String accounts(@AuthenticationPrincipal User user, Model model) {
        if(user.getPlayground() != null) {
            user.setPlayground(null);
            userService.save(user);
        }

        List<User> managers = userService.findAllManager();

        model.addAttribute("managers", managers);
        model.addAttribute("site", "accounts");
        model.addAttribute("url", "admin");
        return "accounts";
    }

    @DeleteMapping("/accounts")
    public String accounts(Long id) {
        User user = userService.findById(id).get();
        Playground pg = playgroundRepository.findById(user.getPlayground().getId()).get();
        pg.setUser(null);
        playgroundRepository.save(pg);

        userService.deleteById(id);
        return "redirect:/admin/accounts";
    }
}

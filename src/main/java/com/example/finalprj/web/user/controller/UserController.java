package com.example.finalprj.web.user.controller;

import com.example.finalprj.db.domain.Entry;
import com.example.finalprj.db.domain.Playground;
import com.example.finalprj.db.domain.User;
import com.example.finalprj.db.service.EntryService;
import com.example.finalprj.db.service.PlaygroundService;
import com.example.finalprj.db.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EntryService entryService;
    private final PlaygroundService playgroundService;

    @GetMapping("/reservation")
    public String reservation(@AuthenticationPrincipal User user, Model model) {
        List<Entry> entries = entryService.findAllByUserIdAndStatus(user.getId(), 1);
        List<Playground> playgrounds = playgroundService.findAll();
        List<Integer> nums = new ArrayList<>();

        playgrounds.forEach(p -> {
            Integer temp = entryService.countByPlaygroundIdAndStatus(p.getId(), 1);
            if (temp == null) {
                nums.add(0);
            } else {
                nums.add(temp);
            }
        });
        model.addAttribute("entries", entries);
        model.addAttribute("playgrounds", playgrounds);
        model.addAttribute("nums", nums);
        model.addAttribute("site", "reservation");
        return "user/reservation.html";
    }

    @PostMapping("/reservation")
    public String reservation(@AuthenticationPrincipal User user, @RequestParam long playgroundId) {
        entryService.save(user.getId(), playgroundId, 1);

        return "redirect:/main/" + playgroundId;
    }

    @DeleteMapping("/reservation")
    public String reservation(long id, Integer playgroundId) {
        entryService.deleteByIdAndStatus(id, 1);
        if (playgroundId != null) {
            return "redirect:/main/" + playgroundId;
        }

        return "redirect:/user/reservation";
    }

    @GetMapping("/list")
    public String list(@AuthenticationPrincipal User temp, Model model) {
        List<Entry> entries = entryService.findAllByUserIdAndStatus(temp.getId(), 3);
        List<Playground> playgrounds = new ArrayList<>();
        entries.forEach(e -> {
            long id = e.getPlaygroundId();
            playgrounds.add(playgroundService.findById(id).get());
        });

        model.addAttribute("entries", entries);
        model.addAttribute("playgrounds", playgrounds);
        model.addAttribute("site", "list");
        return "user/list.html";
    }
}

package vn.hcmute.eatandorder.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.hcmute.eatandorder.model.User;
import vn.hcmute.eatandorder.repository.UserRepository;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProfileController {

    private final UserRepository repo;

    @GetMapping("/{id}")
    public User getProfile(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }
}

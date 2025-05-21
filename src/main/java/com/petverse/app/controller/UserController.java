package com.petverse.app.controller;

import com.petverse.app.dto.PetRequest;
import com.petverse.app.dto.UpdateProfileRequest;
import com.petverse.app.dto.UserPrincipal;
import com.petverse.app.entity.Pet;
import com.petverse.app.entity.User;
import com.petverse.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        User user = userService.getCurrentUser(principal.getId());
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    // 3. ล็อกเอาต์
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        userService.logout(token);
        return ResponseEntity.ok().build();
    }

    // 4. แก้ไขข้อมูลส่วนตัว
    @PutMapping("/profile")
    public ResponseEntity<User> updateProfile(@RequestBody UpdateProfileRequest request, @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(userService.updateProfile(principal.getId(), request));
    }

    // 5. เพิ่มสัตว์เลี้ยง
    @PostMapping("/pets")
    public ResponseEntity<Pet> addPet(@RequestBody PetRequest request, @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(userService.addPet(principal.getId(), request));
    }

    // 6. ลบสัตว์เลี้ยง
    @DeleteMapping("/pets/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable Long petId, @AuthenticationPrincipal UserPrincipal principal) {
        userService.deletePet(principal.getId(), petId);
        return ResponseEntity.ok().build();
    }

    // 7. แก้ไขสัตว์เลี้ยง
    @PutMapping("/pets/{petId}")
    public ResponseEntity<Pet> updatePet(@PathVariable Long petId, @RequestBody PetRequest request, @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(userService.updatePet(principal.getId(), petId, request));
    }
}


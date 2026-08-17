package com.spring.job_portal_backend.user;

import com.spring.job_portal_backend.dto.UserDto;
import com.spring.job_portal_backend.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path="/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping(path="/search/admin")
    public ResponseEntity<?> searchUserByEmail(@RequestParam String email) {
        Optional<UserDto> userOptional = userService.searchUserByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found with email: " + email));
        }
        return ResponseEntity.ok(userOptional.get());
    }

    @PatchMapping(path="/{userId}/role/employer/admin", version = "1.0")
    public ResponseEntity<?> elevateToEmployer(@PathVariable Long userId) {
        UserDto updatedUser = userService.promoteToEmployer(userId);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping(path="/{userId}/company/{companyId}/admin", version = "1.0")
    public ResponseEntity<?> assignCompanyToEmployer(
            @PathVariable Long userId, @PathVariable Long companyId) {
        UserDto updatedUser = userService.assignCompanyToEmployer(userId, companyId);
        return ResponseEntity.ok(updatedUser);
    }
}

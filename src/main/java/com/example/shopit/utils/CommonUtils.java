package com.example.shopit.utils;

import com.example.shopit.dto.response.UserResponse;
import com.example.shopit.entity.User;
import com.example.shopit.repository.RepositoryAccessor;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class CommonUtils {
    public static Optional<User> getLoggedInUser() {
        // Fetching details from token
        UserResponse userResponse =
                (UserResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return RepositoryAccessor.getUserRepository().findByIdAndIsActiveAndIsDeleted(userResponse.getId(), true, false);
    }
}

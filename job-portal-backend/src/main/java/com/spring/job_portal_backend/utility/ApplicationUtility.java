package com.spring.job_portal_backend.utility;

import com.spring.job_portal_backend.constants.ApplicationConstants;
import com.spring.job_portal_backend.entity.JobPortalUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ApplicationUtility {

    public static String getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("AnonymousUser")) {
            return "Anonymous User";
        }

        Object principle = authentication.getPrincipal();
        String username = null;

        if (principle instanceof JobPortalUser jobPortalUser) {
            username = jobPortalUser.getEmail();
        } else {
            username = principle.toString();
        }
        return username;
    }
}

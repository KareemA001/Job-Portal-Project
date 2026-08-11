package com.spring.job_portal_backend.audit;

import com.spring.job_portal_backend.utility.ApplicationUtility;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(value="auitorAwareImpl")
public class AuitorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(ApplicationUtility.getLoggedInUser());
    }
}

package com.spring.job_portal_backend.constants;


public class ApplicationConstants {

    private ApplicationConstants() {
        throw new AssertionError("Utility classes can't be instantiated");
    }

    public static final String JWT_SECRET_KEY = "JWT_SECRET";
    public static final String JWT_SECRET_DEFAULT_VALUE = "jxgEQeXHuPq8VdbyYFNkANdudQ53YUn4";
    public static final String JWT_HEADER = "Authorization";
    public static final String ROLE_JOB_SEEKER = "ROLE_JOB_SEEKER";
    public static final String ACTIVE_STATUS = "ACTIVE";
    public static final String NEW_STATUS = "NEW";
    public static final String CLOSED_STATUS = "CLOSED";
    public static final String ROLE_EMPLOYER = "ROLE_EMPLOYER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String PENDING_STATUS = "PENDING";
}

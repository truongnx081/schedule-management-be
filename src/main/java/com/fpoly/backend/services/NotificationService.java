package com.fpoly.backend.services;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface NotificationService {
    List<Map<String, Object>> getAllNotificationByStudentId();

    List<Map<String, Object>> getAllNotificationsByAdminId();

    List<Map<String, Object>> getAllNotificationsByInstructorId();
}

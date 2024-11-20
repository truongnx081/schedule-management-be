package com.fpoly.backend.services.impl;

import com.fpoly.backend.repository.NotificationRepository;
import com.fpoly.backend.services.IdentifyUserAccessService;
import com.fpoly.backend.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    IdentifyUserAccessService identifyUserAccessService;
    @Override
    public List<Map<String, Object>> getAllNotificationByStudentId() {
        Integer studentId = identifyUserAccessService.getStudent().getId();
        return notificationRepository.getAllNotificationsByStudentId(studentId);
    }

    @Override
    public List<Map<String, Object>> getAllNotificationsByAdminId() {
        Integer adminId = identifyUserAccessService.getAdmin().getId();
        return notificationRepository.getAllNotificationsByAdminId(adminId);
    }

    @Override
    public List<Map<String, Object>> getAllNotificationsByInstructorId() {
        Integer instructorId = identifyUserAccessService.getInstructor().getId();
        return notificationRepository.getAllNotificationsByInstructorId(instructorId);
    }
}

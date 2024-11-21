package com.fpoly.backend.services;

import com.fpoly.backend.dto.AdminDTO;
import com.fpoly.backend.entities.Admin;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {
    Admin findById(Integer id);
    AdminDTO getAdminInfor();
}

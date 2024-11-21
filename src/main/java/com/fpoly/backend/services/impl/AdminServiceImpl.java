package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.AdminDTO;
import com.fpoly.backend.dto.StudentDTO;
import com.fpoly.backend.entities.Admin;
import com.fpoly.backend.entities.Student;
import com.fpoly.backend.mapper.AdminMapper;
import com.fpoly.backend.repository.AdminRepository;
import com.fpoly.backend.services.AdminService;
import com.fpoly.backend.services.IdentifyUserAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminRepository adminRepository;
    @Autowired
    private IdentifyUserAccessService identifyUserAccessService;
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin findById(Integer id) {
        return adminRepository.findById(id).orElse(null);
    }

    @Override
    public AdminDTO getAdminInfor() {
        Admin admin = identifyUserAccessService.getAdmin();
        return adminMapper.toDTO(admin);
    }
}

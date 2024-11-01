package com.fpoly.backend.services.impl;

import com.fpoly.backend.entities.Admin;
import com.fpoly.backend.repository.AdminRepository;
import com.fpoly.backend.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminRepository adminRepository;

    @Override
    public Admin findById(Integer id) {
        return adminRepository.findById(id).orElse(null);
    }
}

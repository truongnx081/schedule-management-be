package com.fpoly.backend.services.impl;

import com.fpoly.backend.entities.Area;
import com.fpoly.backend.repository.AreaRepository;
import com.fpoly.backend.services.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl implements AreaService {

    @Autowired
    AreaRepository areaRepository;
    @Override
    public Area findById(Integer id) {
        return areaRepository.findById(id).orElse(null);
    }
}

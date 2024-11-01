package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.BlockDTO;
import com.fpoly.backend.entities.Block;
import com.fpoly.backend.mapper.BlockMapper;
import com.fpoly.backend.repository.BlockRepository;
import com.fpoly.backend.services.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlockServiceImpl implements BlockService {

    @Autowired
    BlockRepository blockRepository;

    @Autowired
    BlockMapper blockMapper;

    @Override
    public Block findById(Integer block) {
        return blockRepository.findById(block).orElse(null);
    }

    @Override
    public List<BlockDTO> getAllBlocks() {
        return blockRepository.findAll().stream().map(blockMapper::toDTO).collect(Collectors.toList());
    }


}

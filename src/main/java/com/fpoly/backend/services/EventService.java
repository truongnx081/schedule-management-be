package com.fpoly.backend.services;

import com.fpoly.backend.dto.EventDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {
    public List<EventDTO> getAllEvent();
}

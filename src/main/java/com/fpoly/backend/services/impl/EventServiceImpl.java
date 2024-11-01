package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.EventDTO;
import com.fpoly.backend.mapper.EventMapper;
import com.fpoly.backend.repository.EventRepository;
import com.fpoly.backend.services.EventService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class EventServiceImpl implements EventService {

    EventRepository eventRepository;
    EventMapper eventMapper;
    @Override
    public List<EventDTO> getAllEvent() {
        return eventRepository.findAll().stream().map(eventMapper::toDTO).toList();
    }
}

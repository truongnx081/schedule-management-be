package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.RoomDTO;
import com.fpoly.backend.entities.Room;
import com.fpoly.backend.repository.RoomRepository;
import com.fpoly.backend.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    @Autowired
    RoomRepository roomRepository;

    @Override
    public Room findById(Integer id) {
        return roomRepository.findById(id).orElse(null);
    }

    @Override
    public List<Map<String,Object>> getAvailableRooms(Integer buildingId, LocalDate date) {
        return roomRepository.findAvailableRooms(buildingId, date);

    }
}

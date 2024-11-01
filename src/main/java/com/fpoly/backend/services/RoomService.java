package com.fpoly.backend.services;

import com.fpoly.backend.entities.Room;
import org.springframework.stereotype.Service;

@Service
public interface RoomService {
    Room findById(Integer id);
}

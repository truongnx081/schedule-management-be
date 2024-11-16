package com.fpoly.backend.controller;

import com.fpoly.backend.dto.BlockDTO;
import com.fpoly.backend.dto.Response;
import com.fpoly.backend.dto.RoomDTO;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.services.BlockService;
import com.fpoly.backend.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
@Validated
public class RoomController {

    @Autowired
    RoomService roomService;

    //Get All room available
    @GetMapping("/available")
    public ResponseEntity<Response> getAllBlock(@RequestParam Integer buildingId,
                                                @RequestParam LocalDate date){
        try{
            List<Map<String,Object>> availableRooms = roomService.getAvailableRooms(buildingId, date);
            return ResponseEntity.ok(new Response(
                    LocalDateTime.now(),
                    availableRooms,
                    "Get room available successful",
                    HttpStatus.OK.value()));
        }catch (AppUnCheckedException e){
            return ResponseEntity.status(e.getStatus()).body(new Response(LocalDateTime.now(),null,e.getMessage(),e.getStatus().value()));
        }

    }
}

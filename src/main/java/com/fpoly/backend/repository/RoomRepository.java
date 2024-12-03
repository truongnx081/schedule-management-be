package com.fpoly.backend.repository;

import com.fpoly.backend.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room,Integer> {
    @Query("SELECT r.id AS roomId, r.name AS roomName " +
            "FROM Room r " +
            "LEFT JOIN Schedule sch ON sch.clazz.room.id = r.id AND sch.date = :date " +
            "LEFT JOIN ExamSchedule esch ON esch.room.id = r.id AND esch.date = :date " +
            "LEFT JOIN RetakeSchedule rsch ON rsch.room.id = r.id AND rsch.date = :date " +
            "WHERE r.building.id = :buildingId " +
            "GROUP BY r.id, r.name " +
            "HAVING COUNT(sch.id) + COUNT(esch.id) + COUNT(rsch.id) < 6")
    List<Map<String, Object>> findAvailableRooms(@Param("buildingId") Integer buildingId,
                                                 @Param("date") LocalDate date);

    List<Room> findAllByBuildingAreaId(Integer AreaId);

    Optional<Room> findByName(String name);
}

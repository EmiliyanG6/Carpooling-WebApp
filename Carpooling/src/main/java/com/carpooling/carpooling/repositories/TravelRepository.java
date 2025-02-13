package com.carpooling.carpooling.repositories;


import com.carpooling.carpooling.enums.TravelStatus;
import com.carpooling.carpooling.models.Travel;
import com.carpooling.carpooling.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {
    List<Travel> findByDriverId(Long driverId);
    List<Travel> findByStartPointContainingIgnoreCase(String startPoint);
    List<Travel> findByEndPointContainingIgnoreCase(String endPoint);
    List<Travel> findByStartPointAndEndPointAndDepartureTime(
            String startingPoint, String endingPoint, LocalDateTime departureTime);
    boolean existsByDriverAndStartPointAndEndPointAndDepartureTime(
            User driver, String startingPoint, String endingPoint, LocalDateTime departureTime);

    Page<Travel> findByStartPoint(String filterValue, Pageable pageable);

    Page<Travel> findByEndPoint(String filterValue, Pageable pageable);

    @Query(value = "SELECT * FROM travels WHERE driver_id = :userId ORDER BY departure_time DESC",
            countQuery = "SELECT COUNT(*) FROM travels WHERE driver_id = :userId",
            nativeQuery = true)
    Page<Travel> findByDriver(@Param("userId") long userId, Pageable pageable);

    List<Travel> findByStatus(TravelStatus status);
}


package com.carpooling.carpooling.repositories;


import com.carpooling.carpooling.models.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {
    List<Travel> findByDriverId(Long driverId);
    List<Travel> findByStartPointContainingIgnoreCase(String startPoint);
    List<Travel> findByEndPointContainingIgnoreCase(String endPoint);

}

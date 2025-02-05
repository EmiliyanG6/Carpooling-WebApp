package com.carpooling.carpooling.helpers;

import com.carpooling.carpooling.models.Dtos.TravelDto;
import com.carpooling.carpooling.models.Travel;
import com.carpooling.carpooling.services.interfaces.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TravelMapper {

    private final TravelService travelService;

    @Autowired
    public TravelMapper(TravelService travelService) {
        this.travelService = travelService;
    }

    public Travel fromDto(long id, TravelDto travelDto) {
        Travel travel = fromDto(travelDto);
        travel.setId(id);
        Travel repositoryTravel = travelService.getTravelById(id);
        travel.setDriver(repositoryTravel.getDriver());
        return travel;
    }
    public Travel fromDto(TravelDto travelDto) {
        Travel travel = new Travel();
        travel.setStartPoint(travelDto.getStartingPoint());
        travel.setEndPoint(travelDto.getEndingPoint());
        travel.setDepartureTime(travelDto.getDepartureTime());
        travel.setFreeSpots(travelDto.getFreeSpots());
        travel.setComment(travelDto.getComment());
        return travel;
    }
    public TravelDto toDto(Travel travel) {
        TravelDto travelDto = new TravelDto();
        travelDto.setStartingPoint(travel.getStartPoint());
        travelDto.setEndingPoint(travel.getEndPoint());
        travelDto.setDepartureTime(travel.getDepartureTime());
        travelDto.setFreeSpots(travel.getFreeSpots());
        travelDto.setComment(travel.getComment());
        return travelDto;
    }

}

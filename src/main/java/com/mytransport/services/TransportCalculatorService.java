package com.mytransport.services;

import com.mytransport.models.OceanFreightEntity;
import com.mytransport.models.dto.TransportCalculationRequest;
import com.mytransport.repository.OceanFreightRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransportCalculatorService {

    private final OceanFreightRepository oceanFreightRepository;


    public TransportCalculatorService(OceanFreightRepository oceanFreightRepository) {
        this.oceanFreightRepository = oceanFreightRepository;
    }


    public double calculate (TransportCalculationRequest request){
        Optional<OceanFreightEntity> basePriceOpt = oceanFreightRepository
                .findAll()
                .stream()
                .filter(p -> p.getOriginPort().equalsIgnoreCase(request.getOriginPort())
                        && p.getDestinationPort().equalsIgnoreCase(request.getDestinationPort())
                        && p.getVehicleType().equalsIgnoreCase(request.getVehicleType()))
                .findFirst();


        if (basePriceOpt.isEmpty()) return -1;

        double price = basePriceOpt.get().getPrice();

        if (request.isHybrid()){
            price += 300;
        }

        if (request.isElectric()){
            price += 300;
        }

        return price;
    }
}

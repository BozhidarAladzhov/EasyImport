package com.mytransport.services;

import com.mytransport.models.TransportPriceEntity;
import com.mytransport.models.dto.TransportCalculationRequest;
import com.mytransport.repository.TransportPriceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransportCalculatorService {

    private final TransportPriceRepository transportPriceRepository;


    public TransportCalculatorService(TransportPriceRepository transportPriceRepository) {
        this.transportPriceRepository = transportPriceRepository;
    }


    public double calculate (TransportCalculationRequest request){
        Optional<TransportPriceEntity> basePriceOpt = transportPriceRepository
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

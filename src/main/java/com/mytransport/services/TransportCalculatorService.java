package com.mytransport.services;

import com.mytransport.models.DomesticRotterdamEntity;
import com.mytransport.models.OceanFreightEntity;
import com.mytransport.models.TerminalHandlingEntity;
import com.mytransport.models.dto.TransportCalculationRequest;
import com.mytransport.repository.DomesticRotterdamRepository;
import com.mytransport.repository.OceanFreightRepository;
import com.mytransport.repository.TerminalHandlingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransportCalculatorService {

    private final OceanFreightRepository oceanFreightRepository;
    private final TerminalHandlingRepository terminalHandlingRepository;
    private final DomesticRotterdamRepository domesticRotterdamRepository;


    public TransportCalculatorService(OceanFreightRepository oceanFreightRepository, TerminalHandlingRepository terminalHandlingRepository, DomesticRotterdamRepository domesticRotterdamRepository) {
        this.oceanFreightRepository = oceanFreightRepository;
        this.terminalHandlingRepository = terminalHandlingRepository;
        this.domesticRotterdamRepository = domesticRotterdamRepository;
    }


    public double calculateOceanFreight(TransportCalculationRequest request){
        Optional<OceanFreightEntity> basePriceOpt = oceanFreightRepository
                .findAll()
                .stream()
                .filter(p -> p.getOriginPort().equalsIgnoreCase(request.getOriginPort())
                        && p.getDestinationPort().equalsIgnoreCase(request.getDestinationPort())
                        && p.getVehicleType().equalsIgnoreCase(request.getVehicleType()))
                .findFirst();


        if (basePriceOpt.isEmpty()) return -1;

        double priceOceanFreight = basePriceOpt.get().getPrice();

        if (request.isHybrid()){
            priceOceanFreight += 300;
        }

        if (request.isElectric()){
            priceOceanFreight += 300;
        }

        return priceOceanFreight;
    }

    public double calculateTerminalHandling (TransportCalculationRequest request){
        Optional<TerminalHandlingEntity> basePriceOpt = terminalHandlingRepository
                .findAll()
                .stream()
                .filter(p -> p.getOriginPort().equalsIgnoreCase(request.getOriginPort())
                        && p.getDestinationPort().equalsIgnoreCase(request.getDestinationPort())
                        && p.getVehicleType().equalsIgnoreCase(request.getVehicleType()))
                .findFirst();

        if (basePriceOpt.isEmpty()) return -1;

        double priceTerminalHandling = basePriceOpt.get().getPrice();

        return priceTerminalHandling;

    }


    public double calculateDomesticRotterdam (TransportCalculationRequest request){
        Optional<DomesticRotterdamEntity> basePriceOpt = domesticRotterdamRepository
                .findAll()
                .stream()
                .filter(p -> p.getOriginPort().equalsIgnoreCase(request.getOriginPort())
                        && p.getDestinationPort().equalsIgnoreCase(request.getDestinationPort())
                        && p.getVehicleType().equalsIgnoreCase(request.getVehicleType()))
                .findFirst();


        if (basePriceOpt.isEmpty()) return -1;

        double priceDomesticRotterdam = basePriceOpt.get().getPrice();

        return priceDomesticRotterdam;
    }
}

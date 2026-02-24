package com.mytransport.repository;

import com.mytransport.models.VehiclePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehiclePhotoRepository extends JpaRepository<VehiclePhoto, Long> {
    List<VehiclePhoto> findByVehicleId(Long vehicleId);
    Optional<VehiclePhoto> findByIdAndVehicleId(Long id, Long vehicleId);
}

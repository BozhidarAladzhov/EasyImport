package com.mytransport.repository;

import com.mytransport.models.VehicleDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleDocumentRepository extends JpaRepository<VehicleDocument, Long> {
    List<VehicleDocument> findByVehicleIdOrderByUploadedAtDesc(Long vehicleId);
    Optional<VehicleDocument> findByIdAndVehicleId(Long id, Long vehicleId);
}

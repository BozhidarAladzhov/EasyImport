package com.mytransport.repository;

import com.mytransport.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByPublicTokenAndPublicTokenExpiresAtAfter(String publicToken, LocalDateTime now);
    Optional<Vehicle> findByPublicToken(String publicToken);

    List<Vehicle> findAllByOrderByUpdatedAtDesc();

    List<Vehicle> findByStatusOrderByUpdatedAtDesc(com.mytransport.models.VehicleStatus status);

    List<Vehicle> findByVehicleNameContainingIgnoreCaseOrVinContainingIgnoreCaseOrderByUpdatedAtDesc(String vehicleName,
                                                                                                      String vin);

    List<Vehicle> findByStatusAndVehicleNameContainingIgnoreCaseOrStatusAndVinContainingIgnoreCaseOrderByUpdatedAtDesc(
            com.mytransport.models.VehicleStatus status1, String vehicleName,
            com.mytransport.models.VehicleStatus status2, String vin);

    @Query("""
            select v from Vehicle v
            where (:status is null or v.status = :status)
              and (:q is null or lower(v.vehicleName) like lower(concat('%', :q, '%'))
                   or lower(v.vin) like lower(concat('%', :q, '%'))
                   or lower(v.containerNumber) like lower(concat('%', :q, '%'))
                   or lower(v.pol) like lower(concat('%', :q, '%'))
                   or lower(v.pod) like lower(concat('%', :q, '%')))
            order by v.updatedAt desc
            """)
    List<Vehicle> search(@Param("q") String q, @Param("status") com.mytransport.models.VehicleStatus status);
}

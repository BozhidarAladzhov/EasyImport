package com.mytransport.repository;

import com.mytransport.models.TransportPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportPriceRepository extends JpaRepository<TransportPriceEntity, Long> {
}

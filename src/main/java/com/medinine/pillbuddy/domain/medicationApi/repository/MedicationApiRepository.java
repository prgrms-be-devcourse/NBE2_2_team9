package com.medinine.pillbuddy.domain.medicationApi.repository;

import com.medinine.pillbuddy.domain.medicationApi.entity.Medication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MedicationApiRepository extends JpaRepository<Medication, String> {
    @Query("select m from Medication m where m.itemName like %:itemName%")
    Page<Medication> findAllByItemNameLike(String itemName, Pageable pageable);


}

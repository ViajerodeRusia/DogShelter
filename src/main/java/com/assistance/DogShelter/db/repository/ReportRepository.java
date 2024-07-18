package com.assistance.DogShelter.db.repository;

import com.assistance.DogShelter.db.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с отчетами.
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}
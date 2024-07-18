package com.assistance.DogShelter.service;

import com.assistance.DogShelter.db.model.Report;
import com.assistance.DogShelter.db.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с отчетами.
 */
@Service
public class ReportService {

    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * Добавляет новый отчет.
     *
     * @param report Отчет для добавления.
     * @return Добавленный отчет.
     */
    public Report addReport(Report report) {
        return reportRepository.save(report);
    }

    /**
     * Находит отчет по идентификатору.
     *
     * @param id Идентификатор отчета.
     * @return Отчет с указанным идентификатором, если найден.
     */
    public Report findReportById(Long id) {
        Optional<Report> reportOptional = reportRepository.findById(id);
        return reportOptional.orElse(null);
    }

    /**
     * Обновляет данные отчета.
     *
     * @param report Отчет с обновленными данными.
     * @return Обновленный отчет.
     */
    public Report editReport(Report report) {
        return reportRepository.save(report);
    }

    /**
     * Удаляет отчет по идентификатору.
     *
     * @param id Идентификатор отчета для удаления.
     */
    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }

    /**
     * Возвращает список всех отчетов.
     *
     * @return Список всех отчетов.
     */
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }
}

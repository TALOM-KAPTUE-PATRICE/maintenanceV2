package com.kaptue.dev.maintenance.controller;

import com.kaptue.dev.maintenance.controller.dto.kpi.ChartDataDTO;
import com.kaptue.dev.maintenance.controller.dto.kpi.ProjectStatusKpiDTO;
import com.kaptue.dev.maintenance.security.Permission;
import com.kaptue.dev.maintenance.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")

@PreAuthorize("hasAuthority('" + Permission.DASHBOARD_VIEW + "')") 
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/project-status")
    public ResponseEntity<List<ProjectStatusKpiDTO>> getProjectStatus() {
        return ResponseEntity.ok(dashboardService.getProjectStatus());
    }

    @GetMapping("/devis-by-client")
    public ResponseEntity<List<ChartDataDTO<String>>> getDevisByClient() {
        return ResponseEntity.ok(dashboardService.getSubmittedDevisByClient());
    }

    @GetMapping("/devis-status")
    public ResponseEntity<List<? extends ChartDataDTO<?>>> getDevisStatus() {
        return ResponseEntity.ok(dashboardService.getDevisStatusCounts());
    }



    @GetMapping("/projects-po-status")
    public ResponseEntity<List<ChartDataDTO<String>>> getProjectsWithOrWithoutClientPO() {
        return ResponseEntity.ok(dashboardService.getProjectsWithOrWithoutClientPO());
    }

    // ▼▼▼ CORRECTION ICI ▼▼▼ : Le type de retour utilise '?' pour être plus générique.
    @GetMapping("/demande-achat-status")
    public ResponseEntity<List<? extends ChartDataDTO<?>>> getDemandeAchatStatus() {
        return ResponseEntity.ok(dashboardService.getDemandeAchatStatusCounts());
    }
    
}
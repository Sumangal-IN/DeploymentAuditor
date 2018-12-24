package com.kingfisher.deployment.audit.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kingfisher.deployment.audit.data.repository.DeploymentRepository;
import com.kingfisher.deployment.audit.report.builder.ExcelReportBuilder;

@Service
public class DeploymentAuditService {

	@Autowired
	DeploymentRepository deploymentRepository;

	@Autowired
	ExcelReportBuilder excelReportBuilder;

	public byte[] createReport(String referenceEnv, String reportingEnv) throws IOException {
		return excelReportBuilder.createEmptyExcel("test");
	}

}

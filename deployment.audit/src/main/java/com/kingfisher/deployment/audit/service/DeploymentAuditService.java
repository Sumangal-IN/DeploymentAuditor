package com.kingfisher.deployment.audit.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kingfisher.deployment.audit.data.model.Deployment;
import com.kingfisher.deployment.audit.data.repository.DeploymentRepository;
import com.kingfisher.deployment.audit.report.builder.ExcelReportBuilder;

@Service
public class DeploymentAuditService {

	@Autowired
	DeploymentRepository deploymentRepository;

	@Autowired
	ExcelReportBuilder excelReportBuilder;

	public byte[] createReport(String referenceEnv, List<String> reportingEnv) throws IOException {
		Map<String, String[]> reportDataReferenceEnv = prepareDataForReport(deploymentRepository.getLatestStatByEnvironment(referenceEnv));
		List<Map<String, String[]>> reportDataReportingEnv = new ArrayList<>();
		for (String env : reportingEnv)
			reportDataReportingEnv.add(prepareDataForReport(deploymentRepository.getLatestStatByEnvironment(env)));
		return excelReportBuilder.createReportWithEnvData(referenceEnv,reportDataReferenceEnv, reportingEnv,reportDataReportingEnv, "report_sheet");
	}

	private Map<String, String[]> prepareDataForReport(List<Deployment> referenceEnvStatus) {
		Map<String, String[]> reportDataByApplicationName = new HashMap<>();
		for (Deployment envStatus : referenceEnvStatus) {
			String[] param = new String[4];
			param[0] = envStatus.getEnvironment();
			param[1] = envStatus.getInstanceName();
			param[2] = envStatus.getIntegrationServer();
			param[3] = envStatus.getBarReleaseId();
			reportDataByApplicationName.put(envStatus.getApplicationName(), param);
		}
		return reportDataByApplicationName;
	}

}

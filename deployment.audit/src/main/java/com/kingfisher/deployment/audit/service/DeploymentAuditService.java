package com.kingfisher.deployment.audit.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

	public byte[] createReport(String referenceEnv, List<String> reportingEnvs) throws IOException {
		List<String> applications = deploymentRepository.findApplicationNameByEnvironment(referenceEnv);

		Map<String, Map<String, List<Deployment>>> reportData = new TreeMap<>();
		for (String application : applications)
			reportData.put(application, prepareDataForApplication(application, referenceEnv, reportingEnvs));

		return excelReportBuilder.createReport(referenceEnv, reportingEnvs, reportData, "report");
	}

	private Map<String, List<Deployment>> prepareDataForApplication(String application, String referenceEnv,
			List<String> reportingEnvs) {
		Map<String, List<Deployment>> latestDeploymentsInEnvironmentForApplication = new HashMap<>();
		latestDeploymentsInEnvironmentForApplication.put(referenceEnv,
				deploymentRepository.findLatestDeploymentByApplicationNameAndEnvironment(referenceEnv, application));
		for (String reportingEnv : reportingEnvs)
			latestDeploymentsInEnvironmentForApplication.put(reportingEnv, deploymentRepository
					.findLatestDeploymentByApplicationNameAndEnvironment(reportingEnv, application));
		return latestDeploymentsInEnvironmentForApplication;
	}

	public List<Deployment> recordDeployments(List<Deployment> deployments) {
		List<Deployment> deploymentSuccess = new ArrayList<>();
		for (Deployment deployment : deployments) {
			deploymentRepository.save(deployment);
			deploymentSuccess.add(deployment);
		}
		return deployments;
	}

	public String getReportFileName() {
		return "report_" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + ".xlsx";
	}

}

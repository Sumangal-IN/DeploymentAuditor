package com.kingfisher.deployment.audit.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import com.kingfisher.deployment.audit.rest.client.IIBRestClient;

@Service
public class DeploymentAuditService {

	@Autowired
	DeploymentRepository deploymentRepository;

	@Autowired
	ExcelReportBuilder excelReportBuilder;

	@Autowired
	IIBRestClient iibRestClient;

	/**
	 * Generates report by comparing latest deployments statuses of
	 * {@code referenceEnv} and {@code reportingEnvs}
	 * 
	 * @param referenceEnv
	 *            environment with which {@code reportingEnv} will be compared
	 * @param reportingEnvs
	 *            a list of environment to be compared with {@code referenceEnv}
	 * @return
	 * @throws IOException
	 */
	public byte[] createReport(String referenceEnv, List<String> reportingEnvs) throws IOException {
		List<String> applications = deploymentRepository.findApplicationNameByEnvironment(referenceEnv);

		Map<String, Map<String, List<Deployment>>> reportData = new TreeMap<>();
		for (String application : applications)
			reportData.put(application, prepareDataForApplication(application, referenceEnv, reportingEnvs));

		return excelReportBuilder.createReport(referenceEnv, reportingEnvs, reportData, "report");
	}

	/**
	 * 
	 * @param application
	 * @param referenceEnv
	 * @param reportingEnvs
	 * @return
	 */
	private Map<String, List<Deployment>> prepareDataForApplication(String application, String referenceEnv, List<String> reportingEnvs) {
		Map<String, List<Deployment>> latestDeploymentsInEnvironmentForApplication = new HashMap<>();
		latestDeploymentsInEnvironmentForApplication.put(referenceEnv, deploymentRepository.findLatestDeploymentByApplicationNameAndEnvironment(referenceEnv, application));
		for (String reportingEnv : reportingEnvs)
			latestDeploymentsInEnvironmentForApplication.put(reportingEnv, deploymentRepository.findLatestDeploymentByApplicationNameAndEnvironment(reportingEnv, application));
		return latestDeploymentsInEnvironmentForApplication;
	}

	/**
	 * Store the deployment status in database
	 * 
	 * @param deployments
	 *            list of deployment statuses
	 * @return
	 */
	public void recordDeployments(Deployment deployment) {
		deploymentRepository.save(deployment);

	}

	/**
	 * Generates file name in the pattern yyyy_MM_dd_HH_mm_ss with .xlsx extenstion
	 * 
	 * @return the xlsx file name
	 */
	public String getReportFileName() {
		return "report_" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + ".xlsx";
	}

}
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

		Map<String, Map<String, List<String[][]>>> reportData = new TreeMap<>();
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
	private Map<String, List<String[][]>> prepareDataForApplication(String application, String referenceEnv, List<String> reportingEnvs) {
		Map<String, List<String[][]>> latestDeploymentsInEnvironmentForApplication = new HashMap<>();
		latestDeploymentsInEnvironmentForApplication.put(referenceEnv, getReportProperties(deploymentRepository.findLatestDeploymentByApplicationNameAndEnvironment(referenceEnv, application)));
		for (String reportingEnv : reportingEnvs)
			latestDeploymentsInEnvironmentForApplication.put(reportingEnv, getReportProperties(deploymentRepository.findLatestDeploymentByApplicationNameAndEnvironment(reportingEnv, application)));
		return latestDeploymentsInEnvironmentForApplication;
	}

	/**
	 * Extracts the field required for report from deployment records
	 * 
	 * @param deployments
	 *            list of deployment statuses
	 * @return {@code String[][]} for each deployment statuses containing the fields
	 *         values for the report
	 */
	private List<String[][]> getReportProperties(List<Deployment> deployments) {
		List<String[][]> properties = new ArrayList<>();
		for (Deployment deployment : deployments) {
			String[][] prop = new String[3][2];
			prop[0][0] = deployment.getInstanceName(); // Env
			prop[0][1] = null;
			prop[1][0] = deployment.getIntegrationServer(); // EG
			prop[1][1] = null;
			prop[2][0] = deployment.getBarReleaseId(); // Version
			prop[2][1] = null;
			properties.add(prop);
		}
		return properties;
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
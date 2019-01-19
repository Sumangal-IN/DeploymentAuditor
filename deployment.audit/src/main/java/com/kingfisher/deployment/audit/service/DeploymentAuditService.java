package com.kingfisher.deployment.audit.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kingfisher.deployment.audit.data.model.Deployment;
import com.kingfisher.deployment.audit.data.model.Instance;
import com.kingfisher.deployment.audit.data.repository.DeploymentRepository;
import com.kingfisher.deployment.audit.data.repository.InstanceRepository;
import com.kingfisher.deployment.audit.report.builder.ExcelReportBuilder;
import com.kingfisher.deployment.audit.rest.client.IIBRestClient;
import com.kingfisher.deployment.audit.rest.response.model.IIBDeploymentStatus;

@Service
public class DeploymentAuditService {

	@Autowired
	DeploymentRepository deploymentRepository;

	@Autowired
	InstanceRepository instanceRepository;

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
	 * @throws URISyntaxException
	 */
	public byte[] createReport(String referenceEnv, List<String> reportingEnvs) throws IOException, URISyntaxException {
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
	 * @throws URISyntaxException
	 */
	private Map<String, List<Deployment>> prepareDataForApplication(String application, String referenceEnv, List<String> reportingEnvs) throws URISyntaxException {
		Map<String, List<Deployment>> latestDeploymentsInEnvironmentForApplication = new HashMap<>();
		latestDeploymentsInEnvironmentForApplication.put(referenceEnv, updateDeploymentsWithIIBapi(deploymentRepository.findLatestDeploymentByApplicationNameAndEnvironment(referenceEnv, application)));
		for (String reportingEnv : reportingEnvs)
			latestDeploymentsInEnvironmentForApplication.put(reportingEnv, updateDeploymentsWithIIBapi(deploymentRepository.findLatestDeploymentByApplicationNameAndEnvironment(reportingEnv, application)));
		return latestDeploymentsInEnvironmentForApplication;
	}

	private List<Deployment> updateDeploymentsWithIIBapi(List<Deployment> deployments) throws URISyntaxException {
		for (Deployment deployment : deployments) {
			Instance instance = instanceRepository.findByInstanceName(deployment.getInstanceName());
			IIBDeploymentStatus iibDeploymentStatus = iibRestClient.getDeployemntProperties(deployment.getIntegrationServer(), deployment.getApplicationName(), instance.getApiHost(), instance.getApiPort());
		}
		return deployments;
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
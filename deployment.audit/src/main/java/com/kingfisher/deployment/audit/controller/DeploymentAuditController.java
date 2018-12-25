package com.kingfisher.deployment.audit.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kingfisher.deployment.audit.data.model.Deployment;
import com.kingfisher.deployment.audit.service.DeploymentAuditService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value = "DeploymentAudit", description = "API to record and report on Deployments and compare application status across tiers")
@RequestMapping("/deployment/audit")
public class DeploymentAuditController {

	@Autowired
	DeploymentAuditService deploymentAuditService;

	/**
	 * Add a set of deployments audits
	 * 
	 * @param deployments
	 *            Deployments that need adding to the audit
	 */
	@ApiOperation("Add a set of deployments audits")
	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public void audit(@ApiParam(value = "Deployments that need adding to the audit", required = true) @RequestBody List<Deployment> deployments) {

	}

	/**
	 * Generates an Excel Report with application status across environments
	 * 
	 * @param referenceEnv
	 *            Generate an Excel Report with application status across
	 *            environments
	 * @param reportingEnv
	 *            List of environments to extract info for and compared against
	 *            reference environment
	 * @return report in excel format
	 * @throws IOException
	 */
	@ApiOperation("Generates an Excel Report with application status across environments")
	@RequestMapping(value = "/report", method = RequestMethod.GET, produces = "application/vnd.ms-excel")
	public ResponseEntity<byte[]> auditReport(@ApiParam(value = " Generate an Excel Report with application status across environments", required = true) @RequestParam("referenceEnv") String referenceEnv, @ApiParam(value = "List of environments to extract info for and compared against reference environment", required = true) @RequestParam("reportingEnv") List<String> reportingEnv) throws IOException {
		byte[] data = deploymentAuditService.createReport(referenceEnv, reportingEnv);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "report.xlsx").contentLength(data.length) //
				.body(data);
	}

}

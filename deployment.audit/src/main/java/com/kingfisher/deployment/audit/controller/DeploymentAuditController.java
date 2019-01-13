package com.kingfisher.deployment.audit.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kingfisher.deployment.audit.data.model.Deployment;
import com.kingfisher.deployment.audit.data.model.ReportParam;
import com.kingfisher.deployment.audit.exception.model.AuditError;
import com.kingfisher.deployment.audit.rest.client.IIBRestClient;
import com.kingfisher.deployment.audit.service.DeploymentAuditService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "DeploymentAudit")
@RequestMapping("/v1/deployments")
public class DeploymentAuditController {

	@Autowired
	DeploymentAuditService deploymentAuditService;

	@Autowired
	IIBRestClient iIBRestClient;

	/**
	 * Add a set of deployments audits
	 * 
	 * @param deployments
	 *            Deployments that need adding to the audit
	 */
	@ApiOperation("Add a set of deployments audits")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Success"), @ApiResponse(code = 400, message = "Bad Request") })
	@PostMapping(value = "/", consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> audit(@ApiParam(value = "Deployments that need adding to the audit", required = true) @RequestBody Deployment deployment) {
		deploymentAuditService.recordDeployments(deployment);
		return new ResponseEntity<String>(HttpStatus.CREATED);
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
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Success", response = Byte[].class), @ApiResponse(code = 400, message = "Invalid request parameterts", response = AuditError.class) })
	@PostMapping(value = "/report", produces = "application/vnd.ms-excel")
	public ResponseEntity<byte[]> report(@ApiParam(value = "List of environments to extract info for and compared against reference environment", required = true) @RequestBody ReportParam param) throws IOException {
		byte[] data = deploymentAuditService.createReport(param.getReferenceEnv(), param.getReportingEnv());
		return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + deploymentAuditService.getReportFileName()).contentLength(data.length) //
				.body(data);
	}
	
	@ApiOperation("Generates an Excel Report with application status across environments")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Success", response = Byte[].class), @ApiResponse(code = 400, message = "Invalid request parameterts", response = AuditError.class) })
	@GetMapping(value = "/report", produces = "application/vnd.ms-excel")
	public ResponseEntity<byte[]> getreport(@ApiParam(value = "List of environments to extract info for and compared against reference environment", required = true) @RequestParam("r1") String r1,@RequestParam("r2") List<String> r2) throws IOException {
		byte[] data = deploymentAuditService.createReport(r1, r2);
		return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + deploymentAuditService.getReportFileName()).contentLength(data.length) //
				.body(data);
	}

}

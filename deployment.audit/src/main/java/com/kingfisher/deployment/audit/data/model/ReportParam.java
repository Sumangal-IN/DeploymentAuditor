package com.kingfisher.deployment.audit.data.model;

import java.util.List;

public class ReportParam {
	private String referenceEnv;
	private List<String> reportingEnv;

	public ReportParam() {
	}

	public ReportParam(String referenceEnv, List<String> reportingEnv) {
		super();
		this.referenceEnv = referenceEnv;
		this.reportingEnv = reportingEnv;
	}

	public String getReferenceEnv() {
		return referenceEnv;
	}

	public void setReferenceEnv(String referenceEnv) {
		this.referenceEnv = referenceEnv;
	}

	public List<String> getReportingEnv() {
		return reportingEnv;
	}

	public void setReportingEnv(List<String> reportingEnv) {
		this.reportingEnv = reportingEnv;
	}

}

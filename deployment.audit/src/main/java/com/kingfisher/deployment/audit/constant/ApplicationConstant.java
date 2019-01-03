package com.kingfisher.deployment.audit.constant;

public class ApplicationConstant {
	/**
	 * Private constructor to prevent object creation of this class. Members of this
	 * class should be acessed in static-way.
	 */
	private ApplicationConstant() {
	}

	/**
	 * Constants
	 */
	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_USER = "USER";
	public static final String ROLE_JENKINS = "JENKINS";

	public static final String FONT_CALIBRI = "Calibri";
	public static final String FONT_WINGDINGS = "Wingdings";

	public static final String REPORT_STRING_APPLICATION_NAME = "Application Name";
	public static final String REPORT_STRING_STATUS = "Status";
	public static final String REPORT_STRING_INSTANCE = "Instance";
	public static final String REPORT_STRING_EG = "EG";
	public static final String REPORT_STRING_VERSION = "Version";

	public static final String IIB_API_PLACEHOLDER_EG = "{EG}";
	public static final String IIB_API_PLACEHOLDER_APP = "{APP}";
	public static final String IIB_API_URL_EG_APP = "https://unxs0527.ghanp.kfplc.com:8090/apiv1/executiongroups/{EG}/applications/{APP}/properties";

}

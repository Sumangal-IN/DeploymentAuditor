package com.kingfisher.deployment.audit.report.builder;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ExcelReportDataOrganizer {

	public void organize(String referenceEnv, Map<String, List<String[][]>> latestDeploymentsAllInstance) {
		/*
		 *  If reference is ambigous then
		 *  	mark all reference cell as red
		 *  
		 *  group the cell
		 */

	}
	
	public boolean isReferenceAmbiguous()
	{
		return true;
	}
	
	/*
	 *  
	 */

}

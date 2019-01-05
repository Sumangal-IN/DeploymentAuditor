package com.kingfisher.deployment.audit.report.builder;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ExcelReportDataOrganizerTest {

	@Test
	public void test() {
		ExcelReportDataOrganizer excelReportDataOrganizer=new ExcelReportDataOrganizer();
		String referenceEnv="P";
		Map<String, List<String[][]>> latestDeploymentsAllInstance=new HashMap<>();
		List<String[][]> value=new ArrayList<>();
		
		String[][] v={{"PIIBINST1",null},{"MDAT",null},{"F2522.12.12",null}};
		value.add(v);
		String[][] v2={{"PIIBINST2",null},{"MDAT",null},{"F2522.12.12",null}};
		value.add(v2);
		String[][] v3={{"PIIBINST3",null},{"MDAT",null},{"F2522.12.12",null}};
		value.add(v3);
		String[][] v4={{"PIIBINST4",null},{"MDAT",null},{"F2522.12.12",null}};
		value.add(v4);
		latestDeploymentsAllInstance.put("P", value);
		
		excelReportDataOrganizer.organize(referenceEnv, latestDeploymentsAllInstance);
		
		fail("Not yet implemented");
	}

}

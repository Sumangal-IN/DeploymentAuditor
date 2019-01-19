package com.kingfisher.deployment.audit.report.builder;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.kingfisher.deployment.audit.data.model.Deployment;
import com.kingfisher.deployment.audit.data.model.Instance;
import com.kingfisher.deployment.audit.data.repository.InstanceRepository;
import com.kingfisher.deployment.audit.report.model.ReportCell;

@RunWith(SpringRunner.class)
/**
 * Test cases for {@code ExcelReportDataOrganizer} class
 * 
 * @author mandas04
 *
 */
public class ExcelReportDataOrganizerTest {

	@Before
	public void setUp() throws Exception {
		List<Instance> InstanceOfPTier = new ArrayList<>();
		List<Instance> InstanceOfVTier = new ArrayList<>();
		List<Instance> InstanceOfQTier = new ArrayList<>();
		List<Instance> InstanceOfETier = new ArrayList<>();
		List<Instance> InstanceOfFTier = new ArrayList<>();
		InstanceOfPTier.add(new Instance("P", "PIIBINST1", "unxs0616.gha.kfplc.com", "unxs0527.ghanp.kfplc.com", 8090));
		InstanceOfPTier.add(new Instance("P", "PIIBINST2", "unxs0617.gha.kfplc.com", "unxs0527.ghanp.kfplc.com", 8090));
		InstanceOfPTier.add(new Instance("P", "PIIBINST3", "unxs0622.gha.kfplc.com", "unxs0527.ghanp.kfplc.com", 8090));
		InstanceOfPTier.add(new Instance("P", "PIIBINST4", "unxs0623.gha.kfplc.com", "unxs0527.ghanp.kfplc.com", 8090));
		InstanceOfVTier.add(new Instance("V", "VIIBINST1", "unxs0624.gha.kfplc.com", "unxs0527.ghanp.kfplc.com", 8090));
		InstanceOfVTier.add(new Instance("V", "VIIBINST2", "unxs0625.gha.kfplc.com", "unxs0527.ghanp.kfplc.com", 8090));
		InstanceOfVTier.add(new Instance("V", "VIIBINST3", "unxs0630.gha.kfplc.com", "unxs0527.ghanp.kfplc.com", 8090));
		InstanceOfVTier.add(new Instance("V", "VIIBINST4", "unxs0631.gha.kfplc.com", "unxs0527.ghanp.kfplc.com", 8090));
		InstanceOfQTier.add(new Instance("Q", "QIIBINST1", "unxs0634.ghanp.kfplc.com", "unxs0527.ghanp.kfplc.com", 8090));
		InstanceOfQTier.add(new Instance("Q", "QIIBINST2", "unxs0707.ghanp.kfplc.com", "unxs0527.ghanp.kfplc.com", 8090));
		InstanceOfETier.add(new Instance("E", "EIIBINST1", "unxs0612.ghanp.kfplc.com", "unxs0527.ghanp.kfplc.com", 8090));
		InstanceOfFTier.add(new Instance("F", "FIIBINST1", "unxs0614.ghanp.kfplc.com", "unxs0527.ghanp.kfplc.com", 8090));
		Mockito.when(instanceRepository.findByTier("P")).thenReturn(InstanceOfPTier);
		Mockito.when(instanceRepository.findByTier("V")).thenReturn(InstanceOfVTier);
		Mockito.when(instanceRepository.findByTier("Q")).thenReturn(InstanceOfQTier);
		Mockito.when(instanceRepository.findByTier("E")).thenReturn(InstanceOfETier);
		Mockito.when(instanceRepository.findByTier("F")).thenReturn(InstanceOfFTier);
	}

	/**
	 * Test configuration defines implementation of CarrierDataService
	 *
	 */
	@TestConfiguration
	static class ExcelReportDataOrganizerTestContextConfiguration {
		/**
		 * Implementing of ExcelReportDataOrganizer
		 * 
		 * @return implementation ExcelReportDataOrganizer
		 */
		@Bean
		public ExcelReportDataOrganizer carrierDataService() {
			return new ExcelReportDataOrganizer();
		}
	}

	/**
	 * Mock layer
	 */
	@MockBean
	private InstanceRepository instanceRepository;

	/**
	 * Class under testing
	 */
	@Autowired
	private ExcelReportDataOrganizer excelReportDataOrganizer;

	@Test
	public void testOrganizeWithReference() {
		String referenceEnv = "P";
		Map<String, List<Deployment>> latestDeploymentsAllInstance = new HashMap<>();
		String applicationName = "TestApp";
		String deployedByUserName = "mandas04";
		String deployedByUserId = "mandas04";
		String createdByProcess = "Jenkins";

		List<Deployment> deploymentsInP = new ArrayList<>();
		deploymentsInP.add(new Deployment(applicationName, "", "P", "PIIBINST1", "EG1", null, null, null, "10.0", "", deployedByUserName, deployedByUserId, createdByProcess));
		deploymentsInP.add(new Deployment(applicationName, "", "P", "PIIBINST2", "EG1", null, null, null, "10.0", "", deployedByUserName, deployedByUserId, createdByProcess));
		deploymentsInP.add(new Deployment(applicationName, "", "P", "PIIBINST3", "EG1", null, null, null, "10.0", "", deployedByUserName, deployedByUserId, createdByProcess));
		deploymentsInP.add(new Deployment(applicationName, "", "P", "PIIBINST4", "EG1", null, null, null, "10.0", "", deployedByUserName, deployedByUserId, createdByProcess));
		latestDeploymentsAllInstance.put("P", deploymentsInP);

		List<ReportCell[]> actualReportTable = excelReportDataOrganizer.organize(referenceEnv, latestDeploymentsAllInstance);
		List<ReportCell[]> expectedReportTable = new ArrayList<>();
		expectedReportTable.add(new ReportCell[] { new ReportCell("*", false), new ReportCell("EG1", false), new ReportCell("10.0", false) });
		assertArrayEquals(expectedReportTable.toArray(), actualReportTable.toArray());
	}

	@Test
	public void testOrganizeWithAmbiguousReference() {
		String referenceEnv = "P";
		Map<String, List<Deployment>> latestDeploymentsAllInstance = new HashMap<>();
		String applicationName = "TestApp";
		String deployedByUserName = "mandas04";
		String deployedByUserId = "mandas04";
		String createdByProcess = "Jenkins";

		List<Deployment> deploymentsInP = new ArrayList<>();
		deploymentsInP.add(new Deployment(applicationName, "", "P", "PIIBINST1", "EG1", null, null, null, "10.0", "", deployedByUserName, deployedByUserId, createdByProcess));
		deploymentsInP.add(new Deployment(applicationName, "", "P", "PIIBINST2", "EG1", null, null, null, "10.0", "", deployedByUserName, deployedByUserId, createdByProcess));
		deploymentsInP.add(new Deployment(applicationName, "", "P", "PIIBINST3", "EG1", null, null, null, "10.0", "", deployedByUserName, deployedByUserId, createdByProcess));
		latestDeploymentsAllInstance.put("P", deploymentsInP);

		List<ReportCell[]> actualReportTable = excelReportDataOrganizer.organize(referenceEnv, latestDeploymentsAllInstance);
		List<ReportCell[]> expectedReportTable = new ArrayList<>();
		expectedReportTable.add(new ReportCell[] { new ReportCell("PIIBINST(1,2,3)", true), new ReportCell("EG1", true), new ReportCell("10.0", true) });
		assertArrayEquals(expectedReportTable.toArray(), actualReportTable.toArray());
	}

	@Test
	public void testOrganizeWithAmbiguousReferenceAndReporting() {
		String referenceEnv = "P";
		Map<String, List<Deployment>> latestDeploymentsAllInstance = new HashMap<>();
		String applicationName = "TestApp";
		String deployedByUserName = "mandas04";
		String deployedByUserId = "mandas04";
		String createdByProcess = "Jenkins";

		List<Deployment> deploymentsInP = new ArrayList<>();
		deploymentsInP.add(new Deployment(applicationName, "", "P", "PIIBINST1", "EG1", null, null, null, "10.0", "", deployedByUserName, deployedByUserId, createdByProcess));
		deploymentsInP.add(new Deployment(applicationName, "", "P", "PIIBINST2", "EG1", null, null, null, "10.0", "", deployedByUserName, deployedByUserId, createdByProcess));
		deploymentsInP.add(new Deployment(applicationName, "", "P", "PIIBINST3", "EG1", null, null, null, "10.0", "", deployedByUserName, deployedByUserId, createdByProcess));
		latestDeploymentsAllInstance.put("P", deploymentsInP);

		List<Deployment> deploymentsInQ = new ArrayList<>();
		deploymentsInQ.add(new Deployment(applicationName, "", "Q", "QIIBINST1", "EG1", null, null, null, "10.0", "", deployedByUserName, deployedByUserId, createdByProcess));
		latestDeploymentsAllInstance.put("Q", deploymentsInQ);

		List<ReportCell[]> actualReportTable = excelReportDataOrganizer.organize(referenceEnv, latestDeploymentsAllInstance);
		List<ReportCell[]> expectedReportTable = new ArrayList<>();
		expectedReportTable.add(new ReportCell[] { new ReportCell("PIIBINST(1,2,3)", true), new ReportCell("EG1", true), new ReportCell("10.0", true), new ReportCell("QIIBINST(1)", false), new ReportCell("EG1", false), new ReportCell("10.0", false) });
		assertArrayEquals(expectedReportTable.toArray(), actualReportTable.toArray());
	}

	@Test
	public void testOrganizeWithReferenceAndAmbiguousReporting() {
		String referenceEnv = "P";
		Map<String, List<Deployment>> latestDeploymentsAllInstance = new HashMap<>();
		String applicationName = "TestApp";
		String deployedByUserName = "mandas04";
		String deployedByUserId = "mandas04";
		String createdByProcess = "Jenkins";

		List<Deployment> deploymentsInP = new ArrayList<>();
		deploymentsInP.add(new Deployment(applicationName, "", "P", "PIIBINST1", "EG1", null, null, null, "10.0", "", deployedByUserName, deployedByUserId, createdByProcess));
		deploymentsInP.add(new Deployment(applicationName, "", "P", "PIIBINST2", "EG1", null, null, null, "10.0", "", deployedByUserName, deployedByUserId, createdByProcess));
		deploymentsInP.add(new Deployment(applicationName, "", "P", "PIIBINST3", "EG1", null, null, null, "10.0", "", deployedByUserName, deployedByUserId, createdByProcess));
		deploymentsInP.add(new Deployment(applicationName, "", "P", "PIIBINST4", "EG1", null, null, null, "10.0", "", deployedByUserName, deployedByUserId, createdByProcess));
		latestDeploymentsAllInstance.put("P", deploymentsInP);

		List<Deployment> deploymentsInQ = new ArrayList<>();
		deploymentsInQ.add(new Deployment(applicationName, "", "Q", "QIIBINST1", "EG1", null, null, null, "10.0", "", deployedByUserName, deployedByUserId, createdByProcess));
		latestDeploymentsAllInstance.put("Q", deploymentsInQ);

		List<ReportCell[]> actualReportTable = excelReportDataOrganizer.organize(referenceEnv, latestDeploymentsAllInstance);
		List<ReportCell[]> expectedReportTable = new ArrayList<>();
		expectedReportTable.add(new ReportCell[] { new ReportCell("*", false), new ReportCell("EG1", false), new ReportCell("10.0", false), new ReportCell("QIIBINST(1)", true), new ReportCell("EG1", false), new ReportCell("10.0", false) });
		assertArrayEquals(expectedReportTable.toArray(), actualReportTable.toArray());
	}
}

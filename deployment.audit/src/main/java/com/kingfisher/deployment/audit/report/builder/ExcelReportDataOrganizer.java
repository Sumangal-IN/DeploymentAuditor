package com.kingfisher.deployment.audit.report.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kingfisher.deployment.audit.data.model.Deployment;
import com.kingfisher.deployment.audit.data.model.Instance;
import com.kingfisher.deployment.audit.data.repository.InstanceRepository;
import com.kingfisher.deployment.audit.report.model.ReportCell;

@Component
public class ExcelReportDataOrganizer {

	@Autowired
	InstanceRepository instanceRepository;

	enum DataFlag {
		PLACE_AS_PER_DATA, PLACE_ALL_NA, PLACE_ALL_BLANK
	}

	enum AmbiguousFlag {
		MARK_AS_PER_DATA, MARK_ALL_AMBIGUOUS, MARK_ALL_UNAMBIGUOUS
	}

	private static final int FIELD_COUNT = 3;

	public List<ReportCell[]> organize(String referenceEnv,
			Map<String, List<Deployment>> latestDeploymentsAllInstance) {
		List<ReportCell[]> virtualRows = new ArrayList<>();
		String[] environments = latestDeploymentsAllInstance.keySet()
				.toArray(new String[latestDeploymentsAllInstance.keySet().size()]);

		Map<String, List<String[]>> latestDeploymentsAllInstanceProperties = getReportProperties(
				latestDeploymentsAllInstance);

		if (isReferenceAmbiguous(latestDeploymentsAllInstanceProperties.get(referenceEnv))) {
			markReferenceAsAmbiguous(latestDeploymentsAllInstanceProperties, environments, referenceEnv, virtualRows);
		} else {
			compareReferenceWithReportingbyEG(latestDeploymentsAllInstanceProperties, environments, referenceEnv,
					virtualRows);
			compareReportingWithReferencebyVerison(latestDeploymentsAllInstanceProperties, environments, referenceEnv,
					virtualRows);
		}
		return virtualRows;
	}

	private Map<String, List<String[]>> getReportProperties(Map<String, List<Deployment>> deployments) {
		Map<String, List<String[]>> deploymentProperties = new HashMap<>();
		for (Entry<String, List<Deployment>> rowDataPerApplication : deployments.entrySet()) {
			List<String[]> properties = new ArrayList<>();
			for (Deployment deployment : rowDataPerApplication.getValue()) {
				String[] prop = new String[FIELD_COUNT];
				prop[0] = deployment.getInstanceName(); // Env
				prop[1] = deployment.getIntegrationServer(); // EG
				prop[2] = deployment.getBarReleaseId(); // Version
				properties.add(prop);
			}
			//fillInstances(instanceRepository.findByTier(rowDataPerApplication.getKey()), properties);
			properties = mergeInstances(properties);
			deploymentProperties.put(rowDataPerApplication.getKey(), properties);
		}
		return deploymentProperties;
	}

	private List<String[]> mergeInstances(List<String[]> properties) {
		List<String[]> mergedProperties = new ArrayList<>();
		for (String[] property : properties) {
			boolean match = false;
			String[] matchedProperty = null;
			for (String[] mergedProperty : mergedProperties) {
				if (property[2].equals(mergedProperty[2]) && property[1].equals(mergedProperty[1])) {
					matchedProperty = mergedProperty;
					match = true;
					break;
				}
			}
			if (!match)
				mergedProperties.add(addInstanceNumber(null, property));
			else {
				addInstanceNumber(matchedProperty, property);
			}
		}
		if(mergedProperties.size()==1)
			mergedProperties.get(0)[0]="*";
		return mergedProperties;
	}

	private String[] addInstanceNumber(String[] oldProperty, String[] propertyToMerge) {
		if (oldProperty == null) {
			propertyToMerge[0] = getInstanceName(propertyToMerge[0]) + "(" + getInstanceNumber(propertyToMerge[0])
					+ ")";
			return propertyToMerge;
		}
		oldProperty[0] = oldProperty[0].substring(0, oldProperty[0].length() - 1) + ","
				+ getInstanceNumber(propertyToMerge[0]) + ")";
		return oldProperty;
	}

	private String getInstanceNumber(String instance) {
		return instance.substring(instance.length() - 1);
	}

	private String getInstanceName(String instance) {
		return instance.substring(0, instance.length() - 1);
	}

	private void fillInstances(List<Instance> availableInstances, List<String[]> properties) {
		for (Instance instance : availableInstances) {
			boolean match = false;
			for (String[] property : properties) {
				if (instance.getInstanceName().equals(property[0])) {
					match = true;
					break;
				}
			}
			if (!match) {
				String[] property = { instance.getInstanceName(), "NA", "NA" };
				properties.add(property);
			}
		}
	}

	private void compareReportingWithReferencebyVerison(Map<String, List<String[]>> latestDeploymentsAllInstance,
			String[] environments, String referenceEnv, List<ReportCell[]> virtualRows) {
		int maxInstances = calculateMaxRowRequired(latestDeploymentsAllInstance, environments, referenceEnv, false);
		List<String[]> referenceDeployments = latestDeploymentsAllInstance.get(referenceEnv);

		for (int ins_row_num = 0; ins_row_num < maxInstances; ins_row_num++) {
			ReportCell[] virtualRow = prepareVirtualRow(latestDeploymentsAllInstance.size() * FIELD_COUNT);
			for (int env_num = 0; env_num < environments.length; env_num++) {
				if (environments[env_num].equals(referenceEnv)
						|| latestDeploymentsAllInstance.get(environments[env_num]).isEmpty()) {
					populateRow(virtualRow, null, null, env_num, DataFlag.PLACE_ALL_BLANK,
							AmbiguousFlag.MARK_ALL_UNAMBIGUOUS);
				} else {
					String[] data = latestDeploymentsAllInstance.get(environments[env_num]).get(0);
					if (data[1].equals("NA") && data[2].equals("NA")) {
						populateRow(virtualRow, data, null, env_num, DataFlag.PLACE_AS_PER_DATA,
								AmbiguousFlag.MARK_ALL_UNAMBIGUOUS);
					} else {
						Set<Integer> markAmbiguous = new HashSet<>();
						markAmbiguous.add(1);
						if (matchOnVersion(referenceDeployments, data).length == 0) {
							markAmbiguous.add(2);
						}
						populateRow(virtualRow, data, markAmbiguous, env_num, DataFlag.PLACE_AS_PER_DATA,
								AmbiguousFlag.MARK_AS_PER_DATA);
					}
					latestDeploymentsAllInstance.get(environments[env_num]).remove(0);
				}
			}
			virtualRows.add(virtualRow);
		}
	}

	private String[] matchOnVersion(List<String[]> referenceDeployments, String[] reportingDeployment) {
		for (String[] referenceDeployment : referenceDeployments) {
			if (reportingDeployment[2].equals(referenceDeployment[2])) {
				return referenceDeployment;
			}
		}
		return new String[0];
	}

	private void compareReferenceWithReportingbyEG(Map<String, List<String[]>> latestDeploymentsAllInstance,
			String[] environments, String referenceEnv, List<ReportCell[]> virtualRows) {
		for (String[] referenceDeployment : latestDeploymentsAllInstance.get(referenceEnv)) {
			ReportCell[] virtualRow = prepareVirtualRow(latestDeploymentsAllInstance.size() * FIELD_COUNT);
			for (int env_num = 0; env_num < environments.length; env_num++) {
				if (environments[env_num].equals(referenceEnv))
					populateRow(virtualRow, referenceDeployment, null, env_num, DataFlag.PLACE_AS_PER_DATA,
							AmbiguousFlag.MARK_ALL_UNAMBIGUOUS);
				else {
					String[] matchedEGDeployment = matchOnEG(referenceDeployment,
							latestDeploymentsAllInstance.get(environments[env_num]));
					if (matchedEGDeployment.length == 0)
						populateRow(virtualRow, null, null, env_num, DataFlag.PLACE_ALL_NA,
								AmbiguousFlag.MARK_ALL_AMBIGUOUS);
					else {
						Set<Integer> markAmbiguous = new HashSet<>();
						if (!matchedEGDeployment[2].equals(referenceDeployment[2]))
							markAmbiguous.add(2);
						populateRow(virtualRow, matchedEGDeployment, markAmbiguous, env_num, DataFlag.PLACE_AS_PER_DATA,
								AmbiguousFlag.MARK_AS_PER_DATA);
					}
				}
			}
			virtualRows.add(virtualRow);
		}
	}

	private String[] matchOnEG(String[] referenceDeployment, List<String[]> reportingDeployments) {
		for (String[] reportingDeployment : reportingDeployments) {
			if (reportingDeployment[1].equals(referenceDeployment[1])) {
				String[] deployment = reportingDeployment;
				reportingDeployments.remove(reportingDeployment);
				return deployment;
			}
		}
		return new String[0];
	}

	private void markReferenceAsAmbiguous(Map<String, List<String[]>> latestDeploymentsAllInstance,
			String[] environments, String referenceEnv, List<ReportCell[]> virtualRows) {
		int maxInstances = calculateMaxRowRequired(latestDeploymentsAllInstance, environments, referenceEnv, true);
		for (int ins_row_num = 0; ins_row_num < maxInstances; ins_row_num++) {
			ReportCell[] virtualRow = prepareVirtualRow(latestDeploymentsAllInstance.size() * FIELD_COUNT);
			for (int env_num = 0; env_num < environments.length; env_num++) {
				if (latestDeploymentsAllInstance.get(environments[env_num]).isEmpty()) {
					populateRow(virtualRow, null, null, env_num, DataFlag.PLACE_ALL_BLANK,
							AmbiguousFlag.MARK_ALL_UNAMBIGUOUS);
				} else {
					String[] data = latestDeploymentsAllInstance.get(environments[env_num]).get(0);
					if (environments[env_num].equals(referenceEnv))
						populateRow(virtualRow, data, null, env_num, DataFlag.PLACE_AS_PER_DATA,
								AmbiguousFlag.MARK_ALL_AMBIGUOUS);
					else
						populateRow(virtualRow, data, null, env_num, DataFlag.PLACE_AS_PER_DATA,
								AmbiguousFlag.MARK_ALL_UNAMBIGUOUS);
					latestDeploymentsAllInstance.get(environments[env_num]).remove(0);
				}

			}
			virtualRows.add(virtualRow);
		}
	}

	private int calculateMaxRowRequired(Map<String, List<String[]>> latestDeploymentsAllInstance, String[] environments,
			String referenceEnv, boolean includeReferenceEnv) {
		int maxRowRequired = 0;

		for (int env_num = 0; env_num < environments.length; env_num++)
			if (environments[env_num].equals(referenceEnv)) {
				if (includeReferenceEnv
						&& maxRowRequired < latestDeploymentsAllInstance.get(environments[env_num]).size())
					maxRowRequired = latestDeploymentsAllInstance.get(environments[env_num]).size();
			} else {
				if (maxRowRequired < latestDeploymentsAllInstance.get(environments[env_num]).size())
					maxRowRequired = latestDeploymentsAllInstance.get(environments[env_num]).size();
			}

		return maxRowRequired;
	}

	private ReportCell[] prepareVirtualRow(int size) {
		ReportCell[] virtualRow = new ReportCell[size];
		for (int i = 0; i < virtualRow.length; i++)
			virtualRow[i] = new ReportCell();
		return virtualRow;
	}

	private void populateRow(ReportCell[] virtualRow, String[] data, Set<Integer> ambiguousFields, int offset,
			DataFlag dataFlag, AmbiguousFlag ambiguousFlag) {

		for (int i = 0; i < FIELD_COUNT; i++) {
			int pos = (offset * FIELD_COUNT) + i;

			if (dataFlag == DataFlag.PLACE_AS_PER_DATA)
				virtualRow[pos].setValue(data[i]);
			else if (dataFlag == DataFlag.PLACE_ALL_NA)
				virtualRow[pos].setValue("NA");
			else if (dataFlag == DataFlag.PLACE_ALL_BLANK)
				virtualRow[pos].setValue("");

			if (ambiguousFlag == AmbiguousFlag.MARK_AS_PER_DATA && ambiguousFields.contains(i))
				virtualRow[pos].setAmbiguous(true);
			else if (ambiguousFlag == AmbiguousFlag.MARK_ALL_AMBIGUOUS)
				virtualRow[pos].setAmbiguous(true);
			else if (ambiguousFlag == AmbiguousFlag.MARK_ALL_UNAMBIGUOUS)
				virtualRow[pos].setAmbiguous(false);
		}
	}

	private boolean isReferenceAmbiguous(List<String[]> referenceDeployments) {
		String version = null;
		for (String[] referenceDeployment : referenceDeployments) {
			if (!referenceDeployment[2].equals("NA")) {
				if (version == null)
					version = referenceDeployment[2];
				else if (version != referenceDeployment[2])
					return true;
			}
		}
		return false;
	}

}

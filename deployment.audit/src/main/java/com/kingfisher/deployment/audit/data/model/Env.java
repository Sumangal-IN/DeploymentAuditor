package com.kingfisher.deployment.audit.data.model;

import java.util.ArrayList;
import java.util.List;

public class Env {

	List<Instance> getInstances(String tier) {
		List<Instance> instances = new ArrayList<>();
		switch (tier) {
		case "P":
			instances.add(new Instance("P", "PIIBINST1", "unxs0616.gha.kfplc.com"));
			instances.add(new Instance("P", "PIIBINST2", "unxs0617.gha.kfplc.com"));
			instances.add(new Instance("P", "PIIBINST3", "unxs0622.gha.kfplc.com"));
			instances.add(new Instance("P", "PIIBINST4", "unxs0623.gha.kfplc.com"));
			break;
		case "V":
			instances.add(new Instance("V", "VIIBINST1", "unxs0624.gha.kfplc.com"));
			instances.add(new Instance("V", "VIIBINST2", "unxs0625.gha.kfplc.com"));
			instances.add(new Instance("V", "VIIBINST3", "unxs0630.gha.kfplc.com"));
			instances.add(new Instance("V", "VIIBINST4", "unxs0631.gha.kfplc.com"));
			break;
		case "Q":
			instances.add(new Instance("Q", "QIIBINST1", "unxs0634.ghanp.kfplc.com"));
			instances.add(new Instance("Q", "QIIBINST2", "unxs0707.ghanp.kfplc.com"));
			break;
		case "E":
			instances.add(new Instance("E", "EIIBINST1", "unxs0612.ghanp.kfplc.com"));
			break;
		case "F":
			instances.add(new Instance("F", "FIIBINST1", "unxs0614.ghanp.kfplc.com"));
			break;
		default:
		}
		return instances;
	}
}

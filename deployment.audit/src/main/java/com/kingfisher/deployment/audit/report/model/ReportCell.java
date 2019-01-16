package com.kingfisher.deployment.audit.report.model;

public class ReportCell {
	private String value;
	private boolean ambiguous;

	public ReportCell() {
	}

	public ReportCell(String value, boolean ambiguous) {
		super();
		this.value = value;
		this.ambiguous = ambiguous;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isAmbiguous() {
		return ambiguous;
	}

	public void setAmbiguous(boolean ambiguous) {
		this.ambiguous = ambiguous;
	}

	@Override
	public String toString() {
		return "ReportCell [value=" + value + ", ambiguous=" + ambiguous + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportCell other = (ReportCell) obj;
		if (ambiguous != other.ambiguous)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}

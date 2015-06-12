package com.anpi.app.api.domain;

public class Fax {

	private String fromDid;
	private String toDid;
	private String fileName;
	private String uuid;
	private String originalFileName;
	private String originalUuid;
	

	public String getFromDid() {
		return fromDid;
	}

	public void setFromDid(String fromDid) {
		this.fromDid = fromDid;
	}

	public String getToDid() {
		return toDid;
	}

	public void setToDid(String toDid) {
		this.toDid = toDid;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "Fax [fromDid=" + fromDid + ", toDid=" + toDid + ", fileName=" + fileName + "]";
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getOriginalUuid() {
		return originalUuid;
	}

	public void setOriginalUuid(String originalUuid) {
		this.originalUuid = originalUuid;
	}
	
	
	

}

package net.dorokhov.pony.web.shared;

import java.util.Date;
import java.util.List;

public class ScanResultDto extends AbstractEntityDto<Long> {

	private Date date;

	private boolean success;

	private List<String> targetFiles;

	private Long duration;

	private Long scannedFolderCount;

	private Long scannedFileCount;

	private Long importedFileCount;

	public Date getDate() {
		return date;
	}

	public void setDate(Date aDate) {
		date = aDate;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean aSuccess) {
		success = aSuccess;
	}

	public List<String> getTargetFiles() {
		return targetFiles;
	}

	public void setTargetFiles(List<String> aTargetFiles) {
		targetFiles = aTargetFiles;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long aDuration) {
		duration = aDuration;
	}

	public Long getScannedFolderCount() {
		return scannedFolderCount;
	}

	public void setScannedFolderCount(Long aScannedFolderCount) {
		scannedFolderCount = aScannedFolderCount;
	}

	public Long getScannedFileCount() {
		return scannedFileCount;
	}

	public void setScannedFileCount(Long aScannedFileCount) {
		scannedFileCount = aScannedFileCount;
	}

	public Long getImportedFileCount() {
		return importedFileCount;
	}

	public void setImportedFileCount(Long aImportedFileCount) {
		importedFileCount = aImportedFileCount;
	}
}

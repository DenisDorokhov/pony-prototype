package net.dorokhov.pony.core.domain;

import net.dorokhov.pony.core.dao.entity.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "scan_result")
public class ScanResult extends BaseEntity<Long> {

	private Boolean success;

	private List<String> targetFiles;

	private Long duration;

	private Long scannedFolderCount;

	private Long scannedFileCount;

	private Long importedFileCount;

	@Column(name = "success")
	@NotNull
	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean aSuccess) {
		success = aSuccess;
	}

	@Column(name="path")
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="scan_result_path", joinColumns=@JoinColumn(name="scan_result_id"))
	public List<String> getTargetFiles() {

		if (targetFiles == null) {
			targetFiles = new ArrayList<String>();
		}

		return targetFiles;
	}

	public void setTargetFiles(List<String> aTargetFiles) {
		targetFiles = aTargetFiles;
	}

	@Column(name = "duration")
	@NotNull
	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long aDuration) {
		duration = aDuration;
	}

	@Column(name = "scanned_folder_count")
	@NotNull
	public Long getScannedFolderCount() {
		return scannedFolderCount;
	}

	public void setScannedFolderCount(Long aScannedFolderCount) {
		scannedFolderCount = aScannedFolderCount;
	}

	@Column(name = "scanned_file_count")
	@NotNull
	public Long getScannedFileCount() {
		return scannedFileCount;
	}

	public void setScannedFileCount(Long aScannedFileCount) {
		scannedFileCount = aScannedFileCount;
	}

	@Column(name = "imported_file_count")
	@NotNull
	public Long getImportedFileCount() {
		return importedFileCount;
	}

	public void setImportedFileCount(Long aImportedFileCount) {
		importedFileCount = aImportedFileCount;
	}
}

package net.dorokhov.pony.core.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "scan_result")
public class ScanResult {

	private Long id;

	private Date date;

	private Boolean success;

	private List<String> targetFiles;

	private Long duration;

	private Long scannedFolderCount;

	private Long scannedFileCount;

	private Long importedFileCount;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long aId) {
		id = aId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date")
	public Date getDate() {
		return date;
	}

	public void setDate(Date aDate) {
		date = aDate;
	}

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

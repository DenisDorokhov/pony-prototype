package net.dorokhov.pony.web.client.mvp.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;
import net.dorokhov.pony.web.client.Messages;
import net.dorokhov.pony.web.client.common.ContentState;
import net.dorokhov.pony.web.shared.ConfigurationDto;
import net.dorokhov.pony.web.shared.ConfigurationOptions;
import net.dorokhov.pony.web.shared.ScanResultDto;
import net.dorokhov.pony.web.shared.ScanStatusDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsView extends PopupViewWithUiHandlers<SettingsUiHandlers> implements SettingsPresenter.MyView {

	interface MyUiBinder extends UiBinder<PopupPanel, SettingsView> {}

	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	private static final NumberFormat PROGRESS_FORMAT = NumberFormat.getPercentFormat();

	private final Map<String, ConfigurationDto> configurationMap = new HashMap<String, ConfigurationDto>();

	@UiField
	Label scanResultLabel;

	@UiField
	Label progressLabel;

	@UiField
	Button scanButton;

	@UiField
	DeckPanel deck;

	@UiField
	Label loadingLabel;

	@UiField
	Label errorLabel;

	@UiField
	Panel formPanel;

	@UiField
	TextBox libraryFoldersText;

	@UiField
	ListBox autoScanIntervalList;

	@UiField
	Button saveButton;

	private ScanResultDto scanResult;

	private ScanStatusDto progress;

	private List<ConfigurationDto> configuration;

	private ContentState scanResultState;

	private ScannerState scannerState;

	private ConfigurationState configurationState;

	private boolean modified;

	@Inject
	public SettingsView(EventBus aEventBus) {

		super(aEventBus);

		initWidget(uiBinder.createAndBindUi(this));

		updateScannerState();
	}

	@Override
	public ScanResultDto getScanResult() {
		return scanResult;
	}

	@Override
	public void setScanResult(ScanResultDto aScanResult) {
		scanResult = aScanResult;
	}

	@Override
	public ScanStatusDto getProgress() {
		return progress;
	}

	@Override
	public void setProgress(ScanStatusDto aProgress) {
		progress = aProgress;
	}

	@Override
	public List<ConfigurationDto> getConfiguration() {
		return configuration;
	}

	@Override
	public void setConfiguration(List<ConfigurationDto> aConfiguration) {

		configuration = aConfiguration;

		configurationMap.clear();

		if (configuration != null) {
			for (ConfigurationDto item : configuration) {
				configurationMap.put(item.getId(), item);
			}
		}

		updateConfiguration();

		modified = false;
	}

	@Override
	public ContentState getScanResultState() {
		return scanResultState;
	}

	@Override
	public void setScanResultState(ContentState aScanResultState) {

		scanResultState = aScanResultState;

		updateScanResultState();
	}

	@Override
	public ScannerState getScannerState() {

		if (scannerState == null) {
			scannerState = ScannerState.INACTIVE;
		}

		return scannerState;
	}

	@Override
	public void setScannerState(ScannerState aScannerState) {

		scannerState = aScannerState;

		updateScannerState();
	}

	@Override
	public ConfigurationState getConfigurationState() {
		return configurationState;
	}

	@Override
	public void setConfigurationState(ConfigurationState aConfigurationState) {

		configurationState = aConfigurationState;

		updateConfigurationState();
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@UiHandler("libraryFoldersText")
	void onLibraryFoldersChange(ChangeEvent aEvent) {
		modified = true;
	}

	@UiHandler("autoScanIntervalList")
	void onAutoScanIntervalChange(ChangeEvent aEvent) {
		modified = true;
	}

	@UiHandler("scanButton")
	void onScanClick(ClickEvent aEvent) {
		getUiHandlers().onScanRequested();
	}

	@UiHandler("saveButton")
	void onSaveClick(ClickEvent aEvent) {
		getUiHandlers().onSaveRequested(exportConfiguration());
	}

	private void updateScanResultState() {
		if (getScanResultState() == ContentState.LOADING) {

			scanResultLabel.setText(Messages.IMPL.commonLoadingLabel());

		} else if (getScanResultState() == ContentState.LOADED) {

			if (getScanResult() != null && getScanResult().getDate() != null) {
				scanResultLabel.setText(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM).format(getScanResult().getDate()));
			} else {
				scanResultLabel.setText(Messages.IMPL.settingsNoLastScan());
			}

		} else {
			scanResultLabel.setText(Messages.IMPL.commonErrorLabel());
		}
	}

	private void updateScannerState() {

		scanButton.setEnabled(getScannerState() == null || getScannerState() == ScannerState.INACTIVE);

		if (getScannerState() == ScannerState.SCAN_STARTING) {

			progressLabel.setText(Messages.IMPL.settingsStartingScan());

		} else if (getScannerState() == ScannerState.SCANNING) {

			if (getProgress() != null) {

				String percentProgress = PROGRESS_FORMAT.format(getProgress().getProgress());

				progressLabel.setText(Messages.IMPL.settingsScanningWithProgress(percentProgress, getProgress().getStep(), getProgress().getTotalSteps()));

			} else {
				progressLabel.setText(Messages.IMPL.settingsScanningUnknownProgress());
			}

		} else {
			progressLabel.setText(Messages.IMPL.settingsScanningInactive());
			scanButton.setEnabled(true);
		}
	}

	private void updateConfigurationState() {
		if (getConfigurationState() == null) {

			deck.setVisible(false);

		} else {

			deck.setVisible(true);

			saveButton.setEnabled(getConfigurationState() != ConfigurationState.SAVING);

			switch (getConfigurationState()) {

				case LOADING:
					deck.showWidget(deck.getWidgetIndex(loadingLabel));
					break;

				case LOADED:
				case SAVING:
					deck.showWidget(deck.getWidgetIndex(formPanel));
					break;

				default:
					deck.showWidget(deck.getWidgetIndex(errorLabel));
					break;
			}
		}
	}

	private void updateConfiguration() {

		// Library folders

		libraryFoldersText.setText(getConfigValue(ConfigurationOptions.LIBRARY_FOLDERS));

		// Auto-scan interval

		String autoScanInterval = getConfigValue(ConfigurationOptions.AUTO_SCAN_INTERVAL);

		int indexToSelect = -1;

		for (int i = 0; i < autoScanIntervalList.getItemCount(); i++) {

			String value = autoScanIntervalList.getValue(i);

			if ((Integer.valueOf(value) <= 0 && autoScanInterval == null) || value.equals(autoScanInterval)) {

				indexToSelect = i;

				break;
			}
		}

		autoScanIntervalList.setSelectedIndex(indexToSelect);
	}

	private List<ConfigurationDto> exportConfiguration() {

		List<ConfigurationDto> result = new ArrayList<ConfigurationDto>();

		// Library folders

		ConfigurationDto config;

		config = new ConfigurationDto(ConfigurationOptions.LIBRARY_FOLDERS, libraryFoldersText.getText(), getConfigVersion(ConfigurationOptions.LIBRARY_FOLDERS));

		result.add(config);

		// Auto-scan interval

		Integer autoScanInterval = Integer.valueOf(autoScanIntervalList.getValue(autoScanIntervalList.getSelectedIndex()));

		config = new ConfigurationDto(ConfigurationOptions.AUTO_SCAN_INTERVAL, null, getConfigVersion(ConfigurationOptions.AUTO_SCAN_INTERVAL));

		if (autoScanInterval > 0) {
			config.setValue(autoScanInterval.toString());
		}

		result.add(config);

		return result;
	}

	private String getConfigValue(String aId) {

		ConfigurationDto config = configurationMap.get(aId);

		return config != null ? config.getValue() : null;
	}

	private Long getConfigVersion(String aId) {

		ConfigurationDto config = configurationMap.get(aId);

		return config != null ? config.getVersion() : null;
	}
}

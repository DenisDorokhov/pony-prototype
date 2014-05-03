package net.dorokhov.pony.web.client.mvp.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;
import net.dorokhov.pony.web.shared.ConfigurationDto;
import net.dorokhov.pony.web.shared.ConfigurationOptions;
import net.dorokhov.pony.web.shared.StatusDto;

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

	private StatusDto progress;

	private List<ConfigurationDto> configuration;

	private ScanState scanState;

	private ContentState contentState;

	@Inject
	public SettingsView(EventBus aEventBus) {

		super(aEventBus);

		initWidget(uiBinder.createAndBindUi(this));

		updateScanState();
	}

	@Override
	public StatusDto getProgress() {
		return progress;
	}

	@Override
	public void setProgress(StatusDto aProgress) {
		progress = aProgress;
	}

	@Override
	public ScanState getScanState() {

		if (scanState == null) {
			scanState = ScanState.INACTIVE;
		}

		return scanState;
	}

	@Override
	public void setScanState(ScanState aScanState) {

		scanState = aScanState;

		updateScanState();
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
	}

	@Override
	public ContentState getContentState() {
		return contentState;
	}

	@Override
	public void setContentState(ContentState aContentState) {

		contentState = aContentState;

		updateContentState();
	}

	@UiHandler("scanButton")
	void onScanClick(ClickEvent aEvent) {
		getUiHandlers().onScanRequested();
	}

	@UiHandler("saveButton")
	void onSaveClick(ClickEvent aEvent) {
		getUiHandlers().onSaveRequested(exportConfiguration());
	}

	private void updateScanState() {

		scanButton.setEnabled(getScanState() == null || getScanState() == ScanState.INACTIVE);

		if (getScanState() == ScanState.SCAN_STARTING) {

			progressLabel.setText("Starting...");

		} else if (getScanState() == ScanState.SCANNING) {

			if (getProgress() != null) {
				progressLabel.setText("Scanning " + PROGRESS_FORMAT.format(getProgress().getProgress()) +
						" (" + getProgress().getStep() + "/" + getProgress().getTotalSteps() + ")");
			} else {
				progressLabel.setText("Scanning...");
			}

		} else {
			progressLabel.setText("Inactive");
			scanButton.setEnabled(true);
		}
	}

	private void updateContentState() {
		if (getContentState() == null) {

			deck.setVisible(false);

		} else {

			deck.setVisible(true);

			saveButton.setEnabled(getContentState() != ContentState.SAVING);

			switch (getContentState()) {

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

		config = new ConfigurationDto(ConfigurationOptions.AUTO_SCAN_INTERVAL, null, getConfigVersion(ConfigurationOptions.LIBRARY_FOLDERS));

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

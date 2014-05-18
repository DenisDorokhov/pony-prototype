package net.dorokhov.pony.web.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface LocaleMessages extends Messages {

	public static final LocaleMessages IMPL = GWT.create(LocaleMessages.class);

	public String commonLoadingLabel();
	public String commonErrorLabel();
	public String commonNoDataLabel();
	public String commonSaveLabel();

	public String alertPlaybackWillBeStopped();
	public String alertLibraryNotDefined();
	public String alertLibraryAlreadyScanning();
	public String alertCouldNotStartScanning();
	public String alertCouldNotSaveConfiguration();

	public String confirmationScanAfterConfigurationChange();

	public String toolbarRefresh();
	public String toolbarSettings();

	public String albumDiscCaption(int aDiscNumber);

	public String settingsNoLastScan();
	public String settingsStartingScan();
	public String settingsScanningUnknownProgress();
	public String settingsScanningWithProgress(String aProgress, int aStep, int aTotalSteps);
	public String settingsScanningInactive();
	public String settingsScanProgress();
	public String settingsLastScan();
	public String settingsLibraryFiles();
	public String settingsAutoScan();
	public String settingsAutoScanEveryHour();
	public String settingsAutoScanEveryDay();
	public String settingsAutoScanEveryWeek();
	public String settingsAutoScanOff();

}

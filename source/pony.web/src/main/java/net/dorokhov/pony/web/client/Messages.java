package net.dorokhov.pony.web.client;

import com.google.gwt.core.client.GWT;

public interface Messages extends com.google.gwt.i18n.client.Messages {

	public static final Messages IMPL = GWT.create(Messages.class);

	public String titlePrefix();
	public String titleBodyNoSong();
	public String titleBodyWithSong(String aArtist, String aSong);

	public String commonLoadingLabel();
	public String commonErrorLabel();
	public String commonNoDataLabel();
	public String commonSaveLabel();

	public String alertPlaybackWillBeStopped();
	public String alertLibraryNotDefined();
	public String alertLibraryAlreadyScanning();
	public String alertCouldNotStartScanning();
	public String alertCouldNotSaveConfiguration();
	public String alertScanRequiresConfigurationSaving();

	public String confirmationScanAfterConfigurationChange();

	public String playerNoSongTitle();

	public String toolbarRefresh();
	public String toolbarSettings();

	public String albumDiscCaption(int aDiscNumber);

    public String settingsHeader();
    public String settingsLibraryScanningHeader();
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

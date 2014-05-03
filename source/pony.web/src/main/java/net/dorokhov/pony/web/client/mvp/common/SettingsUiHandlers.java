package net.dorokhov.pony.web.client.mvp.common;

import com.gwtplatform.mvp.client.UiHandlers;
import net.dorokhov.pony.web.shared.ConfigurationDto;

import java.util.List;

public interface SettingsUiHandlers extends UiHandlers {

	public void onScanRequested();

	public void onSaveRequested(List<ConfigurationDto> aConfiguration);

}

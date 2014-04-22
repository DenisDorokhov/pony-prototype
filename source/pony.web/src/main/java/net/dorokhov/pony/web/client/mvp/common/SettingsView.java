package net.dorokhov.pony.web.client.mvp.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;
import net.dorokhov.pony.web.shared.StatusDto;

public class SettingsView extends PopupViewWithUiHandlers<SettingsUiHandlers> implements SettingsPresenter.MyView {

	interface MyUiBinder extends UiBinder<PopupPanel, SettingsView> {}

	private static final MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	private static final NumberFormat PROGRESS_FORMAT = NumberFormat.getPercentFormat();

	@UiField
	Label progressLabel;

	@UiField
	Button scanButton;

	private StatusDto progress;

	private State state;

	@Inject
	public SettingsView(EventBus aEventBus) {

		super(aEventBus);

		initWidget(uiBinder.createAndBindUi(this));

		updateState();
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
	public State getState() {

		if (state == null) {
			state = State.INACTIVE;
		}

		return state;
	}

	@Override
	public void setState(State aState) {

		state = aState;

		updateState();
	}

	@UiHandler("scanButton")
	void onScanClick(ClickEvent aEvent) {
		getUiHandlers().onScanRequested();
	}

	private void updateState() {

		scanButton.setEnabled(getState() == null || getState() == State.INACTIVE);

		if (getState() == State.SCAN_STARTING) {

			progressLabel.setText("Starting...");

		} else if (getState() == State.SCANNING) {

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
}

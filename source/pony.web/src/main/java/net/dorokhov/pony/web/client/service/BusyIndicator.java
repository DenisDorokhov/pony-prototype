package net.dorokhov.pony.web.client.service;

import com.google.gwt.user.client.ui.RootLayoutPanel;

import java.util.logging.Logger;

public class BusyIndicator {

	private static final Logger log = Logger.getLogger(BusyIndicator.class.getName());

	private static int taskCount = 0;

	private static boolean busy = false;

	public static void startTask() {

		taskCount++;

		if (taskCount > 0) {
			setBusy(true);
		}
	}

	public static void finishTask() {

		if (taskCount > 0) {
			taskCount--;
		}

		if (taskCount == 0) {
			setBusy(false);
		}
	}

	public static boolean isBusy() {
		return busy;
	}

	private static void setBusy(boolean aIsBusy) {

		if (busy != aIsBusy) {

			busy = aIsBusy;

			if (busy) {

				showWaitCursor();

				log.fine("busy mode started");

			} else {

				showDefaultCursor();

				log.fine("busy mode finished");
			}
		}
	}

	private static void showWaitCursor() {
		RootLayoutPanel.get().getElement().getStyle().setProperty("cursor", "wait");
	}

	private static void showDefaultCursor() {
		RootLayoutPanel.get().getElement().getStyle().setProperty("cursor", "default");
	}

}

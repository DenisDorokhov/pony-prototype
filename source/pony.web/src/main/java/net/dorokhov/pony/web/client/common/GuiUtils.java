package net.dorokhov.pony.web.client.common;

public class GuiUtils {

	public static native void clearSelection() /*-{
		if ($wnd.getSelection) {
			$wnd.getSelection().removeAllRanges();
		} else if ($doc.selection) {
			$doc.selection.empty();
		}
	}-*/;

}

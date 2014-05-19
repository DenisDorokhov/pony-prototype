package net.dorokhov.pony.web.client.common;

public class NativeUtils {

	public static native void clearSelection() /*-{
		if ($wnd.getSelection) {
			$wnd.getSelection().removeAllRanges();
		} else if ($doc.selection) {
			$doc.selection.empty();
		}
	}-*/;

	public static native boolean isTouchDevice() /*-{
		return (('ontouchstart' in window) || (navigator.MaxTouchPoints > 0) || (navigator.msMaxTouchPoints > 0));
	}-*/;

}

package net.dorokhov.pony.web.client.common;

public class StringUtils {

	public static boolean nullSafeNormalizedEquals(String aString1, String aString2) {

		if (aString1 == null && aString2 == null) {
			return true;
		}

		if (aString1 == null || aString2 == null) {
			return false;
		}

		String normalizedValue1 = aString1.trim().toLowerCase();
		String normalizedValue2 = aString2.trim().toLowerCase();

		return normalizedValue1.equals(normalizedValue2);
	}

	public static String nullSafeToString(Object aObject) {
		return aObject != null ? aObject.toString() : null;
	}

}

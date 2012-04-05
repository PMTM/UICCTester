package eu.mighty.javatools;

import android.util.Log;

public class HexTools {

	public static final byte[] hs2ba(final String s) {
		String[] v = s.split(" ");
		byte[] arr = new byte[v.length];
		int i = 0;
		Log.e("UUUU", "hs2ba:" + s);
		for (String val : v) {
			arr[i++] = Integer.decode("0x" + val).byteValue();

		}
		return arr;
	}

	public static final String ba2hs(byte[] bytes) {
		return ba2hs(bytes, " ");
	}

	public static final String ba2hs(byte[] bytes, String spc) {
		StringBuffer sb = new StringBuffer();
		if (bytes == null) {
			sb.append("--null--");
		} else {
			for (byte b : bytes) {
				sb.append(String.format("%02x%s", b & 0xFF, spc));
			}
		}
		Log.e("UUUU", "ba2hs:" + sb.toString());
		return sb.toString();
	}

}

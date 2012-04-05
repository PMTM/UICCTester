package cz.tm.uicc;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.util.Log;

/**
 * class to process zip and jar files to extract them into original tree under
 * certain base dir
 * 
 * @author jon, petr
 */
public class Decompress {
	private String _zipFile;
	private String _location;

	/**
	 * public constructor
	 * 
	 * @param zipFile
	 *            path to the source zip/jar
	 * @param location
	 *            target base directory
	 */
	public Decompress(String zipFile, String location) {
		_zipFile = zipFile;
		_location = location;

		_dirChecker("");
	}

	/**
	 * execution of unzip
	 */
	public void unzip(Context ctx) {
		try {
			FileInputStream fin = new FileInputStream(_zipFile);
			ZipInputStream zin = new ZipInputStream(fin);
			ZipEntry ze = null;
			BufferedOutputStream dest = null;
			while ((ze = zin.getNextEntry()) != null) {
				Log.v("Decompress", "Begin unzipping " + ze.getName());

				if (ze.isDirectory()) {
					_dirChecker(ze.getName());
				} else {
					File f = new File(_location + ze.getName());
					Log.i(getClass().getSimpleName(), "File size: "
							+ ze.getSize());
					File td = new File(f.getParent());

					if (!td.isDirectory()) {
						td.mkdirs();
						Log.i(getClass().getSimpleName(), "Making directory: "
								+ td.getPath());
					}
					
					int count;
					byte data[] = new byte[UICCConstants.BUFFER];
					FileOutputStream fout = new FileOutputStream(_location
							+ ze.getName());
					dest = new BufferedOutputStream(fout, UICCConstants.BUFFER);
					while ((count = zin.read(data, 0, UICCConstants.BUFFER)) != -1) {
						dest.write(data, 0, count);
					}
					dest.flush();
					dest.close();
					/*
					 * for (int c = zin.read(); c != -1; c = zin.read()) {
					 * fout.write(c); }
					 */
					fout.close();
					zin.closeEntry();
				}
				Log.v("Decompress", "End unzipping " + ze.getName());

			}
			zin.close();
		} catch (Exception e) {
			Log.e("Decompress", "Unzip error", e);
			// Bundle b = new Bundle();
			// b.putString("label", "Download alert");
			// b.putString("cont", e.toString());
			// Intent i = new Intent(ctx, MWHome.class);
			// i.setAction(MWConstants.HOME_SHOW_ALERT);
			// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
			// | Intent.FLAG_ACTIVITY_SINGLE_TOP
			// | Intent.FLAG_ACTIVITY_NEW_TASK);
			// i.putExtras(b);
			// ctx.startActivity(i);
		}

	}

	/**
	 * verify existence of target directory in case it does not exist create it
	 * 
	 * @param dir
	 *            name of target directory
	 */
	private void _dirChecker(String dir) {
		File f = new File(_location + dir);

		if (!f.isDirectory()) {
			f.mkdirs();
		}
	}
}
package cz.tm.uicc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class BgDownCheckUnzip {

	private Context _ctx;
	private String _trgPath;
	private String _zipFilePath;

	@SuppressWarnings("unused")
	private String _downloadURL;
	@SuppressWarnings("unused")
	private String _uuid;

	public void execute(Context aCtx, final String downloadURL,
			final String trgPath, final String uuid) {

		Log.i(getClass().getSimpleName(), "execute downloadUrl='" + downloadURL
				+ "',uuid='" + uuid + "'" + "',trgPath='" + trgPath + "'");

		_ctx = aCtx;
		_trgPath = trgPath;
		_downloadURL = downloadURL;
		_uuid = uuid;
		// UUID.randomUUID()
		// UUID.fromString(downloadURL)
		// UUID.nameUUIDFromBytes(downloadURL.getBytes())
		_zipFilePath = _ctx.getFilesDir().getAbsolutePath() + "/tmp/"
				+ UUID.nameUUIDFromBytes(downloadURL.getBytes()) + ".jar";

		File tf = new File(_zipFilePath);
		File td = new File(tf.getParent());

		if (!td.isDirectory()) {
			td.mkdirs();
			Log.i(getClass().getSimpleName(),
					"Making directory: " + tf.getParent());
		}

		Log.d(getClass().getSimpleName(), "_zipFilePath = '" + _zipFilePath
				+ "'");

		new DownloadFileAsync().execute(downloadURL, _zipFilePath);

		Log.i(getClass().getSimpleName(), "Spawned async download");
	}

	class DownloadFileAsync extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Bundle b = new Bundle();
			// b.putInt("dialogID", MWConstants.DIALOG_DOWNLOAD_PROGRESS);
			//
			// Intent i = new Intent(_ctx, MWHome.class);
			// i.setAction(MWConstants.HOME_SHOW_PROGRESS);
			// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
			// Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			// i.putExtras(b);
			// _ctx.startActivity(i);
		}

		@Override
		protected String doInBackground(String... aurl) {
			int count;

			try {
				URL url = new URL(aurl[0]);
				URLConnection conexion = url.openConnection();
				conexion.connect();

				int lenghtOfFile = conexion.getContentLength();

				Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
				if (lenghtOfFile < 0)
					throw new Exception();

				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = new FileOutputStream(aurl[1]);

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					// publishProgress("" + (int) ((total * 100) /
					// lenghtOfFile));
					publishProgress("" + total, "" + lenghtOfFile);
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
				Log.e("ERROR", "download failed: ", e);

				// Bundle b = new Bundle();
				// b.putInt("dialogID", MWConstants.DIALOG_DOWNLOAD_PROGRESS);
				//
				// Intent i = new Intent(_ctx, MWHome.class);
				// i.setAction(MWConstants.HOME_HIDE_PROGRESS);
				// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
				// Intent.FLAG_ACTIVITY_SINGLE_TOP |
				// Intent.FLAG_ACTIVITY_NEW_TASK);
				// i.putExtras(b);
				// _ctx.startActivity(i);
				//
				// b = new Bundle();
				// b.putString("label", "Download alert");
				// b.putString("cont", e.toString());
				// i = new Intent(_ctx, MWHome.class);
				// i.setAction(MWConstants.HOME_SHOW_ALERT);
				// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				// | Intent.FLAG_ACTIVITY_SINGLE_TOP
				// | Intent.FLAG_ACTIVITY_NEW_TASK);
				// i.putExtras(b);
				// _ctx.startActivity(i);

			}
			return null;
		}

		protected void onProgressUpdate(String... progress) {
			// Log.d("ANDRO_ASYNC", progress[0]);
			// mProgressDialog.setProgress(Integer.parseInt(progress[0]));

			// Bundle b = new Bundle();
			// b.putInt("dialogID", MWConstants.DIALOG_DOWNLOAD_PROGRESS);
			// b.putInt("progress", Integer.parseInt(progress[0]));
			// b.putLong(MWConstants.B_PROGRESS, Integer.parseInt(progress[0]));
			// b.putLong(MWConstants.B_MAX, Integer.parseInt(progress[1]));
			// b.putString(MWConstants.B_LABEL, _downloadURL);
			//
			// Intent i = new Intent(_ctx, MWHome.class);
			// i.setAction(MWConstants.HOME_UPDATE_PROGRESS);
			// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
			// Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			// i.putExtras(b);
			// _ctx.startActivity(i);

		}

		@Override
		protected void onPostExecute(String unused) {

			// Decompress dc;
			if (_trgPath.charAt(_trgPath.length() - 1) != '/') {
				new Decompress(_zipFilePath, _trgPath + "/");
			} else {
				new Decompress(_zipFilePath, _trgPath);
			}
			// Bundle b = new Bundle();
			//
			// b.putLong(MWConstants.B_PROGRESS, 0);
			// b.putLong(MWConstants.B_MAX, 100);
			// b.putString(MWConstants.B_LABEL, "Unzipping...");
			//
			// Intent i = new Intent(_ctx, MWHome.class);
			// i.setAction(MWConstants.HOME_UPDATE_PROGRESS);
			// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
			// Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			// i.putExtras(b);
			// _ctx.startActivity(i);
			//
			// dc.unzip(_ctx);

			Log.i(getClass().getSimpleName(), "Unzip finished into: "
					+ _trgPath);

			// b = new Bundle();
			// b.putInt("dialogID", MWConstants.DIALOG_DOWNLOAD_PROGRESS);
			//
			// i = new Intent(_ctx, MWHome.class);
			// i.setAction(MWConstants.HOME_HIDE_PROGRESS);
			// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
			// Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			// i.putExtras(b);
			// _ctx.startActivity(i);

			// String jsonFile=_ctx.getFilesDir().getAbsolutePath() + "/svcs/" +
			// _uuid + "/svc.json";
			// Log.e("JSTR", jsonFile);
			// String jstr;
			// try {
			// jstr = MWConstants.readFile(jsonFile);
			// String[] ajstr = jstr.split("\n");
			// jstr="";
			//
			// for(int idx = 0;idx<ajstr.length;idx++) {
			// jstr+=ajstr[idx];
			// }
			// } catch (IOException e) {
			// // TODO: import data into DB from a file out of DB
			// jstr = "";
			// }
			//
			// Log.e("JSTR", "setService: jstr=" + jstr);
			//
			// b = new Bundle();
			// b.putString("str", jstr);
			// b.putString("uuid", _uuid);
			// b.putString("hrn", "HRN");
			// i = new Intent(_ctx, MWHome.class);
			// i.setAction(MWConstants.HOME_SET_MWSERVICE_INFO);
			// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
			// Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			// i.putExtras(b);
			// _ctx.startActivity(i);

			// TODO: on download end post an update of a service
		}
	}
}

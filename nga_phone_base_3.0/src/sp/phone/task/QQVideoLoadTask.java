package sp.phone.task;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import gov.anzong.androidnga.R;
import gov.anzong.androidnga.activity.Media_Player;
import sp.phone.fragment.ProgressDialogFragment;
import sp.phone.utils.HttpUtil;
import sp.phone.utils.StringUtil;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class QQVideoLoadTask extends AsyncTask<String, Integer, String> {

	final FragmentActivity fa ;
	static final String dialogTag = "load_qq";
	public QQVideoLoadTask(FragmentActivity fa) {
		super();
		this.fa = fa;
	}
	private boolean startIntent = true;
	@Override
	protected void onPreExecute() {
		//create progress view
		 ProgressDialogFragment pd = new  ProgressDialogFragment();
		 
		Bundle args = new Bundle();
		final String content = fa.getResources().getString(R.string.load_qq_video);
		args.putString("content", content);
		pd.setArguments(args );
		pd.show(fa.getSupportFragmentManager(), dialogTag);
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(String result) {
		if(!startIntent)
			return;
		
		if(result != null){
			Intent intent = new Intent(fa.getBaseContext(),Media_Player.class);
			Bundle b = new Bundle();
			b.putString("MEDIAPATH", result);
			intent.putExtras(b);
			fa.startActivity(intent);
		}

		this.onCancelled();

		super.onPostExecute(result);
	}

	@Override
	protected void onCancelled(String result) {
		
		this.onCancelled();
	}

	@Override
	protected void onCancelled() {
		FragmentManager fm = fa.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

        Fragment prev = fm.findFragmentByTag(dialogTag);
        if (prev != null) {
            ft.remove(prev);
            
        }
        try
        {
        	ft.commit();
        }catch(Exception e){
        	
        }
	}

	@Override
	protected String doInBackground(String... params) {
		String uri = params[0];

		try {
			uri=URLEncoder.encode(uri,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return null;
		}
		uri = uri.replaceAll("tpout.swf", "TPout.swf");
		uri="http://www.flvcd.com/parse.php?kw="+uri;
		String htmlString = HttpUtil.iosGetHtml(uri, null);
		Log.i("TAG",uri+"\n"+htmlString);
		String iid = StringUtil.getStringBetween(
				htmlString, 0, "clipurl = \"", "\"").result;
		if(StringUtil.isEmpty(iid))
			return null;
		String m3u8Url = iid;
		Log.i("TAG",iid);
		return m3u8Url;
	}

}

package cn.byr.bbs.sdk.demo.openAPI;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.byr.bbs.sdk.demo.AccessTokenKeeper;
import cn.byr.bbs.sdk.demo.R;
import cn.byrbbs.sdk.api.AttachmentApi;
import cn.byrbbs.sdk.api.model.FileMeta;
import cn.byrbbs.sdk.auth.Oauth2AccessToken;
import cn.byrbbs.sdk.exception.BBSException;
import cn.byrbbs.sdk.net.RequestListener;
import cn.byrbbs.sdk.utils.LogUtil;

public class AttachmentAPIActivity extends Activity implements OnItemClickListener {
	private static final String TAG = AttachmentAPIActivity.class.getName();

	private ListView mFuncListView;
	/** func list */
	private String[] mFuncList;

	private AttachmentApi mAttApi;

	private Oauth2AccessToken mAccessToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.openapi_item);

		mAccessToken = AccessTokenKeeper.readAccessToken(this);
		mAttApi = new AttachmentApi(mAccessToken);

		// 获取功能列表
		mFuncList = getResources().getStringArray(R.array.attachment_func_list);
		// 初始化功能列表 ListView
		mFuncListView = (ListView) findViewById(R.id.api_func_list);
		mFuncListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mFuncList));
		mFuncListView.setOnItemClickListener(this);
	}

	private RequestListener mListener = new RequestListener() {
		@Override
		public void onComplete(String response) {
			if (!TextUtils.isEmpty(response)) {
				LogUtil.i(TAG, response);

				FileMeta file = FileMeta.parse(response);
				if (file != null) {
					Toast.makeText(AttachmentAPIActivity.this,
							"Success: " + file.name,
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(AttachmentAPIActivity.this, response,
							Toast.LENGTH_LONG).show();
				}
			}
		}

		@Override
		public void onException(BBSException e) {
			// TODO Auto-generated method stub
			LogUtil.e(TAG, e.getMessage());
			// Toast.LENGTH_LONG).show();
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (view instanceof TextView) {
			if (mAccessToken != null && mAccessToken.isSessionValid()) {
				switch (position) {
				case 0:
					mAttApi.info("Zhejiang", 153371, mListener);
					break;

				case 1:
					File file = new File("tst");
					mAttApi.upload("Zhejiang", 153360, mListener, file);
					break;

				case 2:
					mAttApi.delete("Zhejiang", 153371, "logo.gif", mListener);
					break;
				default:break;
				}
			} else {
				Toast.makeText(AttachmentAPIActivity.this,
						R.string.bbsSDK_token_empty, Toast.LENGTH_LONG).show();
			}// else
		}// if( view)
	}// func
}

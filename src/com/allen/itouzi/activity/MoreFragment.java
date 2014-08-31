package com.allen.itouzi.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.allen.itouzi.R;
import com.allen.itouzi.utils.FileUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class MoreFragment extends Fragment implements OnClickListener {
	private static final String TAG = "MoreFragment";
	private Button more_btn;
	private View more_hands;
	private View more_emails;
	private FeedbackAgent agent;
	private UMSocialService mController;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root_view = inflater.inflate(R.layout.more_fragment, container,
				false);
		more_hands = root_view.findViewById(R.id.more_hands);
		more_emails = root_view.findViewById(R.id.more_emails);
		View more_uss = root_view.findViewById(R.id.more_uss);
		View more_helps = root_view.findViewById(R.id.more_helps);
		View more_phones = root_view.findViewById(R.id.more_phones);
		View more_scores = root_view.findViewById(R.id.more_scores);
		View more_commands = root_view.findViewById(R.id.more_commands);
		View more_shares = root_view.findViewById(R.id.more_shares);
		more_btn = (Button) root_view.findViewById(R.id.more_btn);

		more_hands.setOnClickListener(this);
		more_emails.setOnClickListener(this);
		more_uss.setOnClickListener(this);
		more_helps.setOnClickListener(this);
		more_phones.setOnClickListener(this);
		more_scores.setOnClickListener(this);
		more_commands.setOnClickListener(this);
		more_shares.setOnClickListener(this);
		more_btn.setOnClickListener(this);
		//UMENG
		agent = new FeedbackAgent(this.getActivity());
		// to sync the default conversation. If there is new reply, there
		// will be notification in the status bar. If you do not want
		// notification, you can use
		// agent.getDefaultConversation().sync(listener);
		// instead.
		agent.sync();
		MainActivity main=(MainActivity)getActivity();
		this.mController=main.getController();
		mController.setShareContent("��Ͷ��android�������ڲ⣬��ӭ���ʣ�����ָͷ��������ɳ�14%!��Ӧ���г�������Ͷ�ʣ����ɰ�װ�� "
				+ "http://cdn6.down.apk.gfan.com/asdf/Pfiles/2014/8/19/929966_eea60d4c-42e0-425a-aa58-e2a59fe21e4b.apk");
		mController.setShareMedia(new UMImage(getActivity(),
				"http://cdn2.image.apk.gfan.com/asdf/PBarcode/2014/8/19/929966_eea60d4c-42e0-425a-aa58-e2a59fe21e4b.png"));
		mController.getConfig().removePlatform( SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);
		return root_view;
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		if (null != (FileUtil.getToken(getActivity()))) {
			// �������� վ����
			more_hands.setVisibility(View.VISIBLE);
			more_emails.setVisibility(View.VISIBLE);
			more_btn.setText("�˳�");
		} else {
			more_hands.setVisibility(View.GONE);
			more_emails.setVisibility(View.GONE);
			more_btn.setText("��¼/ע��");
		}
		MobclickAgent.onPageStart(TAG);
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more_hands:
			//������������
			Intent lockset = new Intent(getActivity(),
					MoreLockActivity.class);
			startActivity(lockset);
			break;
		case R.id.more_emails:

			break;
		case R.id.more_uss:
			Intent aboutIntent = new Intent(getActivity(),
					AboutActivity.class);
			startActivity(aboutIntent);
			break;
		case R.id.more_helps:
			Intent helpIntent = new Intent(getActivity(),
					HelpActivity.class);
			startActivity(helpIntent);
			break;
		case R.id.more_phones:
			Intent callIntent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel://4006004300"));
			startActivity(callIntent);
			break;
		case R.id.more_scores:

			break;
		case R.id.more_commands:
			 agent.startFeedbackActivity();
			break;
		case R.id.more_shares:
	        // todo:�Ƿ�ֻ���ѵ�¼�û����ܴ򿪷���ѡ��ҳ
			
			// wx967daebe835fbeac������΢�ſ���ƽ̨ע��Ӧ�õ�AppID, ������Ҫ�滻����ע���AppID
			String appID = "wxb3e6d1f1f66bf421";
			// ���΢��ƽ̨
			UMWXHandler wxHandler = new UMWXHandler(getActivity(),appID);
			wxHandler.addToSocialSDK();
			// ֧��΢������Ȧ
			UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(),appID);
			wxCircleHandler.setToCircle(true);
			wxCircleHandler.addToSocialSDK();
			//���qq
			UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(), "1102010456",
	                "CzPHdoiHJ5z0635y");
			qqSsoHandler.addToSocialSDK();  
			QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(), "1102010456",
	                "CzPHdoiHJ5z0635y");
			qZoneSsoHandler.addToSocialSDK();
			//������Ѷ΢��SSO handler
			mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
			//��������SSO handler
//			mController.getConfig().setSsoHandler(new SinaSsoHandler());
			// ��Ӷ���
			SmsHandler smsHandler = new SmsHandler();
			smsHandler.addToSocialSDK();
			// ���email
			EmailHandler emailHandler = new EmailHandler();
			emailHandler.addToSocialSDK();
	        mController.openShare(getActivity(), false);
			break;
		case R.id.more_btn:
			if (FileUtil.getToken(getActivity()) != null) {
				FileUtil.clearToken(getActivity());
				onResume();
				//TODO:֪ͨ��������
			} else {
				Intent register = new Intent(getActivity(),
						RegisterActivity.class);
				startActivity(register);
			}
			// �˳�
			break;
		default:
			break;
		}
	}
}

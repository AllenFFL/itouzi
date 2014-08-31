package com.allen.itouzi.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allen.itouzi.bean.HomeImageMap;
import com.allen.itouzi.bean.IdanbaoMain;
import com.allen.itouzi.bean.IdanbaoProject;
import com.allen.itouzi.bean.IrongzuProject;
import com.allen.itouzi.db.ItouziDAO;
import com.allen.itouzi.net.HttpsUtil;
import com.allen.itouzi.utils.DataFormat;
import com.allen.itouzi.utils.DateUtil;
import com.allen.itouzi.utils.ValueUtil;

public class HomeEngine {
	private static String TAG = "HomeDataEngine";
	private static HomeEngine homeDataEngine = null;
	private static List<HomeImageMap> imageList = null;
	private JSONObject idanbaoMainJson = null;
	private ItouziDAO itouziDAO;

	private HomeEngine() {
	};

	public static HomeEngine getInstance() {
		if (homeDataEngine == null) {
			synchronized (HomeEngine.class) {
				if (homeDataEngine == null) {
					homeDataEngine = new HomeEngine();
					return homeDataEngine;
				}
			}
		}
		return homeDataEngine;
	};

	/**
	 * public IdanbaoMain(String id,String project_name,String
	 * project_gruantee,String project_gruantee_tab, String
	 * project_profit,String project_time,int project_amount,String
	 * lowest_account,long project_investdate, String project_paydate,String
	 * project_progress,int account_remain,int account_yes,int isForNewUser,int
	 * status){
	 */
	public void getHomeData(Context context) {
		String homeJson=null;
		itouziDAO = new ItouziDAO(context);
		try {//�ȴ������  TODO:������Ԥ�ȼ���
			homeJson = HttpsUtil.doHttpsGet(ValueUtil.serverURL
				+ ValueUtil.homeURL);
			} catch (Exception e) {
				Log.e(TAG,"getHomeData()��ȡ����ʧ�ܣ�" + "Exception e:"
							+ e.getLocalizedMessage());
			}
			Log.i(TAG, "Https homeJson:"+homeJson);
			if(homeJson==null){//û���ٴӱ��ز�
				homeJson=itouziDAO.quHome();
				Log.i(TAG, "itouziDAO homeJson:"+homeJson);
			}else{
				itouziDAO.inHome(homeJson);//���ݳ־û�����
			}
			if(homeJson!=null){
				JSONObject homeObject2 = JSONObject.parseObject(homeJson);
				if (homeObject2.getString("code").equals("0")) {
					JSONObject dataObject2 = homeObject2.getJSONObject("data");
					com.alibaba.fastjson.JSONArray slidesArray2 = dataObject2
							.getJSONArray("slides");
					com.alibaba.fastjson.JSONArray idanbaoArray2 = dataObject2
							.getJSONArray("borrow_invest");
					com.alibaba.fastjson.JSONArray irongzuArray2 = dataObject2
							.getJSONArray("borrow_lease");
					idanbaoMainJson = idanbaoArray2.getJSONObject(0);
					imageList = JSON.parseArray(slidesArray2.toJSONString(),
							HomeImageMap.class);
					}
			}
			}

	public List<HomeImageMap> getImageList(Context context) {
		getHomeData(context);
		return imageList;
	}

	/**
	 * ֻ�ǽ�����Ҫ�ֶ� ������IdanbaoProject ÿ��һ��ʱ���Զ�ȡ���� ���ݱ��� ȡ��̬����
	 * 
	 * @return other account_yes
	 */
	public List<IdanbaoMain> getIdanbaoMainList(Context context) {
		getHomeData(context);
		if (idanbaoMainJson != null) {
			List<IdanbaoMain> idanbaoMainList = new ArrayList<IdanbaoMain>();
			String project_time = DateUtil.getTimeInterval(
					idanbaoMainJson.getString("formal_time"),
					idanbaoMainJson.getString("repayment_time"));
			JSONObject guarantorObject = idanbaoMainJson
					.getJSONObject("guarInfo");
			float amountFloat = Float.parseFloat(idanbaoMainJson
					.getString("account"));
			IdanbaoMain idanbaoMain = new IdanbaoMain(
					idanbaoMainJson.getString("id_encode"),
					idanbaoMainJson.getString("name"),
					guarantorObject.getString("name"),
					"���ʵ���",
					DataFormat.getFloat(idanbaoMainJson.getString("apr")),
					project_time,
					(int) amountFloat,
					idanbaoMainJson.getString("scale"),
					Integer.parseInt(idanbaoMainJson.getString("priority_type")),
					Integer.parseInt(idanbaoMainJson.getString("status")));
			idanbaoMainList.add(idanbaoMain);
			return idanbaoMainList;
		} else {
			return null;
		}
	}

	public List<IrongzuProject> getIrongzuList() {
		// TODO:������ Ҫ�Ƽ���
		return null;
	}

}

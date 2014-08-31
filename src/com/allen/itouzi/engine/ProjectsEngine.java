package com.allen.itouzi.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.allen.itouzi.bean.Idanbao;
import com.allen.itouzi.bean.IdanbaoMain;
import com.allen.itouzi.bean.InvestRecord;
import com.allen.itouzi.db.ItouziDAO;
import com.allen.itouzi.net.HttpsUtil;
import com.allen.itouzi.utils.DataFormat;
import com.allen.itouzi.utils.DateUtil;
import com.allen.itouzi.utils.ValueUtil;

public class ProjectsEngine {
	private String TAG = "ProjectsEngine";

	private ProjectsEngine() {
	}

	private static ProjectsEngine projectsEngine = null;

	public static ProjectsEngine getInstance() {
		if (projectsEngine == null) {
			synchronized (ProjectsEngine.class) {
				if (projectsEngine == null)
					projectsEngine = new ProjectsEngine();
				return projectsEngine;
			}
		}
		return projectsEngine;
	}
	/**
	 * page1 每次默认联网刷新，找不到再查本地
	 * 其他page 每次先从数据库找，没有再联网找（找到了添加数据库）
	 * @param context
	 * @param pageIndex
	 * @return
	 */
	public ArrayList<IdanbaoMain> getIdanbaoList(Context context,int pageIndex) {// pageIndex  1234567
		String idanbaosJson=null;
		//先从本地找
		ItouziDAO itouziDAO = new ItouziDAO(context);
		if(pageIndex!=1){
			idanbaosJson = itouziDAO.quIdanbaos(pageIndex);
		}
		Log.i(TAG,"ItouziDAO idanbaosJson:"+idanbaosJson+"-----pageIndex:"+pageIndex);
			if(null==idanbaosJson){
				//找不到再联网找
				try {
					idanbaosJson = HttpsUtil.doHttpsGet(ValueUtil.serverURL
							+ ValueUtil.projectsIdanbaoURL + ValueUtil.pageURL
							+ pageIndex);
				} catch (Exception e) {
					Log.e(TAG,"getIdanbaoList(pageIndex)获取数据失败！" + "Exception e:"
									+ e.getLocalizedMessage());
				}
				Log.i(TAG, "Https getIdanbaoList :"+idanbaosJson+"-----pageIndex : " + pageIndex);
				if(null!=idanbaosJson){
					itouziDAO.inIdanbaos(pageIndex, idanbaosJson);
					return decIdanbaoJSON(idanbaosJson);
				}else{
					idanbaosJson = itouziDAO.quIdanbaos(pageIndex);
					if(null!=idanbaosJson)
						return decIdanbaoJSON(idanbaosJson);
					return null;
				}
			}
			return decIdanbaoJSON(idanbaosJson);
	}
	
	public ArrayList<IdanbaoMain> decIdanbaoJSON(String idanbaosJson){
		ArrayList<IdanbaoMain> idanbaoMainList;
		JSONObject idanbaosObject = JSONObject.parseObject(idanbaosJson);
		if (idanbaosObject.getString("code").equals("0")) {
			JSONObject dataObject = idanbaosObject.getJSONObject("data");
			JSONArray idanbaosArray = dataObject.getJSONArray("borrows");
			 idanbaoMainList = new ArrayList<IdanbaoMain>();
			for (int i = 0; i < idanbaosArray.size(); i++) {
				JSONObject idanbaoObject = idanbaosArray.getJSONObject(i);
				IdanbaoMain idanbaoMain = parseIdanbaoMain(idanbaoObject);
				idanbaoMainList.add(idanbaoMain);
			}
			return idanbaoMainList;
		}
		return null;
	}
	/**
	 * 因为爱担保和爱融租字段一致，所以暂时都用IdanbaoProject IdanbaoMain
	 * @param pageIndex
	 * @return
	 */
	public ArrayList<IdanbaoMain> getIrongzuList(Context context,int pageIndex) {
		String irongzusJson=null;
		//先从本地找
		ItouziDAO itouziDAO = new ItouziDAO(context);
		if(pageIndex!=1){
			irongzusJson = itouziDAO.quIrongzu(pageIndex);
		}
		if(irongzusJson!=null){
			return decIrongzuJson(irongzusJson);
		}else{
			//找不到再联网找
			try {
				irongzusJson = HttpsUtil.doHttpsGet(ValueUtil.serverURL
						+ ValueUtil.projectsIrongzuURL + ValueUtil.pageURL
						+ pageIndex);
			} catch (Exception e) {
				Log.e(TAG,"getIrongzuList()获取数据失败！" + "Exception e:"
								+ e.getLocalizedMessage());
			}
			Log.i(TAG, "Https getIrongzuList :"+irongzusJson+"-----pageIndex : " + pageIndex);
			if(null!=irongzusJson){
				itouziDAO.inIrongzu(pageIndex, irongzusJson);
				return decIrongzuJson(irongzusJson);
			}else{
				irongzusJson = itouziDAO.quIrongzu(pageIndex);
				Log.i(TAG,"ItouziDAO irongzusJson:"+irongzusJson+"-----pageIndex:"+pageIndex);
				if(null!=irongzusJson)
					return decIrongzuJson(irongzusJson);
				return null;
			}
		}
	}
	public ArrayList<IdanbaoMain> decIrongzuJson(String irongzusJson){
		ArrayList<IdanbaoMain> idanbaoMainList;
		JSONObject idanbaosObject = JSONObject.parseObject(irongzusJson);
		if (idanbaosObject.getString("code").equals("0")) {
			JSONObject dataObject = idanbaosObject.getJSONObject("data");
			JSONArray idanbaosArray = dataObject.getJSONArray("borrows");
			 idanbaoMainList = new ArrayList<IdanbaoMain>();
			for (int i = 0; i < idanbaosArray.size(); i++) {
				JSONObject idanbaoJson = idanbaosArray.getJSONObject(i);
				IdanbaoMain idanbaoMain = parseRongzuMain(idanbaoJson);
				idanbaoMainList.add(idanbaoMain);
			}
			return idanbaoMainList;
		}
		return null;
	}
	/**
	 * public IdanbaoMain(String id,String project_name,String project_gruantor,
	 * String project_gruantor_tab, String project_profit,String
	 * project_time,int project_amount, String project_progress,int
	 * isForNewUser,int status){
	 * 
	 * @param idanbaoObject
	 * @return
	 */
	public IdanbaoMain parseIdanbaoMain(JSONObject idanbaoObject) {
		String project_time = DateUtil.getTimeInterval(
				(String) idanbaoObject.get("formal_time"),
				(String) idanbaoObject.get("repayment_time"));
		JSONObject guarantorObject = idanbaoObject.getJSONObject("guarInfo");
		float amountFloat = Float.parseFloat((String) idanbaoObject
				.get("account"));
		IdanbaoMain idanbaoMain = new IdanbaoMain(
				idanbaoObject.getString("id_encode"),
				idanbaoObject.getString("name"),
				guarantorObject.getString("name"), "融资担保",
				DataFormat.getFloat(idanbaoObject.getString("apr")),
				project_time, (int) amountFloat,
				idanbaoObject.getString("scale"),
				Integer.parseInt(idanbaoObject.getString("priority_type")),
				Integer.parseInt(idanbaoObject.getString("status")));
		return idanbaoMain;
	}

	public IdanbaoMain parseRongzuMain(JSONObject idanbaoObject) {
		String project_time = DateUtil.getTimeInterval(
				(String) idanbaoObject.get("formal_time"),
				(String) idanbaoObject.get("repayment_time"));
		JSONObject guarantorObject = idanbaoObject.getJSONObject("guarInfo");
		float amountFloat = Float.parseFloat((String) idanbaoObject
				.get("account"));
		IdanbaoMain idanbaoMain = new IdanbaoMain(
				idanbaoObject.getString("id_encode"),
				idanbaoObject.getString("name"),
				guarantorObject.getString("name"), "融资租赁",
				DataFormat.getFloat(idanbaoObject.getString("apr")),
				project_time, (int) amountFloat,
				idanbaoObject.getString("scale"),
				Integer.parseInt(idanbaoObject.getString("priority_type")),
				Integer.parseInt(idanbaoObject.getString("status")));
		return idanbaoMain;
	}

	public Idanbao getIdanbao(String id) {
		try {
			String idanbaoJson = HttpsUtil.doHttpsGet(ValueUtil.serverURL
					+ ValueUtil.idanbaoURL + ValueUtil.idURL + id);
			JSONObject idanbaoObject = JSONObject.parseObject(idanbaoJson);
			if (idanbaoObject.getString("code").equals("0")) {
//				Log.i(TAG, "id" + id + "--idanbaoJson:" + idanbaoJson);
				JSONObject dataObject = idanbaoObject.getJSONObject("data");
				JSONObject guarantorObject = dataObject
						.getJSONObject("guarantor");
				JSONObject idanbaoObj = dataObject
						.getJSONObject("borrow_nocache");
				Idanbao idanbao = parseIdanbao(idanbaoObj, guarantorObject);
				idanbao.setId(id);
				return idanbao;
			}
		} catch (Exception e) {
			Log.e(TAG, "getIdanbao(String id)()获取数据失败！id:" + id
					+ "--Exception e:" + e.getLocalizedMessage());
		}
		return null;
	}

	public Idanbao getIrongzu(String id) {
		try {
			String idanbaoJson = HttpsUtil.doHttpsGet(ValueUtil.serverURL
					+ ValueUtil.irongzuURL + ValueUtil.idURL + id);
			JSONObject idanbaoObject = JSONObject.parseObject(idanbaoJson);
			if (idanbaoObject.getString("code").equals("0")) {
//				Log.i(TAG, "id" + id + "--idanbaoJson:" + idanbaoJson);
				JSONObject dataObject = idanbaoObject.getJSONObject("data");
				JSONObject guarantorObject = dataObject
						.getJSONObject("guarantor");
				JSONObject idanbaoObj = dataObject
						.getJSONObject("borrow_nocache");
				Idanbao idanbao = parseIdanbao(idanbaoObj, guarantorObject);
				idanbao.setId(id);
				return idanbao;
			}
		} catch (Exception e) {
			Log.e(TAG, "getIdanbao(String id)()获取数据失败！id:" + id
					+ "--Exception e:" + e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * 
	 * @param idanbaoObj
	 * @param guarantor
	 * @return
	 */
	private Idanbao parseIdanbao(JSONObject idanbaoObj,
			JSONObject guarantorObject) {
		JSONObject idanbaoNocache = idanbaoObj.getJSONObject("borrow");
		String project_time = DateUtil.getTimeInterval(
				(String) idanbaoNocache.get("formal_time"),
				(String) idanbaoNocache.get("repayment_time"));
		float amountFloat = Float.parseFloat((String) idanbaoNocache
				.get("account"));
		/**
		 * public Idanbao(String id, String project_name, String project_profit,
		 * String project_time, int project_amount, String lowest_account, long
		 * project_investdate, String project_paydate, String project_progress,
		 * String invest_persons, Integer account_remain, int account_yes, int
		 * forNewUser, int status, String use_detail, String repayment_source,
		 * String content, String guarantor_name, String project_gruantor_tab,
		 * String guarantor_summary, String guarantor_bankbranch, String
		 * guarantor_bankcardid, String mortgage_info, String risk_control,
		 * String guarantor_opinion) {
		 */
		Idanbao idanbao = new Idanbao(
				idanbaoNocache.getString("id"),
				idanbaoNocache.getString("name"),
				DataFormat.getFloat((String) idanbaoNocache.get("apr")),
				project_time,
				(int) amountFloat,
				idanbaoNocache.getString("lowest_account"),
				Long.parseLong(idanbaoNocache.getString("formal_time")) * 1000l,
				DateUtil.getDateToString(Long.parseLong(idanbaoNocache
						.getString("repayment_time")) * 1000), idanbaoNocache
						.getString("scale"), idanbaoNocache
						.getString("tender_times"), (Integer) idanbaoNocache
						.get("other"), (int) Float.parseFloat(idanbaoNocache
						.getString("account_yes")), Integer
						.parseInt(idanbaoNocache.getString("priority_type")),
				Integer.parseInt(idanbaoNocache.getString("status")),
				idanbaoNocache.getString("use_detail"), idanbaoNocache
						.getString("repayment_source"), idanbaoNocache
						.getString("content"), guarantorObject
						.getString("name"), "融资担保", guarantorObject
						.getString("summary"), guarantorObject
						.getString("bankbranch"), guarantorObject
						.getString("bankcardid"), idanbaoNocache
						.getString("mortgage_info"), idanbaoNocache
						.getString("risk_control"), idanbaoNocache
						.getString("guarantor_opinion"));
		return idanbao;
	}

	public List<InvestRecord> getRecord(String id, String token) {
		ArrayList<InvestRecord> records = null;
		if (token != null) {
			// TODO:登录了传token 显示全部
		} else {
			// 获取10条数据
			try {
				String recordsJson = HttpsUtil.doHttpsGet(ValueUtil.serverURL
						+ ValueUtil.recordURL + ValueUtil.idURL + id);
				JSONObject recordsObj = JSON.parseObject(recordsJson);
				if (recordsObj.getString("code").equals("0")) {
					records = new ArrayList<InvestRecord>();
					JSONObject dataObj = recordsObj.getJSONObject("data");
					JSONArray recordArray = dataObj
							.getJSONArray("borrow_tender");
					InvestRecord record = null;
					for (int i = 0; i < recordArray.size(); i++) {
						JSONObject recordObj = recordArray.getJSONObject(i);
						JSONObject userObj = recordObj
								.getJSONObject("userInfo");
						String username = null;
						if (userObj != null) {
							username = userObj.getString("username");
						} else {
							username = "";
						}
						String amount = recordObj.getString("account");
						String time = recordObj.getString("addtime");
						record = new InvestRecord(username, amount, time);
						records.add(record);
					}
					return records;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		return null;

	}

}

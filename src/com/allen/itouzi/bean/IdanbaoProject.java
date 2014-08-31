package com.allen.itouzi.bean;

import java.io.Serializable;

public class IdanbaoProject implements Serializable {
	private String id, site_id, user_id, name, status, guarantor_status, order,
			hits, litpic, flag, is_vouch, type, guarantors, original_creditor,
			original_creditor_ID, view_type, vouch_award, vouch_user,
			vouch_account, vouch_times, source, publish, customer, number_id,
			verify_user, verify_time, verify_remark, repayment_user,
			forst_account, repayment_account, monthly_repayment,
			repayment_yesaccount, repayment_yesinterest, repayment_time,
			repayment_remark, success_time, end_time, payment_account,
			each_time, use, use_detail, repayment_source, mortgage_info,
			mortgage_rate, compensate_delay_days, risk_control, lease_subject,
			relevant_policies, market_analysis, borrow_remark, time_limit,
			style, account, account_yes, tender_times, apr, lowest_account,
			most_account, invest_step, valid_time, delay_value_days, award,
			part_account, funds, is_false, open_account, open_borrow,
			open_tender, open_credit, content, agreement_template, addtime,
			last_tender_time, addip, is_do, guarantor_remark,
			guarantor_opinion, guarantor_verify_status, is_rewarded,
			is_join_reward, priority_type, organiser, formal_time,
			risk_insurance, score, complaint_information,
			internal_audit_status, online_remark, other, scale, scale_width,
			now_invest_value_date, leave_time, id_encode;
	private IdanbaoProfit profit;
	private IdanbaoGuarantor guarantor;

	public IdanbaoProject() {
	}

	public IdanbaoProject(String id, String site_id, String user_id,
			String name, String status, String guarantor_status, String order,
			String hits, String litpic, String flag, String is_vouch,
			String type, String guarantors, String original_creditor,
			String original_creditor_ID, String view_type, String vouch_award,
			String vouch_user, String vouch_account, String vouch_times,
			String source, String publish, String customer, String number_id,
			String verify_user, String verify_time, String verify_remark,
			String repayment_user, String forst_account,
			String repayment_account, String monthly_repayment,
			String repayment_yesaccount, String repayment_yesinterest,
			String repayment_time, String repayment_remark,
			String success_time, String end_time, String payment_account,
			String each_time, String use, String use_detail,
			String repayment_source, String mortgage_info,
			String mortgage_rate, String compensate_delay_days,
			String risk_control, String lease_subject,
			String relevant_policies, String market_analysis,
			String borrow_remark, String time_limit, String style,
			String account, String account_yes, String tender_times,
			String apr, String lowest_account, String most_account,
			String invest_step, String valid_time, String delay_value_days,
			String award, String part_account, String funds, String is_false,
			String open_account, String open_borrow, String open_tender,
			String open_credit, String content, String agreement_template,
			String addtime, String last_tender_time, String addip,
			String is_do, String guarantor_remark, String guarantor_opinion,
			String guarantor_verify_status, String is_rewarded,
			String is_join_reward, String priority_type, String organiser,
			String formal_time, String risk_insurance, String score,
			String complaint_information, String internal_audit_status,
			String online_remark, String other, String scale,
			String scale_width, String now_invest_value_date,
			String leave_time, String id_encode, IdanbaoProfit profit,
			IdanbaoGuarantor guarantor) {
		super();
		this.id = id;
		this.site_id = site_id;
		this.user_id = user_id;
		this.name = name;
		this.status = status;
		this.guarantor_status = guarantor_status;
		this.order = order;
		this.hits = hits;
		this.litpic = litpic;
		this.flag = flag;
		this.is_vouch = is_vouch;
		this.type = type;
		this.guarantors = guarantors;
		this.original_creditor = original_creditor;
		this.original_creditor_ID = original_creditor_ID;
		this.view_type = view_type;
		this.vouch_award = vouch_award;
		this.vouch_user = vouch_user;
		this.vouch_account = vouch_account;
		this.vouch_times = vouch_times;
		this.source = source;
		this.publish = publish;
		this.customer = customer;
		this.number_id = number_id;
		this.verify_user = verify_user;
		this.verify_time = verify_time;
		this.verify_remark = verify_remark;
		this.repayment_user = repayment_user;
		this.forst_account = forst_account;
		this.repayment_account = repayment_account;
		this.monthly_repayment = monthly_repayment;
		this.repayment_yesaccount = repayment_yesaccount;
		this.repayment_yesinterest = repayment_yesinterest;
		this.repayment_time = repayment_time;
		this.repayment_remark = repayment_remark;
		this.success_time = success_time;
		this.end_time = end_time;
		this.payment_account = payment_account;
		this.each_time = each_time;
		this.use = use;
		this.use_detail = use_detail;
		this.repayment_source = repayment_source;
		this.mortgage_info = mortgage_info;
		this.mortgage_rate = mortgage_rate;
		this.compensate_delay_days = compensate_delay_days;
		this.risk_control = risk_control;
		this.lease_subject = lease_subject;
		this.relevant_policies = relevant_policies;
		this.market_analysis = market_analysis;
		this.borrow_remark = borrow_remark;
		this.time_limit = time_limit;
		this.style = style;
		this.account = account;
		this.account_yes = account_yes;
		this.tender_times = tender_times;
		this.apr = apr;
		this.lowest_account = lowest_account;
		this.most_account = most_account;
		this.invest_step = invest_step;
		this.valid_time = valid_time;
		this.delay_value_days = delay_value_days;
		this.award = award;
		this.part_account = part_account;
		this.funds = funds;
		this.is_false = is_false;
		this.open_account = open_account;
		this.open_borrow = open_borrow;
		this.open_tender = open_tender;
		this.open_credit = open_credit;
		this.content = content;
		this.agreement_template = agreement_template;
		this.addtime = addtime;
		this.last_tender_time = last_tender_time;
		this.addip = addip;
		this.is_do = is_do;
		this.guarantor_remark = guarantor_remark;
		this.guarantor_opinion = guarantor_opinion;
		this.guarantor_verify_status = guarantor_verify_status;
		this.is_rewarded = is_rewarded;
		this.is_join_reward = is_join_reward;
		this.priority_type = priority_type;
		this.organiser = organiser;
		this.formal_time = formal_time;
		this.risk_insurance = risk_insurance;
		this.score = score;
		this.complaint_information = complaint_information;
		this.internal_audit_status = internal_audit_status;
		this.online_remark = online_remark;
		this.other = other;
		this.scale = scale;
		this.scale_width = scale_width;
		this.now_invest_value_date = now_invest_value_date;
		this.leave_time = leave_time;
		this.id_encode = id_encode;
		this.profit = profit;
		this.guarantor = guarantor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGuarantor_status() {
		return guarantor_status;
	}

	public void setGuarantor_status(String guarantor_status) {
		this.guarantor_status = guarantor_status;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getHits() {
		return hits;
	}

	public void setHits(String hits) {
		this.hits = hits;
	}

	public String getLitpic() {
		return litpic;
	}

	public void setLitpic(String litpic) {
		this.litpic = litpic;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getIs_vouch() {
		return is_vouch;
	}

	public void setIs_vouch(String is_vouch) {
		this.is_vouch = is_vouch;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGuarantors() {
		return guarantors;
	}

	public void setGuarantors(String guarantors) {
		this.guarantors = guarantors;
	}

	public String getOriginal_creditor() {
		return original_creditor;
	}

	public void setOriginal_creditor(String original_creditor) {
		this.original_creditor = original_creditor;
	}

	public String getOriginal_creditor_ID() {
		return original_creditor_ID;
	}

	public void setOriginal_creditor_ID(String original_creditor_ID) {
		this.original_creditor_ID = original_creditor_ID;
	}

	public String getView_type() {
		return view_type;
	}

	public void setView_type(String view_type) {
		this.view_type = view_type;
	}

	public String getVouch_award() {
		return vouch_award;
	}

	public void setVouch_award(String vouch_award) {
		this.vouch_award = vouch_award;
	}

	public String getVouch_user() {
		return vouch_user;
	}

	public void setVouch_user(String vouch_user) {
		this.vouch_user = vouch_user;
	}

	public String getVouch_account() {
		return vouch_account;
	}

	public void setVouch_account(String vouch_account) {
		this.vouch_account = vouch_account;
	}

	public String getVouch_times() {
		return vouch_times;
	}

	public void setVouch_times(String vouch_times) {
		this.vouch_times = vouch_times;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPublish() {
		return publish;
	}

	public void setPublish(String publish) {
		this.publish = publish;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getNumber_id() {
		return number_id;
	}

	public void setNumber_id(String number_id) {
		this.number_id = number_id;
	}

	public String getVerify_user() {
		return verify_user;
	}

	public void setVerify_user(String verify_user) {
		this.verify_user = verify_user;
	}

	public String getVerify_time() {
		return verify_time;
	}

	public void setVerify_time(String verify_time) {
		this.verify_time = verify_time;
	}

	public String getVerify_remark() {
		return verify_remark;
	}

	public void setVerify_remark(String verify_remark) {
		this.verify_remark = verify_remark;
	}

	public String getRepayment_user() {
		return repayment_user;
	}

	public void setRepayment_user(String repayment_user) {
		this.repayment_user = repayment_user;
	}

	public String getForst_account() {
		return forst_account;
	}

	public void setForst_account(String forst_account) {
		this.forst_account = forst_account;
	}

	public String getRepayment_account() {
		return repayment_account;
	}

	public void setRepayment_account(String repayment_account) {
		this.repayment_account = repayment_account;
	}

	public String getMonthly_repayment() {
		return monthly_repayment;
	}

	public void setMonthly_repayment(String monthly_repayment) {
		this.monthly_repayment = monthly_repayment;
	}

	public String getRepayment_yesaccount() {
		return repayment_yesaccount;
	}

	public void setRepayment_yesaccount(String repayment_yesaccount) {
		this.repayment_yesaccount = repayment_yesaccount;
	}

	public String getRepayment_yesinterest() {
		return repayment_yesinterest;
	}

	public void setRepayment_yesinterest(String repayment_yesinterest) {
		this.repayment_yesinterest = repayment_yesinterest;
	}

	public String getRepayment_time() {
		return repayment_time;
	}

	public void setRepayment_time(String repayment_time) {
		this.repayment_time = repayment_time;
	}

	public String getRepayment_remark() {
		return repayment_remark;
	}

	public void setRepayment_remark(String repayment_remark) {
		this.repayment_remark = repayment_remark;
	}

	public String getSuccess_time() {
		return success_time;
	}

	public void setSuccess_time(String success_time) {
		this.success_time = success_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getPayment_account() {
		return payment_account;
	}

	public void setPayment_account(String payment_account) {
		this.payment_account = payment_account;
	}

	public String getEach_time() {
		return each_time;
	}

	public void setEach_time(String each_time) {
		this.each_time = each_time;
	}

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public String getUse_detail() {
		return use_detail;
	}

	public void setUse_detail(String use_detail) {
		this.use_detail = use_detail;
	}

	public String getRepayment_source() {
		return repayment_source;
	}

	public void setRepayment_source(String repayment_source) {
		this.repayment_source = repayment_source;
	}

	public String getMortgage_info() {
		return mortgage_info;
	}

	public void setMortgage_info(String mortgage_info) {
		this.mortgage_info = mortgage_info;
	}

	public String getMortgage_rate() {
		return mortgage_rate;
	}

	public void setMortgage_rate(String mortgage_rate) {
		this.mortgage_rate = mortgage_rate;
	}

	public String getCompensate_delay_days() {
		return compensate_delay_days;
	}

	public void setCompensate_delay_days(String compensate_delay_days) {
		this.compensate_delay_days = compensate_delay_days;
	}

	public String getRisk_control() {
		return risk_control;
	}

	public void setRisk_control(String risk_control) {
		this.risk_control = risk_control;
	}

	public String getLease_subject() {
		return lease_subject;
	}

	public void setLease_subject(String lease_subject) {
		this.lease_subject = lease_subject;
	}

	public String getRelevant_policies() {
		return relevant_policies;
	}

	public void setRelevant_policies(String relevant_policies) {
		this.relevant_policies = relevant_policies;
	}

	public String getMarket_analysis() {
		return market_analysis;
	}

	public void setMarket_analysis(String market_analysis) {
		this.market_analysis = market_analysis;
	}

	public String getBorrow_remark() {
		return borrow_remark;
	}

	public void setBorrow_remark(String borrow_remark) {
		this.borrow_remark = borrow_remark;
	}

	public String getTime_limit() {
		return time_limit;
	}

	public void setTime_limit(String time_limit) {
		this.time_limit = time_limit;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccount_yes() {
		return account_yes;
	}

	public void setAccount_yes(String account_yes) {
		this.account_yes = account_yes;
	}

	public String getTender_times() {
		return tender_times;
	}

	public void setTender_times(String tender_times) {
		this.tender_times = tender_times;
	}

	public String getApr() {
		return apr;
	}

	public void setApr(String apr) {
		this.apr = apr;
	}

	public String getLowest_account() {
		return lowest_account;
	}

	public void setLowest_account(String lowest_account) {
		this.lowest_account = lowest_account;
	}

	public String getMost_account() {
		return most_account;
	}

	public void setMost_account(String most_account) {
		this.most_account = most_account;
	}

	public String getInvest_step() {
		return invest_step;
	}

	public void setInvest_step(String invest_step) {
		this.invest_step = invest_step;
	}

	public String getValid_time() {
		return valid_time;
	}

	public void setValid_time(String valid_time) {
		this.valid_time = valid_time;
	}

	public String getDelay_value_days() {
		return delay_value_days;
	}

	public void setDelay_value_days(String delay_value_days) {
		this.delay_value_days = delay_value_days;
	}

	public String getAward() {
		return award;
	}

	public void setAward(String award) {
		this.award = award;
	}

	public String getPart_account() {
		return part_account;
	}

	public void setPart_account(String part_account) {
		this.part_account = part_account;
	}

	public String getFunds() {
		return funds;
	}

	public void setFunds(String funds) {
		this.funds = funds;
	}

	public String getIs_false() {
		return is_false;
	}

	public void setIs_false(String is_false) {
		this.is_false = is_false;
	}

	public String getOpen_account() {
		return open_account;
	}

	public void setOpen_account(String open_account) {
		this.open_account = open_account;
	}

	public String getOpen_borrow() {
		return open_borrow;
	}

	public void setOpen_borrow(String open_borrow) {
		this.open_borrow = open_borrow;
	}

	public String getOpen_tender() {
		return open_tender;
	}

	public void setOpen_tender(String open_tender) {
		this.open_tender = open_tender;
	}

	public String getOpen_credit() {
		return open_credit;
	}

	public void setOpen_credit(String open_credit) {
		this.open_credit = open_credit;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAgreement_template() {
		return agreement_template;
	}

	public void setAgreement_template(String agreement_template) {
		this.agreement_template = agreement_template;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getLast_tender_time() {
		return last_tender_time;
	}

	public void setLast_tender_time(String last_tender_time) {
		this.last_tender_time = last_tender_time;
	}

	public String getAddip() {
		return addip;
	}

	public void setAddip(String addip) {
		this.addip = addip;
	}

	public String getIs_do() {
		return is_do;
	}

	public void setIs_do(String is_do) {
		this.is_do = is_do;
	}

	public String getGuarantor_remark() {
		return guarantor_remark;
	}

	public void setGuarantor_remark(String guarantor_remark) {
		this.guarantor_remark = guarantor_remark;
	}

	public String getGuarantor_opinion() {
		return guarantor_opinion;
	}

	public void setGuarantor_opinion(String guarantor_opinion) {
		this.guarantor_opinion = guarantor_opinion;
	}

	public String getGuarantor_verify_status() {
		return guarantor_verify_status;
	}

	public void setGuarantor_verify_status(String guarantor_verify_status) {
		this.guarantor_verify_status = guarantor_verify_status;
	}

	public String getIs_rewarded() {
		return is_rewarded;
	}

	public void setIs_rewarded(String is_rewarded) {
		this.is_rewarded = is_rewarded;
	}

	public String getIs_join_reward() {
		return is_join_reward;
	}

	public void setIs_join_reward(String is_join_reward) {
		this.is_join_reward = is_join_reward;
	}

	public String getPriority_type() {
		return priority_type;
	}

	public void setPriority_type(String priority_type) {
		this.priority_type = priority_type;
	}

	public String getOrganiser() {
		return organiser;
	}

	public void setOrganiser(String organiser) {
		this.organiser = organiser;
	}

	public String getFormal_time() {
		return formal_time;
	}

	public void setFormal_time(String formal_time) {
		this.formal_time = formal_time;
	}

	public String getRisk_insurance() {
		return risk_insurance;
	}

	public void setRisk_insurance(String risk_insurance) {
		this.risk_insurance = risk_insurance;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getComplaint_information() {
		return complaint_information;
	}

	public void setComplaint_information(String complaint_information) {
		this.complaint_information = complaint_information;
	}

	public String getInternal_audit_status() {
		return internal_audit_status;
	}

	public void setInternal_audit_status(String internal_audit_status) {
		this.internal_audit_status = internal_audit_status;
	}

	public String getOnline_remark() {
		return online_remark;
	}

	public void setOnline_remark(String online_remark) {
		this.online_remark = online_remark;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getScale_width() {
		return scale_width;
	}

	public void setScale_width(String scale_width) {
		this.scale_width = scale_width;
	}

	public String getNow_invest_value_date() {
		return now_invest_value_date;
	}

	public void setNow_invest_value_date(String now_invest_value_date) {
		this.now_invest_value_date = now_invest_value_date;
	}

	public String getLeave_time() {
		return leave_time;
	}

	public void setLeave_time(String leave_time) {
		this.leave_time = leave_time;
	}

	public String getId_encode() {
		return id_encode;
	}

	public void setId_encode(String id_encode) {
		this.id_encode = id_encode;
	}

	public IdanbaoProfit getProfit() {
		return profit;
	}

	public void setProfit(IdanbaoProfit profit) {
		this.profit = profit;
	}

	public IdanbaoGuarantor getGuarantor() {
		return guarantor;
	}

	public void setGuarantor(IdanbaoGuarantor guarantor) {
		this.guarantor = guarantor;
	}

	@Override
	public String toString() {
		return "IdanbaoProject [id=" + id + ", site_id=" + site_id
				+ ", user_id=" + user_id + ", name=" + name + ", status="
				+ status + ", guarantor_status=" + guarantor_status
				+ ", order=" + order + ", hits=" + hits + ", litpic=" + litpic
				+ ", flag=" + flag + ", is_vouch=" + is_vouch + ", type="
				+ type + ", guarantors=" + guarantors + ", original_creditor="
				+ original_creditor + ", original_creditor_ID="
				+ original_creditor_ID + ", view_type=" + view_type
				+ ", vouch_award=" + vouch_award + ", vouch_user=" + vouch_user
				+ ", vouch_account=" + vouch_account + ", vouch_times="
				+ vouch_times + ", source=" + source + ", publish=" + publish
				+ ", customer=" + customer + ", number_id=" + number_id
				+ ", verify_user=" + verify_user + ", verify_time="
				+ verify_time + ", verify_remark=" + verify_remark
				+ ", repayment_user=" + repayment_user + ", forst_account="
				+ forst_account + ", repayment_account=" + repayment_account
				+ ", monthly_repayment=" + monthly_repayment
				+ ", repayment_yesaccount=" + repayment_yesaccount
				+ ", repayment_yesinterest=" + repayment_yesinterest
				+ ", repayment_time=" + repayment_time + ", repayment_remark="
				+ repayment_remark + ", success_time=" + success_time
				+ ", end_time=" + end_time + ", payment_account="
				+ payment_account + ", each_time=" + each_time + ", use=" + use
				+ ", use_detail=" + use_detail + ", repayment_source="
				+ repayment_source + ", mortgage_info=" + mortgage_info
				+ ", mortgage_rate=" + mortgage_rate
				+ ", compensate_delay_days=" + compensate_delay_days
				+ ", risk_control=" + risk_control + ", lease_subject="
				+ lease_subject + ", relevant_policies=" + relevant_policies
				+ ", market_analysis=" + market_analysis + ", borrow_remark="
				+ borrow_remark + ", time_limit=" + time_limit + ", style="
				+ style + ", account=" + account + ", account_yes="
				+ account_yes + ", tender_times=" + tender_times + ", apr="
				+ apr + ", lowest_account=" + lowest_account
				+ ", most_account=" + most_account + ", invest_step="
				+ invest_step + ", valid_time=" + valid_time
				+ ", delay_value_days=" + delay_value_days + ", award=" + award
				+ ", part_account=" + part_account + ", funds=" + funds
				+ ", is_false=" + is_false + ", open_account=" + open_account
				+ ", open_borrow=" + open_borrow + ", open_tender="
				+ open_tender + ", open_credit=" + open_credit + ", content="
				+ content + ", agreement_template=" + agreement_template
				+ ", addtime=" + addtime + ", last_tender_time="
				+ last_tender_time + ", addip=" + addip + ", is_do=" + is_do
				+ ", guarantor_remark=" + guarantor_remark
				+ ", guarantor_opinion=" + guarantor_opinion
				+ ", guarantor_verify_status=" + guarantor_verify_status
				+ ", is_rewarded=" + is_rewarded + ", is_join_reward="
				+ is_join_reward + ", priority_type=" + priority_type
				+ ", organiser=" + organiser + ", formal_time=" + formal_time
				+ ", risk_insurance=" + risk_insurance + ", score=" + score
				+ ", complaint_information=" + complaint_information
				+ ", internal_audit_status=" + internal_audit_status
				+ ", online_remark=" + online_remark + ", other=" + other
				+ ", scale=" + scale + ", scale_width=" + scale_width
				+ ", now_invest_value_date=" + now_invest_value_date
				+ ", leave_time=" + leave_time + ", id_encode=" + id_encode
				+ ", profit=" + profit + ", guarantor=" + guarantor + "]";
	}

}

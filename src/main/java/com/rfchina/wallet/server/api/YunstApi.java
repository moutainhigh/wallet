package com.rfchina.wallet.server.api;

import com.rfchina.wallet.server.yunst.response.YunstCreateMemberResp;

public interface YunstApi {
	/**
	 * 注册云商通会员
	 * @param accessToken
	 * @param bizUserId  业务用户id
	 * @param type 业务用户类型 1-个人 2-企业
	 * @return
	 */
	YunstCreateMemberResp createYunstMember(String accessToken,String bizUserId,Integer type) throws Exception;
}

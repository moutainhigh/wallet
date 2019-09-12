package com.rfchina.wallet.server.api;

import com.rfchina.platform.common.misc.Tuple;
import com.rfchina.wallet.server.bank.yunst.response.result.YunstCreateMemberResult;
import com.rfchina.wallet.server.bank.yunst.response.YunstMemberInfoResp;

public interface YunstApi {
	/**
	 * 注册云商通会员
	 *
	 * @param accessToken
	 * @param bizUserId   业务用户id
	 * @param type        业务用户类型 1-企业 2-个人
	 * @return
	 */
	YunstCreateMemberResult createYunstMember(String accessToken, String bizUserId, Integer type) throws Exception;

	/**
	 * 请求短信验证码
	 *
	 * @param accessToken
	 * @param bizUserId   业务用户id
	 * @param type        业务用户类型 1-企业 2-个人
	 * @param phone       电话号码
	 * @param bizType     验证码业务类型  9-绑定手机
	 * @return
	 * @throws Exception
	 */
//	Tuple<Boolean, String> requestSmsVerifyCode(String accessToken, String bizUserId, Integer type, String phone,
//			Integer bizType) throws Exception;

	/**
	 * 绑定手机
	 *
	 * @param accessToken
	 * @param bizUserId        业务用户id
	 * @param type             业务用户类型 1-企业 2-个人
	 * @param phone            电话号码
	 * @param verificationCode 验证码
	 * @return
	 * @throws Exception
	 */
//	Tuple<Boolean, String> bindPhone(String accessToken, String bizUserId, Integer type, String phone,
//			String verificationCode) throws Exception;

	/**
	 * 修改绑定手机
	 *
	 * @param accessToken
	 * @param bizUserId        业务用户id
	 * @param type             业务用户类型 1-企业 2-个人
	 * @param oldPhone         旧电话号码
	 * @param newPhone         新电话号码
	 * @param verificationCode 验证码
	 * @return
	 * @throws Exception
	 */
//	Tuple<Boolean, String> modifyPhone(String accessToken, String bizUserId, Integer type, String oldPhone,
//			String newPhone, String verificationCode) throws Exception;


	/**
	 * 获取会员信息
	 *
	 * @param accessToken
	 * @param bizUserId        业务用户id
	 * @param type             业务用户类型 1-企业 2-个人
	 * @return
	 * @throws Exception
	 */
//	Object getMemberInfo(String accessToken, String bizUserId, Integer type) throws Exception;
}

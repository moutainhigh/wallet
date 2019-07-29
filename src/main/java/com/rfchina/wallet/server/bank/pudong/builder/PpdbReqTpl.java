package com.rfchina.wallet.server.bank.pudong.builder;

import com.alibaba.fastjson.JSON;
import com.rfchina.wallet.server.bank.pudong.domain.common.RequestHeader;
import com.rfchina.wallet.server.bank.pudong.domain.common.RequestPacket;
import com.rfchina.wallet.server.bank.pudong.domain.common.ResponseHeader;
import com.rfchina.wallet.server.bank.pudong.domain.common.ResponsePacket;
import com.rfchina.wallet.server.bank.pudong.domain.common.SignedBody;
import com.rfchina.wallet.server.bank.pudong.domain.exception.GatewayError;
import com.rfchina.wallet.server.bank.pudong.domain.util.XmlUtil;
import com.rfchina.wallet.domain.exception.WalletResponseException;
import com.rfchina.wallet.domain.misc.WalletResponseCode.EnumWalletResponseCode;
import java.nio.charset.Charset;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

/**
 * 浦发网关请求模板类
 *
 * @author nzm
 */
@Slf4j
public abstract class PpdbReqTpl {

	private final static String SIGN_PREFIX = "<sign>";
	private final static String SIGN_SUFFIX = "</sign>";
	private final static String SIC_PREFIX = "<sic>";

	private final static String SIC_SUFFIX = "</sic>";
	public static final String SIGN_FLAG = "1";
	private static final String SUCC = "AAAAAAA";
	public static final Charset SERVER_CHARSET = Charset.forName("GBK");
	private static Boolean isServerGBK = true;


	protected OkHttpClient client;

	/**
	 * 网关签名, 不规则格式？根据浦发例子改造 example: <html><head><title>...</title><result>0</result></head><body><sign>...</sign></body></html>
	 *
	 * @param xmlData 需要签名的body
	 */
	public String sign(String signUrl, String xmlData) throws Exception {

		Request request = new Request.Builder()
			.url(signUrl)
			.addHeader("Content-Type", "INFOSEC_SIGN/1.0")
			.addHeader("Content-Length", String.valueOf(xmlData.length()))
			.post(RequestBody.create(MediaType.parse("INFOSEC_SIGN/1.0"), reqAdvise(xmlData)))
			.build();

		Response resp = client.newCall(request).execute();
		checkRespCode(resp);

		String body = respAdvise(resp.body().bytes());
		if (StringUtils.isEmpty(body) || !body.contains(SIGN_PREFIX)) {
			throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_SINGED_ERROR);
		}

		return XmlUtil.extract(body, SIGN_PREFIX, SIGN_SUFFIX);
	}

	/**
	 * 网关解签验签
	 *
	 * @param signData 网关返回的Signature
	 */
	public String unsign(String signUrl, String signData) throws Exception {

		Request request = new Request.Builder()
			.url(signUrl)
			.addHeader("Content-Type", "INFOSEC_VERIFY_SIGN/1.0")
			.addHeader("Content-Length", String.valueOf(signData.length()))
			.post(RequestBody.create(MediaType.parse("INFOSEC_VERIFY_SIGN/1.0")
				, reqAdvise(signData)))
			.build();

		Response resp = client.newCall(request).execute();
		checkRespCode(resp);

		String body = respAdvise(resp.body().bytes());
		return XmlUtil.extract(body, SIC_PREFIX, SIC_SUFFIX);
	}


	abstract <T> T buildReqBody();

	protected String parseAndSign(String signUrl, Object reqBody, Class clz) throws Exception {
		String xmlBody = XmlUtil.obj2Xml(reqBody, clz);
		log.info("银企直连，请求报文 {}",xmlBody);
		return sign(signUrl, xmlBody);
	}

	abstract RequestHeader buildReqestHeader();

	protected RequestPacket buildRequestPacket(RequestHeader header, String signature) {
		return RequestPacket.builder()
			.head(header)
			.body(SignedBody.builder().signature(signature).build())
			.build();
	}

	protected ResponsePacket doExec(String hostUrl, String signUrl, RequestPacket requestPacket)
		throws Exception {
		String xmlData = XmlUtil.wrap(XmlUtil.obj2Xml(requestPacket, RequestPacket.class));

		Request request = new Request.Builder()
			.url(hostUrl)
			.addHeader("Content-Type", "text/plain")
			.addHeader("Content-Length", String.valueOf(xmlData.length()))
			.post(RequestBody.create(MediaType.parse("INFOSEC_SIGN/1.0"), reqAdvise(xmlData)))
			.build();
		Response resp = client.newCall(request).execute();

		String respData = respAdvise(resp.body().bytes());
		try {
			ResponsePacket responsePacket = XmlUtil
				.xml2Obj(XmlUtil.unwrap(respData), ResponsePacket.class);
			ResponseHeader head = responsePacket.getHead();
			SignedBody body = responsePacket.getBody();
			// 银企直连接口错误时，返回的接口没有进行签名
			if (!SUCC.equals(head.getReturnCode())) {
				log.error("银企直连接口错误, request = {} , response = {}", xmlData, respData);
				String errMsg = StringUtils.isNotBlank(head.getReturnMsg()) ? head.getReturnMsg()
					: (body != null ? body.getReturnMsg() : null);
				throw GatewayError.builder()
					.transCode(requestPacket.getHead().getTransCode())
					.errCode(head.getReturnCode())
					.errMsg(errMsg)
					.build();
			}
			return responsePacket;
		} catch (GatewayError e) {
			throw e;
		} catch (Exception e) {
			log.error("银企直连接口错误, request = {} , response = {}", xmlData, respData);
			throw new WalletResponseException(
				EnumWalletResponseCode.PAY_IN_GATEWAY_RESPONSE_ERROR);
		}
	}

	private byte[] reqAdvise(String xmlData) {
		return xmlData.getBytes(SERVER_CHARSET);
	}

	private String respAdvise(byte[] bytes) {
		return new String(bytes, SERVER_CHARSET);
	}

	protected String unsign(String signUrl, ResponsePacket responsePacket) throws Exception {
		return unsign(signUrl, responsePacket.getBody().getSignature());
	}

	protected <T> T extractRespObj(String xmlData, Class clz) throws Exception {
		return XmlUtil.xml2Obj(xmlData, clz);
	}

	protected <T> T build(String hostUrl, String signUrl, OkHttpClient client, Class reqBodyClz,
		Class respBodyClz) throws Exception {
		this.client = client;
		// 请求体
		T reqBody = buildReqBody();
		// 解析到签名服务接口
		String signature = parseAndSign(signUrl, reqBody, reqBodyClz);
		// 请求头
		RequestHeader requestHeader = buildReqestHeader();
		// 完整请求
		RequestPacket requestPacket = buildRequestPacket(requestHeader, signature);
		// 发送请求
		log.info("银企直连-请求接口： header = {}, request = {}", JSON.toJSONString(requestHeader),
			JSON.toJSONString(reqBody));
		ResponsePacket responsePacket = doExec(hostUrl, signUrl, requestPacket);
		// 解析到签名服务接口
		String unsign = unsign(signUrl, responsePacket);
		log.info("银企直连-接口响应： response = {}", unsign);
		// 提取响应结果
		return extractRespObj(unsign, respBodyClz);
	}

	private void checkRespCode(Response resp) {
		if (resp.code() != 200) {
			throw new WalletResponseException(EnumWalletResponseCode.PAY_IN_GATEWAY_REQUEST_ERROR);
		}
	}
}

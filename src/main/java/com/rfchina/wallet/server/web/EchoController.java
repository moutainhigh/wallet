package com.rfchina.wallet.server.web;

import com.rfchina.platform.common.misc.SymbolConstant;
import com.rfchina.platform.common.utils.DateUtil;
import io.github.xdiamond.client.annotation.OneKeyListener;
import io.github.xdiamond.client.event.ConfigEvent;
import java.io.File;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class EchoController {

	@Value(value = "${srv.base.home}")
	private String srvBaseHome;

	private final static Logger LOGGER = LoggerFactory.getLogger(EchoController.class);

	@RequestMapping(value = "index", produces = MediaType.TEXT_PLAIN_VALUE, method = RequestMethod.GET)
	public String index(HttpServletResponse response) throws Exception {

		String separator = srvBaseHome.endsWith(SymbolConstant.SYMBOL_SLASH) ? ""
			: SymbolConstant.SYMBOL_SLASH;

		if (new File(srvBaseHome + separator + "maintain.plf").exists()) {
			response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
				"system is under maintenance, date: " + DateUtil.formatDate(new Date()));
			return null;
		}

		return "Hello rfwallet-server ! today: " + DateUtil
			.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.S");
	}


	@OneKeyListener(key="srv.base.home")
	public void onPropertyChange(ConfigEvent event){
		srvBaseHome = event.getValue();
	}
}

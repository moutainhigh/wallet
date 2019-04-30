package com.rfchina.wallet.server.bank.pudong.domain.util;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * @author nzm
 */
public class XmlUtil {

	public static String extract(String xmlData, String prefix, String suffix) {
		return xmlData
			.substring(xmlData.indexOf(prefix) + prefix.length(), xmlData.indexOf(suffix));
	}

	public static String wrap(String xmlData) {
		xmlData = "<?xml version='1.0' encoding='gb2312'?>" + xmlData;
		int len = xmlData.getBytes().length + 6;

		String len_str = "";
		if (len < 100) {
			len_str = "0000" + len;
		} else if (len < 1000) {
			len_str = "000" + len;
		} else if (len < 10000) {
			len_str = "00" + len;
		}
		xmlData = len_str + xmlData;
		return xmlData;
	}

	public static String unwrap(String xmlData) {
		int start = xmlData.indexOf("<?xml");
		if (start >= 0) {
			start = xmlData.indexOf("?>", start) + 2;
		}
		return xmlData.substring(start);
	}

	public static String obj2Xml(Object body, Class clz) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(clz);
		Marshaller m = context.createMarshaller();
		StringWriter sw = new StringWriter();
		m.setProperty(Marshaller.JAXB_ENCODING, "gb2312");
		m.setProperty(Marshaller.JAXB_FRAGMENT, true);
		m.marshal(body, sw);
		return sw.toString();
	}

	public static <T> T xml2Obj(String data, Class clz) throws Exception {
		JAXBContext context = JAXBContext.newInstance(clz);
		Unmarshaller m = context.createUnmarshaller();
		return (T) m.unmarshal(new StringReader(data));
	}
}

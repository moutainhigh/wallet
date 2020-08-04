package com.rfchina.wallet.server.service.report;

import java.util.List;

public interface ReportBean {

	Class getReportClass();

	List getMethodArrays();

	String getFilePrefix();

}

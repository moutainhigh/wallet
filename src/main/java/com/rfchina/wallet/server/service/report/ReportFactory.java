package com.rfchina.wallet.server.service.report;

import com.rfchina.wallet.server.model.ext.VerifyDetailExcelVo;
import com.rfchina.wallet.server.msic.EnumWallet.ExportType;
import com.rfchina.wallet.server.msic.EnumYunst.YunstMethodName;
import java.util.Arrays;
import java.util.List;

public class ReportFactory {

	public static ReportBean createInstance(ExportType exportType) {

		if (ExportType.VERIFY.getValue().byteValue() == exportType.getValue()) {
			return new ReportBean() {

				public Class getReportClass() {
					return VerifyDetailExcelVo.class;
				}

				public List<String> getMethodArrays() {
					return Arrays.asList(
						YunstMethodName.COMPANY_VERIFY.getValue(),
						YunstMethodName.PERSON_VERIFY.getValue()
					);
				}

				public String getFilePrefix() {
					return "report/manager/";
				}
			};
		} else if (ExportType.ORDER.getValue().byteValue() == exportType.getValue()) {
			return new ReportBean() {

				public Class getReportClass() {
					return VerifyDetailExcelVo.class;
				}

				public List<String> getMethodArrays() {
					return Arrays.asList(
						YunstMethodName.COMPANY_VERIFY.getValue(),
						YunstMethodName.PERSON_VERIFY.getValue()
					);
				}

				public String getFilePrefix() {
					return "report/manager/";
				}
			};
		}

		return null;
	}

}

package com.devotion.blue.web.notify.sms;

import com.devotion.blue.model.query.OptionQuery;
import com.devotion.blue.utils.StringUtils;

public class SmsSenderFactory {


    public static ISmsSender createSender() {
		
		String provider = OptionQuery.me().findValue("sms_app_provider");
		
		if(StringUtils.isBlank(provider)){
			return new AlidayuSmsSender();
		}
		
		else if("sms_provider_alidayu".equals(provider)){
			return new AlidayuSmsSender();
		}
		
//		其他短信服务商
//		else if("sms_provider_xxx".equals(provider)){
//			return new XXXSmsSender();
//		}
		
		return new AlidayuSmsSender();

	}

}

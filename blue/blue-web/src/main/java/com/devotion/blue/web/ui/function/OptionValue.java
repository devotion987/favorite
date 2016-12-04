package com.devotion.blue.web.ui.function;

import com.devotion.blue.model.query.OptionQuery;
import com.devotion.blue.web.core.render.freemarker.JFunction;

public class OptionValue extends JFunction {

	@Override
    public Object onExec() {
		String key = getToString(0);
		return OptionQuery.me().findValue(key);
	}

}
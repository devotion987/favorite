package com.devotion.blue.web.ui.function;

import com.devotion.blue.model.query.OptionQuery;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.render.freemarker.JFunction;

public class OptionSelected extends JFunction {

    @Override
    public Object onExec() {
        String key = getToString(0);
        if (key == null)
            return "";

        String value = getToString(1);
        if (StringUtils.isNotBlank(value)) {
            String setting = OptionQuery.me().findValue(key);
            if (value.equals(setting)) {
                return "selected=\"selected\"";
            } else {
                return "";
            }
        }

        if (key.startsWith("!")) {
            Boolean bool = OptionQuery.me().findValueAsBool(key.substring(1));
            if (bool != null && !bool) {
                return "selected=\"selected\"";
            }
        } else {
            Boolean bool = OptionQuery.me().findValueAsBool(key);
            if (bool != null && bool) {
                return "selected=\"selected\"";
            }
        }

        return "";
    }


}

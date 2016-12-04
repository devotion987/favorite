package com.devotion.blue.web.ui.function;

import com.devotion.blue.model.core.JModel;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.render.freemarker.JFunction;

public class MetadataSelected extends JFunction {

    @Override
    public Object onExec() {
        Object obj = get(0);
        if (obj == null) {
            return "";
        }

        String key = getToString(1);
        if (StringUtils.isBlank(key)) {
            return "";
        }

        String value = getToString(2);
        if (value == null)
            value = "true";

        if (obj instanceof JModel<?>) {
            JModel<?> model = (JModel<?>) obj;
            String data = model.metadata(key);
            if (data != null && value.equals(data.toLowerCase())) {
                return "selected=\"selected\"";
            }
        }

        return "";
    }

}
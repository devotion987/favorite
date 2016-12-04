package com.devotion.blue.model.query;

import com.devotion.blue.model.Option;
import com.devotion.blue.utils.StringUtils;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;

public class OptionQuery extends JBaseQuery {

    protected static final Option DAO = new Option();
    private static final OptionQuery QUERY = new OptionQuery();

    public static OptionQuery me() {
        return QUERY;
    }

    public String findValue(final String key) {
//        String value = CacheKit.get(Option.CACHE_NAME, key, new IDataLoader() {
//            @Override
//            public Object load() {
//                Option option = DAO.doFindFirst("option_key =  ?", key);
//                if (null != option && option.getOptionValue() != null) {
//                    return option.getOptionValue();
//                }
//                return "";
//            }
//        });
        IDataLoader dataLoader = () -> {
            Option option = DAO.doFindFirst("option_key =  ?", key);
            if (null != option && option.getOptionValue() != null) {
                return option.getOptionValue();
            }
            return "";
        };
        String value = CacheKit.get(Option.CACHE_NAME, key, dataLoader);
        return "".equals(value) ? null : value;
    }


    public boolean saveOrUpdate(String key, String value) {
        Option option = DAO.doFindFirst("option_key =  ?", key);
        if (null == option) {
            option = new Option();
        }

        option.setOptionKey(key);
        option.setOptionValue(value);

        return option.saveOrUpdate();
    }

    public Option findByKey(String key) {
        return DAO.doFindFirst("option_key =  ?", key);
    }

    public Boolean findValueAsBool(String key) {
        String value = findValue(key);
        if (StringUtils.isNotBlank(value)) {
            try {
                return Boolean.parseBoolean(value);
            } catch (Exception e) {
                log.error("String convert to Boolean error:" + e);
            }
        }
        return null;
    }

    public Integer findValueAsInteger(String key) {
        String value = findValue(key);
        if (StringUtils.isNotBlank(value)) {
            try {
                return Integer.parseInt(value);
            } catch (Exception e) {
                log.error("String convert to Integer error:" + e);
            }
        }
        return null;
    }

    public Float findValueAsFloat(String key) {
        String value = findValue(key);
        if (StringUtils.isNotBlank(value)) {
            try {
                return Float.parseFloat(value);
            } catch (Exception e) {
                log.error("String convert to Float error:" + e);
            }
        }
        return null;
    }
}

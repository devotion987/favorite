package com.devotion.blue.model.query;

import com.devotion.blue.utils.StringUtils;
import com.jfinal.log.Log;

import java.math.BigInteger;
import java.util.List;

public class JBaseQuery {

    protected Log log = Log.getLog(getClass());

    protected static boolean appendWhereOrAnd(StringBuilder builder, boolean needWhere) {
        if (needWhere) {
            builder.append(" WHERE ");
        } else {
            builder.append(" AND ");
        }
        return false;
    }

    protected static boolean appendIfNotEmpty(StringBuilder builder, String colName, String value, List<Object> params,
                                              boolean needWhere) {
        if (value != null) {
            needWhere = appendWhereOrAnd(builder, needWhere);
            builder.append(" ").append(colName).append(" = ? ");
            params.add(value);
        }
        return needWhere;
    }

    protected static boolean appendIfNotEmpty(StringBuilder builder, String colName, BigInteger value,
                                              List<Object> params, boolean needWhere) {
        if (value != null) {
            needWhere = appendWhereOrAnd(builder, needWhere);
            builder.append(" ").append(colName).append(" = ? ");
            params.add(value);
        }
        return needWhere;
    }

    protected static boolean appendIfNotEmpty(StringBuilder builder, String colName, Object[] array,
                                              List<Object> params, boolean needWhere) {
        if (null != array && array.length > 0) {
            needWhere = appendWhereOrAnd(builder, needWhere);
            builder.append(" (");
            for (int i = 0; i < array.length; i++) {
                if (i == 0) {
                    builder.append(" ").append(colName).append(" = ? ");
                } else {
                    builder.append(" OR ").append(colName).append(" = ? ");
                }
                params.add(array[i]);
            }
            builder.append(" ) ");
        }
        return needWhere;
    }

    protected static boolean appendIfNotEmptyWithLike(StringBuilder builder, String colName, String value,
                                                      List<Object> params, boolean needWhere) {
        if (StringUtils.isNotBlank(value)) {
            needWhere = appendWhereOrAnd(builder, needWhere);
            builder.append(" ").append(colName).append(" like ? ");
            if (value.contains("%")) {
                params.add(value);
            } else {
                params.add("%" + value + "%");
            }

        }
        return needWhere;
    }

    protected static boolean appendIfNotEmptyWithLike(StringBuilder builder, String colName, String[] array,
                                                      List<Object> params, boolean needWhere) {
        if (null != array && array.length > 0) {
            needWhere = appendWhereOrAnd(builder, needWhere);
            builder.append(" (");
            for (int i = 0; i < array.length; i++) {
                if (i == 0) {
                    builder.append(" ").append(colName).append(" like ? ");
                } else {
                    builder.append(" OR ").append(colName).append(" like ? ");
                }
                String value = array[i];
                if (value.contains("%")) {
                    params.add(value);
                } else {
                    params.add("%" + value + "%");
                }
            }
            builder.append(" ) ");
        }
        return needWhere;
    }

}

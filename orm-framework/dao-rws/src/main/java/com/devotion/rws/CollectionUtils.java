package com.devotion.rws;

import java.util.Collections;
import java.util.List;

/**
 * 功能描述：基本工具类
 *
 * @author www.ibm.com
 */
public class CollectionUtils {

    /**
     * 降序排列Integer List
     *
     * @param list 参数列表
     * @return 排序后的列表
     */
    public static List<Integer> sortDesc(List<Integer> list) {
        Collections.sort(list, (o1, o2) -> {
            if (o1 <= o2)
                return 1;
            return 0;
        });
//        Collections.sort(list, new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                if (o1 <= o2) {
//                    return 1;
//                }
//                return 0;
//            }
//        });
        return list;
    }

    /**
     * 私有构造函数CollectionUtils
     */
    private CollectionUtils() {

    }

}

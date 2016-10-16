package com.devotion.rws.selector.support;

import java.util.List;
import java.util.Random;

import com.devotion.rws.schema.config.DsConfig;
import com.devotion.rws.selector.IDataSourceSelector;

/**
 * Random 基于权重随机算法的选择器
 */
public class DefaultDataSourceSelector implements IDataSourceSelector {

    /**
     * 随机数
     */
    private final Random random = new Random();

    /**
     * 数据源选择算法实现
     *
     * @param dsConfigs 数据源配置列表
     * @return DsConfigs 数据源对象
     */
    @Override
    public DsConfig select(List<DsConfig> dsConfigs) {

        /** 总个数 */
        int length = dsConfigs.size();
        /** 总权重 */
        int totalWeight = 0;
        /** 权重是否相同 */
        boolean sameWeight = true;

        for (int i = 0; i < length; i++) {
            int weight = dsConfigs.get(i).getWeight();
            /** 累计总权重 */
            totalWeight += weight;
            if (sameWeight && i > 0 && weight != dsConfigs.get(i - 1).getWeight()) {
                /** 计算所有权重是否相同 */
                sameWeight = false;
            }
        }
        if (totalWeight > 0 && !sameWeight) {
            /** 如果权重不相同且权重大于0则按总权重数随机 */
            int offset = random.nextInt(totalWeight);
            /** 并确定随机值落在哪个片断上 */
            for (DsConfig dsConfig : dsConfigs) {
                offset -= dsConfig.getWeight();
                if (offset < 0) {
                    return dsConfig;
                }
            }
        }
        /** 如果权重相同或权重为0则均等随机 */
        return dsConfigs.get(random.nextInt(length));
    }

}

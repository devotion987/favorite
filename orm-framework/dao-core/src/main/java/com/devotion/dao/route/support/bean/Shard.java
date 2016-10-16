package com.devotion.dao.route.support.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 分区Shard<br>
 */
@XStreamAlias("shard")
public class Shard {

    /**
     * 数据源分区名称
     */
    @XStreamAsAttribute
    private String name;

    /**
     * 哈希串
     */
    @XStreamAsAttribute
    private String hash;

    /**
     * 数据源绑定信息
     */
    @XStreamAlias("bean")
    @XStreamAsAttribute
    private String dataSourceRef;

    /**
     * 判断分区中是否包含所传的参数值
     *
     * @param shardHash  分区哈希值
     * @param paramValue 待判断值
     * @return 结果
     */
    public boolean contains(int shardHash, long paramValue) {
        String hashValue = String.valueOf(paramValue % shardHash);
        if (hash.indexOf(',') == -1 && hash.equalsIgnoreCase(hashValue)) {
            return true;
        }
        String[] hashFragment = hash.split(",");
        for (String fragment : hashFragment) {
            if (fragment.equalsIgnoreCase(hashValue)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 功能描述: <br>
     * 〈功能详细描述〉 分片名称
     *
     * @return 分片名称
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public String getName() {
        return name;
    }

    /**
     * 功能描述: <br>
     * 〈功能详细描述〉 设置分片名称
     *
     * @param name 分片名称
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 功能描述: <br>
     * 〈功能详细描述〉 获取hash值
     *
     * @return hash值
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public String getHash() {
        return hash;
    }

    /**
     * 功能描述: <br>
     * 〈功能详细描述〉 设置hash值
     *
     * @param hash 设置hash值
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * 功能描述: <br>
     * 〈功能详细描述〉 获取数据源引用
     *
     * @return 数据源引用
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public String getDataSourceRef() {
        return dataSourceRef;
    }

    /**
     * 功能描述: <br>
     * 〈功能详细描述〉 设置数据源引用
     *
     * @param dataSourceRef 数据源引用
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public void setDataSourceRef(String dataSourceRef) {
        this.dataSourceRef = dataSourceRef;
    }
}

package com.devotion.dao.route.support.bean;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 虚拟节点的定位实现
 */
public class VirtualNodeLocator {

    /**
     * 每组节点数
     */
    private static final int NUMBERS = 160;

    /**
     * 每组节点数
     */
    private static final int NUM0 = 0;

    /**
     * 每组节点数
     */
    private static final int NUM1 = 1;

    /**
     * 每组节点数
     */
    private static final int NUM2 = 2;

    /**
     * 每组节点数
     */
    private static final int NUM3 = 3;

    /**
     * 每组节点数
     */
    private static final int NUM4 = 4;

    /**
     * 每组节点数
     */
    private static final int NUM8 = 8;

    /**
     * 每组节点数
     */
    private static final int NUM16 = 16;

    /**
     * 每组节点数
     */
    private static final int NUM24 = 24;

    /**
     * 每组节点数
     */
    private static final int NUM_0XFF = 0xFF; // 255

    /**
     * 节点treeMap
     */
    private TreeMap<Long, EntryNode> treeMap = new TreeMap<>();

    /**
     * 虚拟节点的定位
     *
     * @param nodeMapping 节点匹配
     */
    public VirtualNodeLocator(Map<String, DataSource> nodeMapping) {
        if (nodeMapping != null) {
            treeMap.putAll(distributeNode(nodeMapping));
        }
    }

    /**
     * 虚拟节点分发
     *
     * @param nodeMapping 节点映射集合
     * @return 节点treeMap
     */
    private TreeMap<Long, EntryNode> distributeNode(Map<String, DataSource> nodeMapping) {
        if (nodeMapping != null && !nodeMapping.isEmpty()) {
            /** 节点装入迭代器 */
            nodeMapping.entrySet().forEach((Entry<String, DataSource> entry) -> {
                /** 调用KETMATA算法计算一致性哈希路由 */
                for (int i = 0; i < NUMBERS / NUM4; i++) {
                    byte[] digest = computeMd5(entry.getKey() + i);
                    for (int h = 0; h < NUM4; h++) {
                        long m = HashAlgorithm.KETAMA_HASH.hash(digest, h);
                        treeMap.put(m, new EntryNode(entry.getKey(), entry.getValue()));
                    }
                }
            });
//            Iterator<Entry<String, DataSource>> it = nodeMapping.entrySet().iterator();
//            while (it.hasNext()) {
//                Entry<String, DataSource> entry = it.next();
//                /** 调用KETMATA算法计算一致性哈希路由 */
//                for (int i = 0; i < NUMBERS / NUM4; i++) {
//                    byte[] digest = computeMd5(entry.getKey() + i);
//                    for (int h = 0; h < NUM4; h++) {
//                        long m = HashAlgorithm.KETAMA_HASH.hash(digest, h);
//                        treeMap.put(m, new EntryNode(entry.getKey(), entry.getValue()));
//                    }
//                }
//            }
        }
        return treeMap;
    }

    /**
     * 调用KETAMA_HASH算法生成哈希串
     *
     * @param k 哈希键值
     * @return EntryNode
     */
    public EntryNode getPrimary(final String k) {
        return getNodeForKey(HashAlgorithm.KETAMA_HASH.hash(computeMd5(k), 0));
    }

    /**
     * 根据传入哈希值获取结点
     *
     * @param hash 哈希值
     * @return 节点
     */
    public EntryNode getNodeForKey(long hash) {
        long hashTemp = hash;
        if (!treeMap.containsKey(hash)) {
            SortedMap<Long, EntryNode> tailMap = treeMap.tailMap(hashTemp);
            if (tailMap.isEmpty()) {
                hashTemp = treeMap.firstKey();
            } else {
                hashTemp = tailMap.firstKey();
            }
        }
        return treeMap.get(hashTemp);
    }

    /**
     * Get the md5 of the given key.
     *
     * @param k 哈希键值
     * @return MD5加密数组
     */
    private byte[] computeMd5(String k) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(k.getBytes("UTF-8"));
            return md5.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unknown string :" + k, e);
        }
    }

    /**
     * KetemaHash算法实现<br>
     */
    private enum HashAlgorithm {

        /**
         * MD5-based hash algorithm used by ketama.
         */
        KETAMA_HASH;

        /**
         * KETAMA_HASH算法实现
         *
         * @param digest MD5串
         * @param nTime  每组节点数
         * @return 虚拟节点在环中的唯一KEY
         */
        public long hash(byte[] digest, int nTime) {
            long rv = ((long) (digest[NUM3 + nTime * NUM4] & NUM_0XFF) << NUM24)
                    | ((long) (digest[NUM2 + nTime * NUM4] & NUM_0XFF) << NUM16)
                    | ((long) (digest[NUM1 + nTime * NUM4] & NUM_0XFF) << NUM8)
                    | (digest[NUM0 + nTime * NUM4] & NUM_0XFF);

            return rv & 0xffffffffL; /* Truncate to 32-bits */
        }
    }

}

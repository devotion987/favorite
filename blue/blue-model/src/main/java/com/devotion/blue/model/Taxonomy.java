package com.devotion.blue.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.devotion.blue.model.base.BaseTaxonomy;
import com.devotion.blue.model.core.Table;
import com.devotion.blue.model.query.MappingQuery;
import com.devotion.blue.model.route.TaxonomyRouter;
import com.devotion.blue.utils.StringUtils;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;

@Table(tableName = "taxonomy", primaryKey = "id")
public class Taxonomy extends BaseTaxonomy<Taxonomy> implements ModelSorter.ISortModel<Taxonomy> {

    private static final long serialVersionUID = 1L;

    public static final String TYPE_TAG = "tag"; // tag
    public static final String TYPE_SPECIAL = "special"; // 专题
    public static final String TYPE_CATEGORY = "category"; // 分类

    private int layer = 0;
    private List<Taxonomy> childList;
    private List<Taxonomy> filterList;
    private Taxonomy parent;
    private String activeClass;

    public <T> T getFromListCache(Object key, IDataLoader dataloader) {
        Set<String> inCacheKeys = CacheKit.get(CACHE_NAME, "cachekeys");

        Set<String> cacheKeyList = new HashSet<String>();
        if (inCacheKeys != null) {
            cacheKeyList.addAll(inCacheKeys);
        }

        cacheKeyList.add(key.toString());
        CacheKit.put(CACHE_NAME, "cachekeys", cacheKeyList);

        return CacheKit.get("taxonomy_list", key, dataloader);
    }

    public void clearList() {
        Set<String> list = CacheKit.get(CACHE_NAME, "cachekeys");
        if (list != null && list.size() > 0) {
            for (String key : list) {
                if (!key.startsWith("module:")) {
                    CacheKit.remove("taxonomy_list", key);
                    continue;
                }

                // 不清除其他模型的内容
                if (key.startsWith("module:" + getContentModule())) {
                    CacheKit.remove("taxonomy_list", key);
                }
            }
        }
    }

    public int getLayer() {
        return layer;
    }

    @Override
    public void setLayer(int tier) {
        this.layer = tier;
    }

    public String getLayerString() {
        String layerString = "";
        for (int i = 0; i < layer; i++) {
            layerString += "— ";
        }
        return layerString;
    }

    public List<Taxonomy> getChildList() {
        return childList;
    }

    public void setChildList(List<Taxonomy> childList) {
        this.childList = childList;
    }

    @Override
    public void addChild(Taxonomy child) {
        if (null == childList) {
            childList = new ArrayList<Taxonomy>();
        }

        //如果是从ehcache内存取到的数据，可能该model已经添加过了
        if (!childList.contains(child)) {
            childList.add(child);
        }
    }

    public List<Taxonomy> getFilterList() {
        return filterList;
    }

    public void initFilterList(List<Taxonomy> list, String activeClass) {
        this.filterList = new ArrayList<Taxonomy>();
        this.filterList.addAll(list);

        this.activeClass = activeClass;
    }

    @Override
    public Taxonomy getParent() {
        return parent;
    }

    @Override
    public void setParent(Taxonomy parent) {
        this.parent = parent;
    }

    public void updateContentCount() {
        long count = MappingQuery.me().findCountByTaxonomyId(getId(), Content.STATUS_NORMAL);
        if (count > 0) {
            setContentCount(count);
            this.update();
        }
    }

    public long findContentCount() {
        Long count = MappingQuery.me().findCountByTaxonomyId(getId());
        return count == null ? 0 : count;
    }

    public String getUrl() {
        return JFinal.me().getContextPath() + TaxonomyRouter.getRouter(this);
    }

    public String getFilterUrl() {
        if (filterList == null || filterList.isEmpty()) {
            return getUrl();
        }

        List<Taxonomy> list = new ArrayList<Taxonomy>();
        for (Taxonomy taxonomy : filterList) {
            if (!taxonomy.getType().equals(getType())) {
                list.add(taxonomy);
            }
        }
        list.add(this);
        return JFinal.me().getContextPath() + TaxonomyRouter.getRouter(list);
    }

    public boolean isActive() {
        return filterList != null && filterList.contains(this);
    }

    public String getActiveClass() {
        if (!isActive())
            return null;
        return activeClass;
    }

    public void setActiveClass(String activeClass) {
        this.activeClass = activeClass;
    }

    public String getSelectUrl() {
        if (filterList == null || filterList.isEmpty()) {
            return getUrl();
        }

        List<Taxonomy> list = new ArrayList<Taxonomy>();
        list.addAll(filterList);
        if (!list.contains(this)) {
            list.add(this);
        } else {
            list.remove(this);
        }

        if (list.isEmpty()) {
            return JFinal.me().getContextPath() + TaxonomyRouter.getRouter(getContentModule());
        }

        return JFinal.me().getContextPath() + TaxonomyRouter.getRouter(list);
    }

    @Override
    public boolean save() {
        if (getId() != null) {
            removeCache(getId());
            putCache(getId(), this);
        }

        clearList();

        return super.save();
    }

    public boolean update() {
        if (getId() != null) {
            removeCache(getId());
            removeCache(this.getContentModule() + ":" + this.getSlug());
        }

        clearList();

        return super.update();
    }

    @Override
    public boolean delete() {
        removeCache(getId());
        removeCache(this.getContentModule() + ":" + this.getSlug());

        clearList();

        return super.delete();
    }

    @Override
    public void setSlug(String slug) {
        if (StringUtils.isNotBlank(slug)) {
            slug = slug.trim();
            if (StringUtils.isNumeric(slug)) {
                slug = "t" + slug; // slug不能为全是数字,随便添加一个字母，t代表taxonomy好了
            } else {
                slug = slug.replaceAll("\\pP|\\pS|(\\s+)|[\\$,。\\.…，_？\\-?、；;:!]", "");
            }
        }
        super.setSlug(slug);
    }

}

package com.devotion.blue.web.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;

import com.devotion.blue.model.core.JModel;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Page;

public class JBaseCRUDController<M extends JModel<? extends JModel<?>>> extends JBaseController {

    private static final Log logger = Log.getLog(JBaseCRUDController.class);
    private final Class<M> mClazz;
//	protected final M mDao;

    @SuppressWarnings("unchecked")
    public JBaseCRUDController() {
        ParameterizedType type = getParameterizedType(getClass());
        mClazz = (Class<M>) type.getActualTypeArguments()[0];
//		mDao = getDao();
    }

    private ParameterizedType getParameterizedType(Class<?> clazz) {
        if (clazz == Object.class) {
            logger.error("get ParameterizedType error in _BaseController.class");
            return null;
        }
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            return (ParameterizedType) genericSuperclass;
        } else {
            return getParameterizedType(clazz.getSuperclass());
        }
    }

    @SuppressWarnings("unchecked")
    public void index() {
        Page<M> page = onIndexDataLoad(getPageNumber(), getPageSize());
        if (null == page) {
            page = (Page<M>) getDao().doPaginate(getPageNumber(), getPageSize());
        }
        setAttr("page", page);
        render("index.html");
    }

    public void edit() {
        BigInteger id = getParaToBigInteger("id");
        if (id != null) {
            setAttr(StrKit.firstCharToLowerCase(mClazz.getSimpleName()), getDao().findById(id));
        }
        render("edit.html");
    }

    public void save() {
        if (isMultipartRequest()) {
            getFile();
        }

        M m = getModel(mClazz);

        if (!onModelSaveBefore(m)) {
            renderAjaxResultForError();
            return;
        }
        m.saveOrUpdate();

        if (!onModelSaveAfter(m)) {
            renderAjaxResultForError();
            return;
        }

        renderAjaxResultForSuccess("ok");
    }

    public void delete() {
        BigInteger id = getParaToBigInteger("id");
        if (id != null) {
            getDao().deleteById(id);
            renderAjaxResultForSuccess("删除成功");
        } else {
            renderAjaxResultForError();
        }
    }

    private M getDao() {
        M m = null;
        try {
            m = mClazz.newInstance();
        } catch (Exception e) {
            logger.error("get DAO error.", e);
        }
        return m;
    }

    public Page<M> onIndexDataLoad(int pageNumber, int pageSize) {
        return null;
    }

    public boolean onModelSaveBefore(M m) {
        // do nothing
        return true;
    }

    public boolean onModelSaveAfter(M m) {
        // do nothing
        return true;
    }

}

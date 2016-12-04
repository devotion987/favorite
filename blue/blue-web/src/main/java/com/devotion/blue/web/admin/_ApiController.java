package com.devotion.blue.web.admin;

import com.devotion.blue.model.Content;
import com.devotion.blue.model.query.ContentQuery;
import com.devotion.blue.model.query.OptionQuery;
import com.devotion.blue.utils.Consts;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.JBaseController;
import com.devotion.blue.web.core.interceptor.ActionCacheClearInterceptor;
import com.devotion.blue.web.interceptor.UCodeInterceptor;
import com.devotion.blue.web.router.RouterMapping;
import com.devotion.blue.web.router.RouterNotAllowConvert;
import com.jfinal.aop.Before;

import java.math.BigInteger;
import java.util.List;

@RouterMapping(url = "/admin/api")
@Before(ActionCacheClearInterceptor.class)
@RouterNotAllowConvert
public class _ApiController extends JBaseController {

	public void index() {
        BigInteger id = getParaToBigInteger("id");
		if (null != id) {
			setAttr("content", ContentQuery.me().findById(id));
		}
		List<Content> contents = ContentQuery.me().findByModule(Consts.MODULE_API_APPLICATION);
		setAttr("contents", contents);
		render("/WEB-INF/admin/option/api.html");
	}

	public void save() {

		Boolean apiEnable = getParaToBoolean("api_enable", Boolean.FALSE);
		OptionQuery.me().saveOrUpdate("api_enable", apiEnable.toString());
		
		Boolean apiCorsEnable = getParaToBoolean("api_cors_enable", Boolean.FALSE);
		OptionQuery.me().saveOrUpdate("api_cors_enable", apiCorsEnable.toString());

		Content c = getModel(Content.class);
		c.setModule(Consts.MODULE_API_APPLICATION);
		
		if(StringUtils.areNotBlank(c.getTitle(),c.getText(),c.getFlag())){
			c.saveOrUpdate();
		}
			
		renderAjaxResultForSuccess();
	}

	@Before(UCodeInterceptor.class)
	public void delete() {
		BigInteger id = getParaToBigInteger("id");
		if (id != null) {
			ContentQuery.me().deleteById(id);
			renderAjaxResultForSuccess("删除成功");
		} else {
			renderAjaxResultForError();
		}
	}
}
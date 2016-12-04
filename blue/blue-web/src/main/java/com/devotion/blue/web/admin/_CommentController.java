package com.devotion.blue.web.admin;

import com.devotion.blue.model.Comment;
import com.devotion.blue.model.Content;
import com.devotion.blue.model.User;
import com.devotion.blue.model.query.CommentQuery;
import com.devotion.blue.model.query.UserQuery;
import com.devotion.blue.model.template.TemplateManager;
import com.devotion.blue.utils.JsoupUtils;
import com.devotion.blue.utils.StringUtils;
import com.devotion.blue.web.core.JBaseCRUDController;
import com.devotion.blue.web.core.interceptor.ActionCacheClearInterceptor;
import com.devotion.blue.web.interceptor.UCodeInterceptor;
import com.devotion.blue.web.router.RouterMapping;
import com.devotion.blue.web.router.RouterNotAllowConvert;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;

import java.math.BigInteger;
import java.util.Date;

@RouterMapping(url = "/admin/comment", viewPath = "/WEB-INF/admin/comment")
@Before(ActionCacheClearInterceptor.class)
@RouterNotAllowConvert
public class _CommentController extends JBaseCRUDController<Comment> {

	private String getModule() {
        return getPara("m");
	}

	private String getType() {
		return getPara("t");
	}

	@Override
	public void index() {

		keepPara();

		setAttr("module", TemplateManager.me().currentTemplateModule(getModule()));
		setAttr("delete_count", CommentQuery.me().findCountByModuleAndStatus(getModule(), Comment.STATUS_DELETE));
		setAttr("draft_count", CommentQuery.me().findCountByModuleAndStatus(getModule(), Comment.STATUS_DRAFT));
		setAttr("normal_count", CommentQuery.me().findCountByModuleAndStatus(getModule(), Comment.STATUS_NORMAL));
		setAttr("count", CommentQuery.me().findCountInNormalByModule(getModule()));

		super.index();
	}

	@Override
	public Page<Comment> onIndexDataLoad(int pageNumber, int pageSize) {

		BigInteger contentId = getParaToBigInteger("cid");
		BigInteger parentCommentId = getParaToBigInteger("pid");

		if (StringUtils.isNotBlank(getPara("s"))) {
			return CommentQuery.me().paginateWithContent(pageNumber, pageSize, getModule(), getType(), contentId,
					parentCommentId, getPara("s"));
		}
		return CommentQuery.me().paginateWithContentNotInDelete(pageNumber, pageSize, getModule(), getType(), contentId,
				parentCommentId);
	}

	@Override
	public void edit() {
		BigInteger id = getParaToBigInteger("id");
		Comment comment = CommentQuery.me().findById(id);
		setAttr("comment", comment);
	}

	@Before(UCodeInterceptor.class)
	public void trash() {
		Comment c = CommentQuery.me().findById(getParaToBigInteger("id"));
		if (c != null) {
			c.setStatus(Comment.STATUS_DELETE);
			if (c.saveOrUpdate()) {
				renderAjaxResultForSuccess();
			} else {
				renderAjaxResultForError("restore error!");
			}
		} else {
			renderAjaxResultForError("trash error!");
		}
	}

	@Before(UCodeInterceptor.class)
	public void restore() {
		BigInteger id = getParaToBigInteger("id");
		Comment c = CommentQuery.me().findById(id);
		if (c != null && c.isDelete()) {
			c.setStatus(Content.STATUS_DRAFT);
			if (c.saveOrUpdate()) {
				renderAjaxResultForSuccess("success");
			} else {
				renderAjaxResultForError("restore error!");
			}
		} else {
			renderAjaxResultForError("restore error!");
		}
	}

	@Before(UCodeInterceptor.class)
	public void pub() {
		BigInteger id = getParaToBigInteger("id");
		Comment c = CommentQuery.me().findById(id);
		if (c != null) {
			c.setStatus(Content.STATUS_NORMAL);
			if (c.saveOrUpdate()) {
				renderAjaxResultForSuccess("success");
			} else {
				renderAjaxResultForError("pub fail!");
			}
		} else {
			renderAjaxResultForError("pub error!");
		}
	}

	@Before(UCodeInterceptor.class)
	public void draft() {
		BigInteger id = getParaToBigInteger("id");
		Comment c = CommentQuery.me().findById(id);
		if (c != null) {
			c.setStatus(Content.STATUS_DRAFT);
			if (c.saveOrUpdate()) {
				renderAjaxResultForSuccess("success");
			} else {
				renderAjaxResultForError("draft fail!");
			}
		} else {
			renderAjaxResultForError("draft error!");
		}
	}

	@Before(UCodeInterceptor.class)
	public void delete() {
		BigInteger id = getParaToBigInteger("id");
		final Comment c = CommentQuery.me().findById(id);
		if (c != null) {
			if (c.delete()) {
				renderAjaxResultForSuccess();
				return;
			}
		}
		renderAjaxResultForError();
	}

	@Override
	public void save() {
		Comment comment = getModel(Comment.class);
		String username = getPara("username");
		if (StringUtils.isNotBlank(username)) {
			User user = UserQuery.me().findUserByUsername(username);
			if (user == null) {
				renderAjaxResultForError("系统没有该用户：" + username);
				return;
			}
			comment.setUserId(user.getId());
		}
		if (comment.saveOrUpdate()) {
			comment.updateCommentCount();
			renderAjaxResultForSuccess();
		} else {
			renderAjaxResultForError();
		}
	}

	public void reply_layer() {
		BigInteger id = getParaToBigInteger("id");
		setAttr("comment", CommentQuery.me().findById(id));
	}

	public void reply() {
		Comment comment = getModel(Comment.class);

		comment.setType(Comment.TYPE_COMMENT);
		comment.setIp(getIPAddress());
		comment.setAgent(getUserAgent());
		User user = getLoginedUser();
		String author = StringUtils.isNotBlank(user.getNickname()) ? user.getNickname() : user.getUsername();
		comment.setAuthor(author);
		comment.setEmail(user.getEmail());
		comment.setStatus(Comment.STATUS_NORMAL);
		comment.setUserId(user.getId());
		comment.setCreated(new Date());

		comment.setText(JsoupUtils.getBodyHtml(comment.getText()));

		comment.save();
		renderAjaxResultForSuccess();
	}
}
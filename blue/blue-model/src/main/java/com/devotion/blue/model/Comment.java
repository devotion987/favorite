package com.devotion.blue.model;

import com.devotion.blue.model.base.BaseComment;
import com.devotion.blue.model.core.Table;
import com.devotion.blue.model.query.CommentQuery;
import com.devotion.blue.model.query.ContentQuery;
import com.devotion.blue.model.query.UserQuery;

import java.math.BigInteger;

@Table(tableName = "comment", primaryKey = "id")
public class Comment extends BaseComment<Comment> {

    public static final String TYPE_COMMENT = "comment";

    public static String STATUS_DELETE = "delete";
    public static String STATUS_DRAFT = "draft";
    public static String STATUS_NORMAL = "normal";

    private Content content;
    private User user;
    private Comment parent;

    public Content getContent() {
        if (content != null) {
            return content;
        }

        if (getContentId() != null) {
            content = ContentQuery.me().findById(getContentId());
        }

        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public User getUser() {
        if (user != null) {
            return user;
        }

        if (getUserId() != null) {
            user = UserQuery.me().findById(getUserId());
        }

        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment getParent() {
        if (parent != null) {
            return parent;
        }

        if (getContentId() != null) {
            parent = CommentQuery.me().findById(getParentId());
        }

        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public boolean isDelete() {
        return STATUS_DELETE.equals(getStatus());
    }

    public String getContentUrl() {
        BigInteger contentId = getContentId();
        if (contentId == null)
            return null;

        Content c = ContentQuery.me().findById(contentId);
        return c == null ? null : c.getUrl();
    }

    public boolean updateCommentCount() {
        long count = CommentQuery.me().findCountByParentIdInNormal(getId());
        if (count > 0) {
            setCommentCount(count);
            return this.update();
        }
        return false;
    }

    @Override
    public boolean update() {
        removeCache(getId());
        removeCache(getSlug());

        return super.update();
    }

    @Override
    public boolean delete() {
        removeCache(getId());
        removeCache(getSlug());

        return super.delete();
    }

    @Override
    public boolean save() {
        removeCache(getId());
        removeCache(getSlug());

        return super.save();
    }
}

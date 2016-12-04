package com.devotion.blue.model.query;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import com.devotion.blue.model.Metadata;
import com.devotion.blue.model.User;
import com.devotion.blue.model.template.TemplateManager;
import com.devotion.blue.model.template.TplModule;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.IDataLoader;

public class UserQuery extends JBaseQuery {

    protected static final User DAO = new User();
    private static final UserQuery QUERY = new UserQuery();

    public static UserQuery me() {
        return QUERY;
    }

    public List<User> findList(int page, int pagesize, String gender, String role, String status, String orderBy) {
        StringBuilder sqlBuilder = new StringBuilder("select * from user u ");
        LinkedList<Object> params = new LinkedList<>();

        boolean needWhere = true;
        needWhere = appendIfNotEmpty(sqlBuilder, "u.gender", gender, params, needWhere);
        needWhere = appendIfNotEmpty(sqlBuilder, "u.role", role, params, needWhere);
        needWhere = appendIfNotEmpty(sqlBuilder, "u.status", status, params, needWhere);

        buildOrderBy(orderBy, sqlBuilder);

        sqlBuilder.append(" LIMIT ?, ?");
        params.add(page - 1);
        params.add(pagesize);

        if (params.isEmpty()) {
            return DAO.find(sqlBuilder.toString());
        } else {
            return DAO.find(sqlBuilder.toString(), params.toArray());
        }

    }

    public User findFirstFromMetadata(String key, Object value) {
//		return DAO.findFirstFromMetadata(key, value);
        Metadata md = MetaDataQuery.me().findFirstByTypeAndValue(User.METADATA_TYPE, key, value);
        if (md != null) {
            BigInteger id = md.getObjectId();
            return findById(id);
        }
        return null;
    }

    public Page<User> paginate(int pageNumber, int pageSize, String orderby) {
        String select = "select * ";
        StringBuilder fromBuilder = new StringBuilder(" from user u ");
        buildOrderBy(orderby, fromBuilder);
        return DAO.paginate(pageNumber, pageSize, select, fromBuilder.toString());
    }

    public long findCount() {
        return DAO.doFindCount();
    }

    public long findAdminCount() {
        return DAO.doFindCount(" role = ? ", "administrator");
    }

    public User findById(final BigInteger userId) {
        return DAO.getCache(userId, new IDataLoader() {
            @Override
            public Object load() {
                return DAO.findById(userId);
            }
        });
    }

    public User findUserByEmail(final String email) {
        return DAO.getCache(email, new IDataLoader() {
            @Override
            public Object load() {
                return DAO.doFindFirst("email = ?", email);
            }
        });
    }

    public User findUserByUsername(final String username) {
        return DAO.getCache(username, new IDataLoader() {
            @Override
            public Object load() {
                return DAO.doFindFirst("username = ?", username);
            }
        });
    }

    public User findUserByMobile(final String mobile) {
        return DAO.getCache(mobile, new IDataLoader() {
            @Override
            public Object load() {
                return DAO.doFindFirst("mobile = ?", mobile);
            }
        });
    }

    public boolean updateContentCount(User user) {
        long count = 0;
        List<TplModule> modules = TemplateManager.me().currentTemplateModules();
        if (modules != null && !modules.isEmpty()) {
            for (TplModule m : modules) {
                long moduleCount = ContentQuery.me().findCountInNormalByModuleAndUserId(m.getName(), user.getId());
                count += moduleCount;
            }
        }

        user.setContentCount(count);
        return user.update();
    }

    public boolean updateCommentCount(User user) {
        long count = CommentQuery.me().findCountByUserIdInNormal(user.getId());
        user.setCommentCount(count);
        return user.update();
    }

    protected static void buildOrderBy(String orderBy, StringBuilder fromBuilder) {
        if ("content_count".equals(orderBy)) {
            fromBuilder.append(" ORDER BY u.content_count DESC");
        } else if ("comment_count".equals(orderBy)) {
            fromBuilder.append(" ORDER BY u.comment_count DESC");
        } else if ("username".equals(orderBy)) {
            fromBuilder.append(" ORDER BY u.username DESC");
        } else if ("nickname".equals(orderBy)) {
            fromBuilder.append(" ORDER BY u.nickname DESC");
        } else if ("amount".equals(orderBy)) {
            fromBuilder.append(" ORDER BY u.amount DESC");
        } else if ("logged".equals(orderBy)) {
            fromBuilder.append(" ORDER BY u.logged DESC");
        } else if ("activated".equals(orderBy)) {
            fromBuilder.append(" ORDER BY u.activated DESC");
        } else {
            fromBuilder.append(" ORDER BY u.created DESC");
        }
    }

}

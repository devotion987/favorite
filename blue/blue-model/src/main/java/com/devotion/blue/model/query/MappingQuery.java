package com.devotion.blue.model.query;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

import com.devotion.blue.model.Mapping;
import com.devotion.blue.model.core.Jdb;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

public class MappingQuery extends JBaseQuery {

    protected static final Mapping DAO = new Mapping();
    private static final MappingQuery QUERY = new MappingQuery();

    public static MappingQuery me() {
        return QUERY;
    }

    public int doDelByContentId(BigInteger contentId) {
        return DAO.doDelete("content_id = ?", contentId);
    }

    public boolean doBatchUpdate(final BigInteger contentId, final BigInteger[] taxonomyIds) {
        return Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                doDelByContentId(contentId);
                for (BigInteger taxonomyid : taxonomyIds) {
                    Mapping mapping = new Mapping();
                    mapping.setContentId(contentId);
                    mapping.setTaxonomyId(taxonomyid);
                    if (!mapping.save()) {
                        return false;
                    }
                }
                return true;
            }
        });
    }

    public List<Mapping> findListByContentId(BigInteger contentId) {
        return DAO.doFindByCache(Mapping.CACHE_NAME, Mapping.buildKeyByContentId(contentId), " content_id = ?",
                contentId);
    }

    public void deleteByContentId(BigInteger id) {
        Jdb.update("DELETE FROM mapping WHERE content_id = ?", id);
    }

    public void deleteByTaxonomyId(BigInteger id) {
        Jdb.update("DELETE FROM mapping WHERE taxonomy_id = ? ", id);
    }

    public long findCountByTaxonomyId(BigInteger id) {
        return DAO.doFindCount("taxonomy_id = ?", id);
    }

    public long findCountByTaxonomyId(BigInteger id, String contentStatus) {
        String sql = "SELECT COUNT(*) FROM mapping m ";
        sql += "left join content c ON m.content_id=c.id ";
        sql += "where m.taxonomy_id = ? and c.status = ?";
        return Jdb.queryLong(sql, id, contentStatus);
    }

    public long findCountByContentId(BigInteger id) {
        return DAO.doFindCount("content_id = ?", id);
    }
}

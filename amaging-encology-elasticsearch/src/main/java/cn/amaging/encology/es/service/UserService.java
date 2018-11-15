package cn.amaging.encology.es.service;

import cn.amaging.encology.es.dao.UserDao;
import cn.amaging.encology.es.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by DuQiyu on 2018/11/14 15:12.
 */
public class UserService extends AbstractService<User> {

    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserDao userDao;

    public Iterable<User> fullSearch(Map<String, String> params) {
        if (null == params) {
            params = new HashMap<>();
        }
        return userDao.search(super.fullQuery(params).build());
    }

    public Iterable<User> conditionSearch(Map<String, String> params) {
        if (null == params) {
            params = new HashMap<>();
        }
        return userDao.search(super.conditionQuery(params).build());
    }

    public Iterable<User> uniqueSearch(String uid) {
        if (StringUtils.isEmpty(uid)) {
            return null;
        }
        return userDao.search(super.detailQuery(uid).build());
    }
}

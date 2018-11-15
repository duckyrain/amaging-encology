package cn.amaging.encology.es.dao;

import cn.amaging.encology.es.model.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by DuQiyu on 2018/6/19.
         */
public interface UserDao extends ElasticsearchRepository<User, Integer> {

}

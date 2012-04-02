package ynu.dao;

import org.springframework.stereotype.Repository;

import ynu.domain.LoginLog;
import ynu.domain.Post;

/**
 * Post的DAO类
 *
 */
@Repository
public class LoginLogDao extends BaseDao<LoginLog> {
	public void save(LoginLog loginLog) {
		this.getHibernateTemplate().save(loginLog);
	}

}

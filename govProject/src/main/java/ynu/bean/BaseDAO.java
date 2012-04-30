/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ynu.bean;

import java.util.List;
import ynu.model.User;
import ynu.model.Page;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author LaiWenGen
 */
@Repository
public class BaseDAO {

        @PersistenceContext
        private EntityManager entityManager;
        
        @Transactional
        public User save(User user) {
                if (user.getId() == null) {
                        entityManager.persist(user);
                        return user;
                } else {
                        return entityManager.merge(user);
                }		
        }

	public User list(Long id) {
		return entityManager.find(User.class, id);
	}
        
        public List<User> find(String jpql) {
                return  entityManager.createQuery(jpql).getResultList();
        }
        
        public void update(User user) {
                entityManager.refresh(user);
        }
        public void delete(User user) {
                entityManager.detach(user);
        }
        
        //for page
        //get a page given pagenNo and pageSize
        public Page pagedQuery(String jpql, int pageNo, int pageSize) {
            List userlist = find(jpql);
            long totalCount = (Long) userlist.get(0);
            
            if(totalCount <1)
                return new Page();
            int startIndex = Page.getStartOfPage(pageNo, pageSize);
            List list = entityManager.createQuery(jpql).setFirstResult(startIndex).setMaxResults(pageSize).getResultList();
            return new Page(startIndex, totalCount, pageSize, list);
        }



    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.UserDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.User;
import com.cherrot.govproject.model.Usermeta;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cherrot
 */
@Repository
public class UserJpaDao implements Serializable, UserDao {

    @PersistenceContext
    private EntityManager em;
//    public UserJpaDao(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    @Override
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    @Override
    @Transactional
    public void create(User user) {
        if (user.getPostList() == null) {
            user.setPostList(new ArrayList<Post>());
        }
        if (user.getUsermetaList() == null) {
            user.setUsermetaList(new ArrayList<Usermeta>());
        }
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            List<Post> attachedPostList = new ArrayList<Post>();
            for (Post postListPostToAttach : user.getPostList()) {
                postListPostToAttach = em.getReference(postListPostToAttach.getClass(), postListPostToAttach.getId());
                attachedPostList.add(postListPostToAttach);
            }
            user.setPostList(attachedPostList);
            List<Usermeta> attachedUsermetaList = new ArrayList<Usermeta>();
            for (Usermeta usermetaListUsermetaToAttach : user.getUsermetaList()) {
                usermetaListUsermetaToAttach = em.getReference(usermetaListUsermetaToAttach.getClass(), usermetaListUsermetaToAttach.getId());
                attachedUsermetaList.add(usermetaListUsermetaToAttach);
            }
            user.setUsermetaList(attachedUsermetaList);
            em.persist(user);
            for (Post postListPost : user.getPostList()) {
                User oldUserIdOfPostListPost = postListPost.getUserId();
                postListPost.setUserId(user);
                postListPost = em.merge(postListPost);
                if (oldUserIdOfPostListPost != null) {
                    oldUserIdOfPostListPost.getPostList().remove(postListPost);
                    oldUserIdOfPostListPost = em.merge(oldUserIdOfPostListPost);
                }
            }
            for (Usermeta usermetaListUsermeta : user.getUsermetaList()) {
                User oldUserIdOfUsermetaListUsermeta = usermetaListUsermeta.getUserId();
                usermetaListUsermeta.setUserId(user);
                usermetaListUsermeta = em.merge(usermetaListUsermeta);
                if (oldUserIdOfUsermetaListUsermeta != null) {
                    oldUserIdOfUsermetaListUsermeta.getUsermetaList().remove(usermetaListUsermeta);
                    oldUserIdOfUsermetaListUsermeta = em.merge(oldUserIdOfUsermetaListUsermeta);
                }
            }
//            em.getTransaction().commit();
//        }
//        finally {
//            if (em != null) {
//                em.close();
//            }
//        }
    }

    @Override
    @Transactional
    public void edit(User user) throws IllegalOrphanException, NonexistentEntityException, Exception {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            User persistentUser = em.find(User.class, user.getId());
            List<Post> postListOld = persistentUser.getPostList();
            List<Post> postListNew = user.getPostList();
            List<Usermeta> usermetaListOld = persistentUser.getUsermetaList();
            List<Usermeta> usermetaListNew = user.getUsermetaList();
            List<String> illegalOrphanMessages = null;
            for (Post postListOldPost : postListOld) {
                if (!postListNew.contains(postListOldPost)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Post " + postListOldPost + " since its userId field is not nullable.");
                }
            }
            for (Usermeta usermetaListOldUsermeta : usermetaListOld) {
                if (!usermetaListNew.contains(usermetaListOldUsermeta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usermeta " + usermetaListOldUsermeta + " since its userId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Post> attachedPostListNew = new ArrayList<Post>();
            for (Post postListNewPostToAttach : postListNew) {
                postListNewPostToAttach = em.getReference(postListNewPostToAttach.getClass(), postListNewPostToAttach.getId());
                attachedPostListNew.add(postListNewPostToAttach);
            }
            postListNew = attachedPostListNew;
            user.setPostList(postListNew);
            List<Usermeta> attachedUsermetaListNew = new ArrayList<Usermeta>();
            for (Usermeta usermetaListNewUsermetaToAttach : usermetaListNew) {
                usermetaListNewUsermetaToAttach = em.getReference(usermetaListNewUsermetaToAttach.getClass(), usermetaListNewUsermetaToAttach.getId());
                attachedUsermetaListNew.add(usermetaListNewUsermetaToAttach);
            }
            usermetaListNew = attachedUsermetaListNew;
            user.setUsermetaList(usermetaListNew);
            user = em.merge(user);
            for (Post postListNewPost : postListNew) {
                if (!postListOld.contains(postListNewPost)) {
                    User oldUserIdOfPostListNewPost = postListNewPost.getUserId();
                    postListNewPost.setUserId(user);
                    postListNewPost = em.merge(postListNewPost);
                    if (oldUserIdOfPostListNewPost != null && !oldUserIdOfPostListNewPost.equals(user)) {
                        oldUserIdOfPostListNewPost.getPostList().remove(postListNewPost);
                        oldUserIdOfPostListNewPost = em.merge(oldUserIdOfPostListNewPost);
                    }
                }
            }
            for (Usermeta usermetaListNewUsermeta : usermetaListNew) {
                if (!usermetaListOld.contains(usermetaListNewUsermeta)) {
                    User oldUserIdOfUsermetaListNewUsermeta = usermetaListNewUsermeta.getUserId();
                    usermetaListNewUsermeta.setUserId(user);
                    usermetaListNewUsermeta = em.merge(usermetaListNewUsermeta);
                    if (oldUserIdOfUsermetaListNewUsermeta != null && !oldUserIdOfUsermetaListNewUsermeta.equals(user)) {
                        oldUserIdOfUsermetaListNewUsermeta.getUsermetaList().remove(usermetaListNewUsermeta);
                        oldUserIdOfUsermetaListNewUsermeta = em.merge(oldUserIdOfUsermetaListNewUsermeta);
                    }
                }
            }
//            em.getTransaction().commit();
        }
        catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = user.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
//        finally {
//            if (em != null) {
//                em.close();
//            }
//        }
    }

    @Override
    @Transactional
    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getId();
            }
            catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Post> postListOrphanCheck = user.getPostList();
            for (Post postListOrphanCheckPost : postListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Post " + postListOrphanCheckPost + " in its postList field has a non-nullable userId field.");
            }
            List<Usermeta> usermetaListOrphanCheck = user.getUsermetaList();
            for (Usermeta usermetaListOrphanCheckUsermeta : usermetaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Usermeta " + usermetaListOrphanCheckUsermeta + " in its usermetaList field has a non-nullable userId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(user);
//            em.getTransaction().commit();
//        }
//        finally {
//            if (em != null) {
//                em.close();
//            }
//        }
    }

    @Override
    public List<User> findEntities() {
        return findEntities(true, -1, -1);
    }

    @Override
    public List<User> findEntities(int maxResults, int firstResult) {
        return findEntities(false, maxResults, firstResult);
    }

    private List<User> findEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
//        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
//        }
//        finally {
//            em.close();
//        }
    }

    @Override
    public User find(Integer id) {
//        EntityManager em = getEntityManager();
//        try {
            return em.find(User.class, id);
//        }
//        finally {
//            em.close();
//        }
    }

    @Override
    public int getCount() {
//        EntityManager em = getEntityManager();
//        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ( (Long) q.getSingleResult() ).intValue();
//        }
//        finally {
//            em.close();
//        }
    }

}

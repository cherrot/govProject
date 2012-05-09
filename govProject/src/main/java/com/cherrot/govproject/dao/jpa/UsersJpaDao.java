/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cherrot.govproject.model.Posts;
import java.util.ArrayList;
import java.util.List;
import com.cherrot.govproject.model.Usermeta;
import com.cherrot.govproject.model.Users;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author cherrot
 */
public class UsersJpaDao implements Serializable {

    public UsersJpaDao(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) {
        if (users.getPostsList() == null) {
            users.setPostsList(new ArrayList<Posts>());
        }
        if (users.getUsermetaList() == null) {
            users.setUsermetaList(new ArrayList<Usermeta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Posts> attachedPostsList = new ArrayList<Posts>();
            for (Posts postsListPostsToAttach : users.getPostsList()) {
                postsListPostsToAttach = em.getReference(postsListPostsToAttach.getClass(), postsListPostsToAttach.getId());
                attachedPostsList.add(postsListPostsToAttach);
            }
            users.setPostsList(attachedPostsList);
            List<Usermeta> attachedUsermetaList = new ArrayList<Usermeta>();
            for (Usermeta usermetaListUsermetaToAttach : users.getUsermetaList()) {
                usermetaListUsermetaToAttach = em.getReference(usermetaListUsermetaToAttach.getClass(), usermetaListUsermetaToAttach.getId());
                attachedUsermetaList.add(usermetaListUsermetaToAttach);
            }
            users.setUsermetaList(attachedUsermetaList);
            em.persist(users);
            for (Posts postsListPosts : users.getPostsList()) {
                Users oldUserIdOfPostsListPosts = postsListPosts.getUserId();
                postsListPosts.setUserId(users);
                postsListPosts = em.merge(postsListPosts);
                if (oldUserIdOfPostsListPosts != null) {
                    oldUserIdOfPostsListPosts.getPostsList().remove(postsListPosts);
                    oldUserIdOfPostsListPosts = em.merge(oldUserIdOfPostsListPosts);
                }
            }
            for (Usermeta usermetaListUsermeta : users.getUsermetaList()) {
                Users oldUserIdOfUsermetaListUsermeta = usermetaListUsermeta.getUserId();
                usermetaListUsermeta.setUserId(users);
                usermetaListUsermeta = em.merge(usermetaListUsermeta);
                if (oldUserIdOfUsermetaListUsermeta != null) {
                    oldUserIdOfUsermetaListUsermeta.getUsermetaList().remove(usermetaListUsermeta);
                    oldUserIdOfUsermetaListUsermeta = em.merge(oldUserIdOfUsermetaListUsermeta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users persistentUsers = em.find(Users.class, users.getId());
            List<Posts> postsListOld = persistentUsers.getPostsList();
            List<Posts> postsListNew = users.getPostsList();
            List<Usermeta> usermetaListOld = persistentUsers.getUsermetaList();
            List<Usermeta> usermetaListNew = users.getUsermetaList();
            List<String> illegalOrphanMessages = null;
            for (Posts postsListOldPosts : postsListOld) {
                if (!postsListNew.contains(postsListOldPosts)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Posts " + postsListOldPosts + " since its userId field is not nullable.");
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
            List<Posts> attachedPostsListNew = new ArrayList<Posts>();
            for (Posts postsListNewPostsToAttach : postsListNew) {
                postsListNewPostsToAttach = em.getReference(postsListNewPostsToAttach.getClass(), postsListNewPostsToAttach.getId());
                attachedPostsListNew.add(postsListNewPostsToAttach);
            }
            postsListNew = attachedPostsListNew;
            users.setPostsList(postsListNew);
            List<Usermeta> attachedUsermetaListNew = new ArrayList<Usermeta>();
            for (Usermeta usermetaListNewUsermetaToAttach : usermetaListNew) {
                usermetaListNewUsermetaToAttach = em.getReference(usermetaListNewUsermetaToAttach.getClass(), usermetaListNewUsermetaToAttach.getId());
                attachedUsermetaListNew.add(usermetaListNewUsermetaToAttach);
            }
            usermetaListNew = attachedUsermetaListNew;
            users.setUsermetaList(usermetaListNew);
            users = em.merge(users);
            for (Posts postsListNewPosts : postsListNew) {
                if (!postsListOld.contains(postsListNewPosts)) {
                    Users oldUserIdOfPostsListNewPosts = postsListNewPosts.getUserId();
                    postsListNewPosts.setUserId(users);
                    postsListNewPosts = em.merge(postsListNewPosts);
                    if (oldUserIdOfPostsListNewPosts != null && !oldUserIdOfPostsListNewPosts.equals(users)) {
                        oldUserIdOfPostsListNewPosts.getPostsList().remove(postsListNewPosts);
                        oldUserIdOfPostsListNewPosts = em.merge(oldUserIdOfPostsListNewPosts);
                    }
                }
            }
            for (Usermeta usermetaListNewUsermeta : usermetaListNew) {
                if (!usermetaListOld.contains(usermetaListNewUsermeta)) {
                    Users oldUserIdOfUsermetaListNewUsermeta = usermetaListNewUsermeta.getUserId();
                    usermetaListNewUsermeta.setUserId(users);
                    usermetaListNewUsermeta = em.merge(usermetaListNewUsermeta);
                    if (oldUserIdOfUsermetaListNewUsermeta != null && !oldUserIdOfUsermetaListNewUsermeta.equals(users)) {
                        oldUserIdOfUsermetaListNewUsermeta.getUsermetaList().remove(usermetaListNewUsermeta);
                        oldUserIdOfUsermetaListNewUsermeta = em.merge(oldUserIdOfUsermetaListNewUsermeta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = users.getId();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Posts> postsListOrphanCheck = users.getPostsList();
            for (Posts postsListOrphanCheckPosts : postsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the Posts " + postsListOrphanCheckPosts + " in its postsList field has a non-nullable userId field.");
            }
            List<Usermeta> usermetaListOrphanCheck = users.getUsermetaList();
            for (Usermeta usermetaListOrphanCheckUsermeta : usermetaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the Usermeta " + usermetaListOrphanCheckUsermeta + " in its usermetaList field has a non-nullable userId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(users);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Users findUsers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao.jpa;

import com.cherrot.govproject.dao.TermDao;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Term;
import com.cherrot.govproject.model.Term.TermType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@Repository
public class TermJpaDao implements TermDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void create(Term term) {
        if (term.getTermList() == null) {
            term.setTermList(new ArrayList<Term>());
        }
        if (term.getPostList() == null) {
            term.setPostList(new ArrayList<Post>());
        }
        Term termParent = term.getTermParent();
        if (termParent != null) {
            termParent = em.getReference(termParent.getClass(), termParent.getId());
            term.setTermParent(termParent);
        }
        List<Term> attachedTermList = new ArrayList<Term>();
        for (Term termListTermToAttach : term.getTermList()) {
            termListTermToAttach = em.getReference(termListTermToAttach.getClass(), termListTermToAttach.getId());
            attachedTermList.add(termListTermToAttach);
        }
        term.setTermList(attachedTermList);
        List<Post> attachedPostList = new ArrayList<Post>();
        for (Post postListPostToAttach : term.getPostList()) {
            postListPostToAttach = em.getReference(postListPostToAttach.getClass(), postListPostToAttach.getId());
            attachedPostList.add(postListPostToAttach);
        }
        term.setPostList(attachedPostList);
        em.persist(term);
        if (termParent != null) {
            termParent.getTermList().add(term);
            termParent = em.merge(termParent);
        }
        for (Term termListTerm : term.getTermList()) {
            Term oldTermParentOfTermListTerm = termListTerm.getTermParent();
            termListTerm.setTermParent(term);
            termListTerm = em.merge(termListTerm);
            if (oldTermParentOfTermListTerm != null) {
                oldTermParentOfTermListTerm.getTermList().remove(termListTerm);
                oldTermParentOfTermListTerm = em.merge(oldTermParentOfTermListTerm);
            }
        }
        for (Post postListPost : term.getPostList()) {
            postListPost.getTermList().add(term);
            postListPost = em.merge(postListPost);
        }
    }

    @Override
    @Transactional
    public void edit(Term term) throws NonexistentEntityException, Exception {
        try {
            Term persistentTerm = em.find(Term.class, term.getId());
            Term termParentOld = persistentTerm.getTermParent();
            Term termParentNew = term.getTermParent();
            List<Term> termListOld = persistentTerm.getTermList();
            List<Term> termListNew = term.getTermList();
            List<Post> postListOld = persistentTerm.getPostList();
            List<Post> postListNew = term.getPostList();
            if (termParentNew != null) {
                termParentNew = em.getReference(termParentNew.getClass(), termParentNew.getId());
                term.setTermParent(termParentNew);
            }
            List<Term> attachedTermListNew = new ArrayList<Term>();
            for (Term termListNewTermToAttach : termListNew) {
                termListNewTermToAttach = em.getReference(termListNewTermToAttach.getClass(), termListNewTermToAttach.getId());
                attachedTermListNew.add(termListNewTermToAttach);
            }
            termListNew = attachedTermListNew;
            term.setTermList(termListNew);
            List<Post> attachedPostListNew = new ArrayList<Post>();
            for (Post postListNewPostToAttach : postListNew) {
                postListNewPostToAttach = em.getReference(postListNewPostToAttach.getClass(), postListNewPostToAttach.getId());
                attachedPostListNew.add(postListNewPostToAttach);
            }
            postListNew = attachedPostListNew;
            term.setPostList(postListNew);
            term = em.merge(term);
            if (termParentOld != null && !termParentOld.equals(termParentNew)) {
                termParentOld.getTermList().remove(term);
                termParentOld = em.merge(termParentOld);
            }
            if (termParentNew != null && !termParentNew.equals(termParentOld)) {
                termParentNew.getTermList().add(term);
                termParentNew = em.merge(termParentNew);
            }
            for (Term termListOldTerm : termListOld) {
                if (!termListNew.contains(termListOldTerm)) {
                    termListOldTerm.setTermParent(null);
                    termListOldTerm = em.merge(termListOldTerm);
                }
            }
            for (Term termListNewTerm : termListNew) {
                if (!termListOld.contains(termListNewTerm)) {
                    Term oldTermParentOfTermListNewTerm = termListNewTerm.getTermParent();
                    termListNewTerm.setTermParent(term);
                    termListNewTerm = em.merge(termListNewTerm);
                    if (oldTermParentOfTermListNewTerm != null && !oldTermParentOfTermListNewTerm.equals(term)) {
                        oldTermParentOfTermListNewTerm.getTermList().remove(termListNewTerm);
                        oldTermParentOfTermListNewTerm = em.merge(oldTermParentOfTermListNewTerm);
                    }
                }
            }
            for (Post postListOldPost : postListOld) {
                if (!postListNew.contains(postListOldPost)) {
                    postListOldPost.getTermList().remove(term);
                    postListOldPost = em.merge(postListOldPost);
                }
            }
            for (Post postListNewPost : postListNew) {
                if (!postListOld.contains(postListNewPost)) {
                    postListNewPost.getTermList().add(term);
                    postListNewPost = em.merge(postListNewPost);
                }
            }
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = term.getId();
                if (find(id) == null) {
                    throw new NonexistentEntityException("The term with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    @Override
    @Transactional
    public void destroy(Integer id) throws NonexistentEntityException {
        Term term;
        try {
            term = em.getReference(Term.class, id);
            term.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The term with id " + id + " no longer exists.", enfe);
        }
        Term termParent = term.getTermParent();
        if (termParent != null) {
            termParent.getTermList().remove(term);
            termParent = em.merge(termParent);
        }
        List<Term> termList = term.getTermList();
        for (Term termListTerm : termList) {
            termListTerm.setTermParent(null);
            termListTerm = em.merge(termListTerm);
        }
        List<Post> postList = term.getPostList();
        for (Post postListPost : postList) {
            postListPost.getTermList().remove(term);
            postListPost = em.merge(postListPost);
        }
        em.remove(term);
    }

    @Override
    public List<Term> findEntities() {
        return findEntities(true, -1, -1);
    }

    @Override
    public List<Term> findEntities(int maxResults, int firstResult) {
        return findEntities(false, maxResults, firstResult);
    }

    private List<Term> findEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Term.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    @Override
    public Term find(Integer id) {
        return em.find(Term.class, id);
    }

    @Override
    public int getCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Term> rt = cq.from(Term.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public Term findByNameAndType(String name, Term.TermType type) {
        return em.createNamedQuery("Term.findByNameAndType", Term.class)
            .setParameter("name", name).setParameter("type", type).getSingleResult();
    }

    @Override
    public Term findBySlug(String slug) {
        return em.createNamedQuery("Term.findBySlug", Term.class)
            .setParameter("slug", slug).getSingleResult();
    }

    @Override
    public void save(Term model) {
        if (model.getId() == null) {
            create(model);
        } else {
            try {
                edit(model);
            } catch (Exception ex) {
                Logger.getLogger(CommentmetaJpaDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public List<Term> findEntitiesByType(TermType type) {
        return em.createNamedQuery("Term.findEntitiesByType", Term.class)
            .setParameter("type", type).getResultList();
    }

    @Override
    public List<Term> findEntitiesByType(TermType type, int maxResults, int firstResult) {
        Query q = em.createNamedQuery("Term.findEntitiesByType", Term.class)
            .setParameter("type", type);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    @Override
    public List<Term> findEntitiesByTypeOrderbyCount(TermType type) {
//        return em.createNamedQuery("Term.findEntitiesByTypeOrderbyCount", Term.class)
//            .setParameter("type", type).getResultList();
        return em.createNamedQuery("Term.findEntitiesByTypeOrderbyCount", Term.class)
            .setParameter("type", type).getResultList();
    }

    @Override
    public List<Term> findEntitiesByTypeOrderbyCount(TermType type, int maxResults, int firstResult) {
        Query q = em.createNamedQuery("Term.findEntitiesByTypeOrderbyCount", Term.class)
            .setParameter("type", type);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }
}

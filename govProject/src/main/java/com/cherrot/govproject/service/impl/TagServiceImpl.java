/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.dao.TagDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Category;
import com.cherrot.govproject.model.Tag;
import com.cherrot.govproject.service.TagService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service manages Category and TermTaxonomy.
 * @author cherrot
 */
@Service
public class TagServiceImpl implements TagService {

    @Inject
    private TagDao tagDao;

    @Override
    @Transactional
    public void create(Tag tag) {
        tagDao.create(tag);
    }

    @Override
    @Transactional
    public List<Tag> createTagsByName(List<String> tagStrings) {
        Tag tag = null;
        List<Tag> tags = new ArrayList<Tag>();
        for (String tagString : tagStrings) {
            try {
               tag = tagDao.findByName(tagString);
            }
            catch (NoResultException  e) {
                tag = new Tag();
                tag.setName(tagString);
                tag.setSlug(tagString);
                create(tag);
            }
            tags.add(tag);
        }
        return tags;
    }

    @Override
    @Transactional
    public void edit(Tag model) {
        try {
            tagDao.edit(model);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(TagServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(TagServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(TagServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
//    @Transactional(readOnly=true)
    public Tag find(Integer id) {
        return tagDao.find(id);
    }

    @Override
    @Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
    public Tag find(Integer id, boolean withPosts) {
        Tag tag = find(id);
        if (withPosts) tag.getPostList().isEmpty();
        return tag;
    }

    @Override
    @Transactional
    public void destroy(Integer id) {
        try {
            tagDao.destroy(id);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(TagServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(TagServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int getCount() {
        return tagDao.getCount();
    }

    @Override
//    @Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
    public List<Tag> list() {
        return tagDao.findEntities();
    }

    @Override
//    @Transactional(readOnly=true)
    public List<Tag> list(int pageNum, int pageSize) {
        return tagDao.findEntities(pageSize, (pageNum-1)*pageSize);
    }

    private void processDependency(List<Category> terms, boolean withPosts, boolean withTerms) {
        if (withPosts || withTerms) {
            for (Category term : terms) {
                if (withPosts) term.getPostList().isEmpty();
                if (withTerms) term.getCategoryList().isEmpty();
            }
        }
    }

    @Override
    public void save(Tag model) {
        if (model.getId() == null) {
            create(model);
        } else {
            edit(model);
        }
    }

}

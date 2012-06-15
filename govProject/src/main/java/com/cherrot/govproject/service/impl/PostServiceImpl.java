/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.dao.PostDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Postmeta;
import com.cherrot.govproject.model.Category;
import com.cherrot.govproject.service.PostService;
import com.cherrot.govproject.service.SiteLogService;
import com.cherrot.govproject.service.TermService;
import static com.cherrot.govproject.util.Constants.DEFAULT_PAGE_SIZE;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author cherrot
 */
@Service
public class PostServiceImpl implements PostService {

    @Inject
    private PostDao postDao;
    @Inject
    private TermService termService;
    @Inject
    private SiteLogService siteLogService;

    @Override
    @Transactional
    public void create(Post post, List<Category> categories, List<String> tags) {
        create(post, categories, tags, null);
    }

    @Override
    @Transactional
    public void create(Post post, List<Category> categories, List<String> tags, List<Postmeta> postmetas) {
        post.setTermList(categories);
        post.setPostmetaList(postmetas);
        List<Category> tagTerms = termService.createTagsByName(tags);
        addTermList(post, tagTerms);
        postDao.create(post);
//        siteLogService.create(post.getUser(),post.getTitle()+"被创建" );
    }

    @Override
    @Transactional
    public void create(Post post) {
        postDao.create(post);
//        siteLogService.create(post.getUser(),post.getTitle()+"被创建" );
    }

    @Override
//    @Transactional(readOnly=true)
    public Post find(Integer id) {
        return postDao.find(id);
    }

    @Override
    @Transactional
    public void destroy(Integer id) {
        Post post = postDao.find(id);
//        siteLogService.create(post.getUser(), post.getTitle()+"被删除" );
        try {
            postDao.destroy(id);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int getCount() {
        return postDao.getCount();
    }

    @Override
//    @Transactional(readOnly=true)
    public List<Post> list() {
        return postDao.findEntities();
    }

    @Override
//    @Transactional(readOnly=true)
    public List<Post> list(int pageNum) {
        return list(pageNum, DEFAULT_PAGE_SIZE);
    }

    @Override
//    @Transactional(readOnly=true)
    public List<Post> list(int pageNum, int pageSize) {
        return postDao.findEntities(pageSize, (pageNum-1)*pageSize);
    }

    @Override
    @Transactional
    public void edit(Post model) {
//        siteLogService.create(model.getUser(), model.getTitle()+"被修改" );
        try {
            postDao.edit(model);
        }
        catch (IllegalOrphanException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (NonexistentEntityException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    @Transactional
    //FIXME 应当让此方法处理term和post的关系！ 下同！
    public void addTerm(Post post, Category term) {
        post.getTermList().add(term);
    }

    @Override
    @Transactional
    public void addTermList(Post post, List<Category> terms) {
        post.getTermList().addAll(terms);
    }

    @Override
    @Transactional
    public void removeTerm(Post post, Category term) {
        post.getTermList().remove(term);
    }

    @Override
    @Transactional
    public void removeTermList(Post post, List<Category> terms) {
        post.getTermList().removeAll(terms);
    }

    @Override
    @Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
    public Post find(Integer id, boolean withComments, boolean withPostmetas, boolean withTerms) {
        Post post = find(id);
        processDependency(post, withComments, withPostmetas, withTerms);
        return post;
    }

    @Override
    @Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
    public Post findBySlug(String slug, boolean withComments, boolean withPostmetas, boolean withTerms) {
        Post post = postDao.findBySlug(slug);
        processDependency(post, withComments, withPostmetas, withTerms);
        return post;
    }

    @Override
//    @Transactional(readOnly=true)
    public List<Post> listNewestPostsByTerm(Integer termId, int pageNum, int pageSize) {
        return postDao.findEntitiesByTermDescOrder(termId, pageSize, (pageNum-1)*pageSize);
    }

    @Override
//    @Transactional(readOnly=true)
    public List<Post> listNewestPostsByCategoryName(String categoryName, int pageNum, int pageSize) {
        return postDao.findEntitiesByCategoryNameDescOrder(categoryName, pageSize, (pageNum-1)*pageSize);
    }

    private void processDependency(Post post, boolean withComments, boolean withPostmetas, boolean withTerms){
        if (withComments) post.getCommentList().isEmpty();
        if (withPostmetas) post.getPostmetaList().isEmpty();
        if (withTerms) post.getTermList().isEmpty();
    }

    @Override
    public void save(Post model) {
        if (model.getId() == null) {
            create(model);
        } else {
            edit(model);
        }
    }

    @Override
    public List<Post> listByUser(Integer userId, int pageNum) {
        return listByUser(userId, pageNum, DEFAULT_PAGE_SIZE);
    }

    @Override
    public List<Post> listByUser(Integer userId, int pageNum, int pageSize) {
        List<Post> posts = postDao.findEntitiesByUserId(userId, pageSize, (pageNum-1)*pageSize);
        return posts;
    }
//FIXME!!!
    @Override
    public List<Post> listNewesPostsByUser(Integer userId, int pageNum) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Post> listNewesPostsByUser(Integer userId, int pageNum, int pageSize) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getCountByUser(Integer userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getCountByTerm(Integer termId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional
    public void addAttachment(Integer postId, File localFile, String mime) {
        Post videoPost = new Post();
        Post postParent = find(postId);
        videoPost.setUser(postParent.getUser());
        videoPost.setPostParent(postParent);
        videoPost.setType(Post.PostType.ATTACHMENT);
        videoPost.setMime(mime);
        videoPost.setContent("");
        videoPost.setSlug("testvideo");
        videoPost.setTitle("testvideo");
        create(videoPost);

        String content = "<object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0\">"
            + "<embed src=\"/govProject/resources/misc/flvplayer.swf\" allowfullscreen=\"true\" flashvars=\"vcastr_file=" + "/govProject/uploads/MVI_0015.AVI.flv" + "&BufferTime=3\" quality=\"high\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" type=\"application/x-shockwave-flash\" width=\"459\" height=\"370\"></embed>"
            + "</object>";
        postParent.setContent(content);
    }
}

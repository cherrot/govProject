/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.service.impl;

import com.cherrot.govproject.dao.PostDao;
import com.cherrot.govproject.dao.exceptions.IllegalOrphanException;
import com.cherrot.govproject.dao.exceptions.NonexistentEntityException;
import com.cherrot.govproject.model.Category;
import com.cherrot.govproject.model.Post;
import com.cherrot.govproject.model.Postmeta;
import com.cherrot.govproject.model.Tag;
import com.cherrot.govproject.service.CategoryService;
import com.cherrot.govproject.service.PostService;
import com.cherrot.govproject.service.SiteLogService;
import com.cherrot.govproject.service.TagService;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cherrot
 */
@Service
public class PostServiceImpl implements PostService {

    @Inject
    private PostDao postDao;
    @Inject
    private CategoryService categoryService;
    @Inject
    private TagService tagService;
    @Inject
    private SiteLogService siteLogService;

    @Override
    @Transactional
    public void create(Post post, List<Category> categories, List<String> tags) {
        create(post, categories, tags, null);
    }

    @Override
    @Transactional
    public void create(Post post, List<Category> categories, List<String> tagStrings, List<Postmeta> postmetas) {
        post.setCategoryList(categories);
        post.setPostmetaList(postmetas);
        List<Tag> tags = tagService.createTagsByName(tagStrings);
        post.setTagList(tags);
        postDao.create(post);
        siteLogService.create(post.getUser(), "创建了文章（ID:" + post.getId() + "）。标题：" + post.getTitle() );
    }

    @Override
    @Transactional
    public void create(Post post) {
        postDao.create(post);
        siteLogService.create(post.getUser(), "文章已创建（ID:" + post.getId() + "）。标题：" + post.getTitle() );
    }

    @Override
    public Post find(Integer id) {
        return postDao.find(id);
    }

    @Override
    @Transactional
    public void destroy(Integer id) {
        try {
            Post post = postDao.find(id);
            siteLogService.create(post.getUser(), "文章已删除（ID:" + post.getId() + "）。标题：" + post.getTitle() );
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
    public List<Post> list() {
        return postDao.findEntities();
    }

    @Override
    public List<Post> list(int pageNum, int pageSize) {
        return postDao.findEntities(pageSize, (pageNum-1)*pageSize);
    }

    @Override
    @Transactional
    public void edit(Post post) {
        try {
            postDao.edit(post);
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
        siteLogService.create(post.getUser(), "文章已更新（ID:" + post.getId() + "）。标题：" + post.getTitle() );
    }

    @Override
    @Transactional
    public void addCategory(Post post, Category term) {
//        term.getPostList().add(post);
            post.getCategoryList().add(term);
        try {
            postDao.edit(post);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    @Transactional
    public void addCategoryList(Post post, List<Category> terms) {
        post.getCategoryList().addAll(terms);
        try {
            postDao.edit(post);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    @Transactional
    public void removeCategory(Post post, Category term) {
            post.getCategoryList().remove(term);
        try {
            postDao.edit(post);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    @Transactional
    public void removeCategoryList(Post post, List<Category> terms) {
        post.getCategoryList().removeAll(terms);
        try {
            postDao.edit(post);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    @Transactional
    public void addTag(Post post, Tag tag) {
            post.getTagList().add(tag);
        try {
            postDao.edit(post);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    @Transactional
    public void addTagList(Post post, List<Tag> tags) {
        post.getTagList().addAll(tags);
        try {
            postDao.edit(post);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    @Transactional
    public void removeTag(Post post, Tag tag) {
            post.getTagList().remove(tag);
        try {
            postDao.edit(post);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    @Transactional
    public void removeTagList(Post post, List<Tag> tags) {
        post.getTagList().removeAll(tags);
        try {
            postDao.edit(post);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PostServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    @Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
    public Post find(Integer id, boolean withComments, boolean withPostmetas, boolean withCategories, boolean withTags) {
        Post post = find(id);
        processDependency(post, withComments, withPostmetas, withCategories, withTags);
        return post;
    }

    @Override
    @Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
    public Post findBySlug(String slug, boolean withComments, boolean withPostmetas, boolean withCategories, boolean withTags) {
        Post post = postDao.findBySlug(slug);
        processDependency(post, withComments, withPostmetas, withCategories, withTags);
        return post;
    }

    @Override
    public List<Post> listNewestPostsByCategory(Integer termId, int pageNum, int pageSize) {
        return postDao.findEntitiesByCategoryDescOrder(termId, pageSize, (pageNum-1)*pageSize);
    }

    @Override
    public List<Post> listNewestPostsByCategorySlug(String categoryName, int pageNum, int pageSize) {
        return postDao.findEntitiesByCategorySlugDescOrder(categoryName, pageSize, (pageNum-1)*pageSize);
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
    public List<Post> listByUser(Integer userId, int pageNum, int pageSize) {
        List<Post> posts = postDao.findEntitiesByUserId(userId, pageSize, (pageNum-1)*pageSize);
        return posts;
    }

//FIXME 尚未实现
    @Override
    public List<Post> listNewesPostsByUser(Integer userId, int pageNum, int pageSize) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getCountByUser(Integer userId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getCountByCategory(Integer termId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Post> listNewestPostsByTag(Integer tagId, int pageNum, int pageSize) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Post> listNewestPostsByTagSlug(String tagName, int pageNum, int pageSize) {
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

    private void processDependency(Post post, boolean withComments, boolean withPostmetas, boolean withCategories, boolean withTags){
        if (withComments) post.getCommentList().isEmpty();
        if (withPostmetas) post.getPostmetaList().isEmpty();
        if (withCategories) post.getCategoryList().isEmpty();
        if (withTags) post.getTagList().isEmpty();
    }

    //TODO 待实现
    @Override
    public int getCountByTag(Integer tagId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author sai
 */
@Entity
@Table(name = "posts", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"slug"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Post.findAll", query = "SELECT p FROM Post p"),
    @NamedQuery(name = "Post.findById", query = "SELECT p FROM Post p WHERE p.id = :id"),
    @NamedQuery(name = "Post.findByCreateDate", query = "SELECT p FROM Post p WHERE p.createDate = :createDate"),
    @NamedQuery(name = "Post.findByModifyDate", query = "SELECT p FROM Post p WHERE p.modifyDate = :modifyDate"),
    @NamedQuery(name = "Post.findByCommentStatus", query = "SELECT p FROM Post p WHERE p.commentStatus = :commentStatus"),
    @NamedQuery(name = "Post.findByCommentCount", query = "SELECT p FROM Post p WHERE p.commentCount = :commentCount"),
    @NamedQuery(name = "Post.findByStatus", query = "SELECT p FROM Post p WHERE p.status = :status"),
    @NamedQuery(name = "Post.findByType", query = "SELECT p FROM Post p WHERE p.type = :type"),
    @NamedQuery(name = "Post.findBySlug", query = "SELECT p FROM Post p WHERE p.slug = :slug"),
    @NamedQuery(name = "Post.findByTitle", query = "SELECT p FROM Post p WHERE p.title = :title"),
    @NamedQuery(name = "Post.findByPassword", query = "SELECT p FROM Post p WHERE p.password = :password"),
    @NamedQuery(name = "Post.findByMime", query = "SELECT p FROM Post p WHERE p.mime = :mime"),
    //以下为等价式
    //@NamedQuery(name = "Post.findEntitiesByTermOrderbyCreateDate", query="SELECT p FROM Post p INNER JOIN p.termList t WHERE t.id IN (:termId) ORDER BY p.createDate DESC"),
    @NamedQuery(name = "Post.findEntitiesByCategoryDescOrder", query="SELECT p FROM Post p, IN(p.categoryList) category WHERE category.id = :categoryId ORDER BY p.id DESC"),
    @NamedQuery(name = "Post.findEntitiesByCategoryNameDescOrder", query="SELECT p FROM Post p INNER JOIN p.categoryList t WHERE t.name = :categoryName ORDER BY p.id DESC"),
    @NamedQuery(name = "Post.findEntitiesByUserID",query="SELECT p FROM Post p WHERE p.user.id = :userId")
})
/**
 * state_field_path_expression must have a string, numeric, or enum value.
 * @See http://openjpa.apache.org/builds/1.1.0/docs/jpa_langref.html#jpa_langref_in
 */
public class Post implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum PostStatus {
        DRAFT("草稿"), PUBLISHED("发布"), PENDING("待审核");

        private String description;

        private PostStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    //TODO 完成所有JPA类的枚举类型
    public enum PostType {
        POST("文章"), ATTACHMENT("附件");

        private String description;

        private PostType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
//    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "createDate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "modifyDate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "commentStatus", nullable = false)
    private boolean commentStatus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "commentCount", nullable = false)
    private int commentCount;
    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    @NotNull
//    @Size(min = 1, max = 20) Can't use this annotation for enum
    @Column(name = "status", nullable = false, length = 20)
    private PostStatus status;
    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    @NotNull
//    @Size(min = 1, max = 20) Can't use this annotation for enum
    @Column(name = "type", nullable = false, length = 20)
    private PostType type;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "slug", nullable = false, length = 200)
    private String slug;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title", nullable = false, length = 255)
    private String title;
    @Size(max = 20)
    @Column(name = "password", length = 20)
    private String password;
    @Size(max = 45)
    @Column(name = "mime", length = 45)
    private String mime;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "content", nullable = false, length = 65535)
    private String content;
    @Lob
    @Size(max = 65535)
    @Column(name = "excerpt", length = 65535)
    private String excerpt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<Postmeta> postmetaList;
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private User user;
    @OneToMany(mappedBy = "postParent")
    private List<Post> postList;
    @JoinColumn(name = "post_parent", referencedColumnName = "id")
    @ManyToOne
    private Post postParent;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
//    /**
//     * Retriving the commentList order by its createDate
//     */
//    @OrderBy("commentDate")
    private List<Comment> commentList;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name="category_relationships",
            inverseJoinColumns=@JoinColumn(name="category_id"),
            joinColumns=@JoinColumn(name="post_id"))
    private List<Category> categoryList;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(name="tag_relationships",
            inverseJoinColumns=@JoinColumn(name="tag_id"),
            joinColumns=@JoinColumn(name="post_id"))
    private List<Tag> tagList;

    public Post() {
        createDate = modifyDate = new Date();
        commentStatus = true;
        commentCount = 0;
        status = PostStatus.DRAFT;
        type = PostType.POST;
    }

//    public Post(Integer id) {
//        this.id = id;
//    }

    public Post(/*Integer id,*/ Date createDate, Date modifyDate, boolean commentStatus, int commentCount, PostStatus status, PostType type, String slug, String title, String content) {
//        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.commentStatus = commentStatus;
        this.commentCount = commentCount;
        this.status = status;
        this.type = type;
        this.slug = slug;
        this.title = title;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public boolean getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(boolean commentStatus) {
        this.commentStatus = commentStatus;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public PostType getType() {
        return type;
    }

    public void setType(PostType type) {
        this.type = type;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 如果PostType为post，那么excerpt（摘要）中存储的是文章摘要，可以为空
     * 如果PostType为attachment，那么excerpt(摘要)中存储的是该文件的存储位置的URI
     * @return 文章摘要 或 附件文件的下载URI
     */
    public String getExcerpt() {
        return excerpt;
    }

    /**
     * 如果PostType为post，那么excerpt（摘要）中存储的是文章摘要，可以为空
     * 如果PostType为attachment，那么excerpt(摘要)中存储的是该文件的存储位置的URI
     * @param excerpt 文章摘要或附件URI
     */
    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    @XmlTransient
    public List<Postmeta> getPostmetaList() {
//        if (postmetaList == null) postmetaList = new ArrayList<Postmeta>();
        return postmetaList;
    }

    public void setPostmetaList(List<Postmeta> postmetaList) {
        this.postmetaList = postmetaList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @XmlTransient
    public List<Post> getPostList() {
//        if (postList == null) postList = new ArrayList<Post>();
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public Post getPostParent() {
        return postParent;
    }

    public void setPostParent(Post postParent) {
        this.postParent = postParent;
    }

    @XmlTransient
    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @XmlTransient
    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    @XmlTransient
    public List<Comment> getCommentList() {
//        if (commentList == null) commentList = new ArrayList<Comment>();
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Warning - this method won't work in the case the id fields are not set
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Post)) {
            return false;
        }
        Post other = (Post) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cherrot.govproject.model.Post[ id=" + id + " ]";
    }

}

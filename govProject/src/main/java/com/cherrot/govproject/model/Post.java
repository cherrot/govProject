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
 * 文章实体。文章的type字段表示文章的类型（普通、图文、视频）
 * 注意，只有当文章中的多媒体资源是上传到服务器的情况下才会添加该资源的postmeta,用于多媒体资源的管理。
 * 也就是说，存在这一情况：文章类型不是‘普通’，但postmeta中却没有多媒体资源的描述（用户通过外链引用的多媒体资源）
 * @author sai
 */
@Entity
@Table(name = "posts", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"slug"})})
@XmlRootElement
@NamedQueries({
    //根据JPA规范，MEMBER OF 后面应该为对象的某个字段，而IN后面应该是一个查询参数。这个字段/参数是一个集合。
    @NamedQuery(name = "Post.findAll", query = "SELECT p FROM Post p"),
    @NamedQuery(name = "Post.findAllDesc", query = "SELECT p FROM Post p ORDER BY p.id DESC"),
    @NamedQuery(name = "Post.findById", query = "SELECT p FROM Post p WHERE p.id = :id"),
    @NamedQuery(name = "Post.findByCreateDate", query = "SELECT p FROM Post p WHERE p.createDate = :createDate"),
    @NamedQuery(name = "Post.findByModifyDate", query = "SELECT p FROM Post p WHERE p.modifyDate = :modifyDate"),
    @NamedQuery(name = "Post.findByCommentStatus", query = "SELECT p FROM Post p WHERE p.commentStatus = :commentStatus"),
    @NamedQuery(name = "Post.findByStatus", query = "SELECT p FROM Post p WHERE p.status = :status"),
    @NamedQuery(name = "Post.findByType", query = "SELECT p FROM Post p WHERE p.type = :type"),
    @NamedQuery(name = "Post.findByTypeDesc", query = "SELECT p FROM Post p WHERE p.type = :type ORDER BY p.id DESC"),
    @NamedQuery(name = "Post.findBySlug", query = "SELECT p FROM Post p WHERE p.slug = :slug"),
    @NamedQuery(name = "Post.findByTitle", query = "SELECT p FROM Post p WHERE p.title = :title"),
    //下面两式  IN 和 JOIN 的作用等价。 MEMBER OF 也可完成查询
    @NamedQuery(name = "Post.findByCategoryDesc", query = "SELECT p FROM Post p, IN(p.categoryList) c WHERE c = :category ORDER BY p.id DESC"),
    @NamedQuery(name = "Post.findByCategorySlugDesc", query = "SELECT p FROM Post p INNER JOIN p.categoryList c WHERE c.slug = :categorySlug ORDER BY p.id DESC"),
    @NamedQuery(name = "Post.findByTagDesc", query = "SELECT p FROM Post p WHERE :tag MEMBER OF p.tagList ORDER BY p.id DESC"),
    @NamedQuery(name = "Post.findByTagSlugDesc", query = "SELECT p FROM Post p INNER JOIN p.tagList t WHERE t.slug = :tagSlug ORDER BY p.id DESC"),
    @NamedQuery(name = "Post.findByUser", query = "SELECT p FROM Post p WHERE p.user = :user"),
    @NamedQuery(name = "Post.findByUserDesc", query = "SELECT p FROM Post p WHERE p.user = :user ORDER BY p.id DESC"),
    @NamedQuery(name = "Post.getCount", query = "SELECT COUNT(p) FROM Post p"),
    @NamedQuery(name = "Post.getCountByType", query = "SELECT COUNT(p) FROM Post p WHERE p.type = :type"),
    @NamedQuery(name = "Post.getCountByUser", query = "SELECT COUNT(p) FROM Post p WHERE :user = p.user"),
    @NamedQuery(name = "Post.getCountByCategory", query = "SELECT COUNT(p) FROM Post p WHERE :category MEMBER OF p.categoryList"),
    @NamedQuery(name = "Post.getCountByTag", query = "SELECT COUNT(p) FROM Post p WHERE :tag MEMBER OF p.tagList")
})
/**
 * state_field_path_expression must have a string, numeric, or enum value.
 * XXX: 如果是用来指示多媒体内容的Post,那么其content为用于显示这个多媒体内容的HTML代码，其摘要(excerpt)为多媒体内容的URI
 * @See
 * http://openjpa.apache.org/builds/1.1.0/docs/jpa_langref.html#jpa_langref_in
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

    public enum PostType {

        PLAIN("普通"), IMAGE("图文"), VIDEO("视频");
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
//    /**
//     * Retriving the commentList order by its createDate
//     */
//    @OrderBy("commentDate")
    private List<Comment> commentList;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
        name = "category_relationships",
        inverseJoinColumns = @JoinColumn(name = "category_id"),
        joinColumns = @JoinColumn(name = "post_id")
    )
    private List<Category> categoryList;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
        name = "tag_relationships",
        inverseJoinColumns = @JoinColumn(name = "tag_id"),
        joinColumns = @JoinColumn(name = "post_id")
    )
    private List<Tag> tagList;

    public Post() {
        createDate = modifyDate = new Date();
        commentStatus = true;
        commentCount = 0;
        status = PostStatus.DRAFT;
        type = PostType.PLAIN;
//        processLists();
    }

//    public Post(Integer id) {
//        this.id = id;
//    }
    public Post(/*Integer id,*/Date createDate, Date modifyDate, boolean commentStatus, int commentCount, PostStatus status, PostType type, String slug, String title, String content) {
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
//        processLists();
    }

    /**
     * 在创建实体时实例化实体中的所有集合对象。否则会在DAO层出现空指针异常（Netbeans自动生成JPA控制器的BUG）
     */
//    private void processLists() {
//        postmetaList = new ArrayList<Postmeta>();
//        postList = new ArrayList<Post>();
//        commentList = new ArrayList<Comment>();
//        categoryList = new ArrayList<Category>();
//        tagList = new ArrayList<Tag>();
//    }
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     *
     * @return 文章摘要 或 附件文件的下载URI
     */
    public String getExcerpt() {
        return excerpt;
    }

    /**
     *
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
     *
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

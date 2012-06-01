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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author cherrot
 */
@Entity
@Table(name = "posts")
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
    @NamedQuery(name = "Post.findByTittle", query = "SELECT p FROM Post p WHERE p.tittle = :tittle"),
    @NamedQuery(name = "Post.findByPassword", query = "SELECT p FROM Post p WHERE p.password = :password"),
    @NamedQuery(name = "Post.findByMime", query = "SELECT p FROM Post p WHERE p.mime = :mime")})
public class Post implements Serializable {
    private static final long serialVersionUID = 1L;
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "status", nullable = false, length = 20)
    private String status;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "type", nullable = false, length = 20)
    private String type;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "slug", nullable = false, length = 200)
    private String slug;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "tittle", nullable = false, length = 255)
    private String tittle;
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
    private List<TermRelationship> termRelationshipList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<Comment> commentList;

    public Post() {
    }

    public Post(Integer id) {
        this.id = id;
    }

    public Post(Integer id, Date createDate, Date modifyDate, boolean commentStatus, int commentCount, String status, String type, String slug, String tittle, String content) {
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        this.commentStatus = commentStatus;
        this.commentCount = commentCount;
        this.status = status;
        this.type = type;
        this.slug = slug;
        this.tittle = tittle;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
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

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    @XmlTransient
    public List<Postmeta> getPostmetaList() {
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
    public List<TermRelationship> getTermRelationshipList() {
        return termRelationshipList;
    }

    public void setTermRelationshipList(List<TermRelationship> termRelationshipList) {
        this.termRelationshipList = termRelationshipList;
    }

    @XmlTransient
    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += ( id != null ? id.hashCode() : 0 );
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!( object instanceof Post )) {
            return false;
        }
        Post other = (Post) object;
        if (( this.id == null && other.id != null ) || ( this.id != null && !this.id.equals(other.id) )) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cherrot.govproject.model.Post[ id=" + id + " ]";
    }

}

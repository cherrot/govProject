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
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Posts.findAll", query = "SELECT p FROM Posts p"),
    @NamedQuery(name = "Posts.findById", query = "SELECT p FROM Posts p WHERE p.id = :id"),
    @NamedQuery(name = "Posts.findByCreateDate", query = "SELECT p FROM Posts p WHERE p.createDate = :createDate"),
    @NamedQuery(name = "Posts.findByModifyDate", query = "SELECT p FROM Posts p WHERE p.modifyDate = :modifyDate"),
    @NamedQuery(name = "Posts.findByCommentStatus", query = "SELECT p FROM Posts p WHERE p.commentStatus = :commentStatus"),
    @NamedQuery(name = "Posts.findByCommentCount", query = "SELECT p FROM Posts p WHERE p.commentCount = :commentCount"),
    @NamedQuery(name = "Posts.findByPostParent", query = "SELECT p FROM Posts p WHERE p.postParent = :postParent"),
    @NamedQuery(name = "Posts.findByStatus", query = "SELECT p FROM Posts p WHERE p.status = :status"),
    @NamedQuery(name = "Posts.findByType", query = "SELECT p FROM Posts p WHERE p.type = :type"),
    @NamedQuery(name = "Posts.findBySlug", query = "SELECT p FROM Posts p WHERE p.slug = :slug"),
    @NamedQuery(name = "Posts.findByTittle", query = "SELECT p FROM Posts p WHERE p.tittle = :tittle"),
    @NamedQuery(name = "Posts.findByPassword", query = "SELECT p FROM Posts p WHERE p.password = :password"),
    @NamedQuery(name = "Posts.findByMime", query = "SELECT p FROM Posts p WHERE p.mime = :mime")})
public class Posts implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private boolean commentStatus;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private int commentCount;
    @Column(name = "post_parent")
    private Integer postParent;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(nullable = false, length = 20)
    private String status;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(nullable = false, length = 20)
    private String type;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(nullable = false, length = 200)
    private String slug;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String tittle;
    @Size(max = 20)
    @Column(length = 20)
    private String password;
    @Size(max = 45)
    @Column(length = 45)
    private String mime;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(nullable = false, length = 65535)
    private String content;
    @Lob
    @Size(max = 65535)
    @Column(length = 65535)
    private String excerpt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postId")
    private List<Postmeta> postmetaList;
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Users userId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "posts")
    private List<TermRelationships> termRelationshipsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postId")
    private List<Comments> commentsList;

    public Posts() {
    }

    public Posts(Integer id) {
        this.id = id;
    }

    public Posts(Integer id, Date createDate, Date modifyDate, boolean commentStatus, int commentCount, String status, String type, String slug, String tittle, String content) {
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

    public Integer getPostParent() {
        return postParent;
    }

    public void setPostParent(Integer postParent) {
        this.postParent = postParent;
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

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    @XmlTransient
    public List<TermRelationships> getTermRelationshipsList() {
        return termRelationshipsList;
    }

    public void setTermRelationshipsList(List<TermRelationships> termRelationshipsList) {
        this.termRelationshipsList = termRelationshipsList;
    }

    @XmlTransient
    public List<Comments> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(List<Comments> commentsList) {
        this.commentsList = commentsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Posts)) {
            return false;
        }
        Posts other = (Posts) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cherrot.govproject.model.Posts[ id=" + id + " ]";
    }

}

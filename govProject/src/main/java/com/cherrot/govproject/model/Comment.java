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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author sai
 */
@Entity
@Table(name = "comments")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comment.findAll", query = "SELECT c FROM Comment c"),
    @NamedQuery(name = "Comment.findById", query = "SELECT c FROM Comment c WHERE c.id = :id"),
    @NamedQuery(name = "Comment.findByCommentDate", query = "SELECT c FROM Comment c WHERE c.commentDate = :commentDate"),
    @NamedQuery(name = "Comment.findByApproved", query = "SELECT c FROM Comment c WHERE c.approved = :approved"),
    @NamedQuery(name = "Comment.findByUserId", query = "SELECT c FROM Comment c WHERE c.userId = :userId"),
    @NamedQuery(name = "Comment.findByAuthor", query = "SELECT c FROM Comment c WHERE c.author = :author"),
    @NamedQuery(name = "Comment.findByAuthorEmail", query = "SELECT c FROM Comment c WHERE c.authorEmail = :authorEmail"),
    @NamedQuery(name = "Comment.findByAuthorUrl", query = "SELECT c FROM Comment c WHERE c.authorUrl = :authorUrl"),
    @NamedQuery(name = "Comment.findByAuthorIp", query = "SELECT c FROM Comment c WHERE c.authorIp = :authorIp")})
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
//    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "commentDate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date commentDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "approved", nullable = false)
    private boolean approved;
    @Column(name = "user_id")
    private Integer userId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "author", nullable = false, length = 45)
    private String author;
    @Basic(optional = false)
    //if the field contains email address consider using this annotation to enforce field validation
    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="电子邮件无效")
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "authorEmail", nullable = false, length = 45)
    private String authorEmail;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "authorUrl", nullable = false, length = 200)
    private String authorUrl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 39)
    @Column(name = "authorIp", nullable = false, length = 39)
    private String authorIp;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "content", nullable = false, length = 65535)
    private String content;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comment")
    private List<Commentmeta> commentmetaList;
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Post post;
    @OneToMany(mappedBy = "commentParent")
    private List<Comment> commentList;
    @JoinColumn(name = "comment_parent", referencedColumnName = "id")
    @ManyToOne
    private Comment commentParent;

    public Comment() {
    }

//    public Comment(Integer id) {
//        this.id = id;
//    }

    public Comment(/*Integer id,*/ Date commentDate, boolean approved, String author, String authorEmail, String authorUrl, String authorIp, String content) {
//        this.id = id;
        this.commentDate = commentDate;
        this.approved = approved;
        this.author = author;
        this.authorEmail = authorEmail;
        this.authorUrl = authorUrl;
        this.authorIp = authorIp;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public boolean getApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public String getAuthorIp() {
        return authorIp;
    }

    public void setAuthorIp(String authorIp) {
        this.authorIp = authorIp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @XmlTransient
    public List<Commentmeta> getCommentmetaList() {
        return commentmetaList;
    }

    public void setCommentmetaList(List<Commentmeta> commentmetaList) {
        this.commentmetaList = commentmetaList;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @XmlTransient
    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public Comment getCommentParent() {
        return commentParent;
    }

    public void setCommentParent(Comment commentParent) {
        this.commentParent = commentParent;
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
        if (!(object instanceof Comment)) {
            return false;
        }
        Comment other = (Comment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cherrot.govproject.model.Comment[ id=" + id + " ]";
    }

}

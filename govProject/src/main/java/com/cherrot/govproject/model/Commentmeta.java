/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cherrot
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Commentmeta.findAll", query = "SELECT c FROM Commentmeta c"),
    @NamedQuery(name = "Commentmeta.findById", query = "SELECT c FROM Commentmeta c WHERE c.id = :id"),
    @NamedQuery(name = "Commentmeta.findByMetaKey", query = "SELECT c FROM Commentmeta c WHERE c.metaKey = :metaKey"),
    @NamedQuery(name = "Commentmeta.findByMetaValue", query = "SELECT c FROM Commentmeta c WHERE c.metaValue = :metaValue")})
public class Commentmeta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)//Basic反映映射， Column反映数据库定义（用于重新生成表）
    @NotNull //Notnull 用于数据验证
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(nullable = false, length = 45)
    private String metaKey;
    @Size(max = 255)
    @Column(length = 255)
    private String metaValue;
    @JoinColumn(name = "comment_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Comments commentId;

    public Commentmeta() {
    }

    public Commentmeta(Integer id) {
        this.id = id;
    }

    public Commentmeta(Integer id, String metaKey) {
        this.id = id;
        this.metaKey = metaKey;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMetaKey() {
        return metaKey;
    }

    public void setMetaKey(String metaKey) {
        this.metaKey = metaKey;
    }

    public String getMetaValue() {
        return metaValue;
    }

    public void setMetaValue(String metaValue) {
        this.metaValue = metaValue;
    }

    public Comments getCommentId() {
        return commentId;
    }

    public void setCommentId(Comments commentId) {
        this.commentId = commentId;
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
        if (!(object instanceof Commentmeta)) {
            return false;
        }
        Commentmeta other = (Commentmeta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cherrot.govproject.model.Commentmeta[ id=" + id + " ]";
    }

}

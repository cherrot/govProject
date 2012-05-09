/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.model;

import java.io.Serializable;
import javax.persistence.Basic;
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
    //@Basic(optional) is (should be) checked on runtime by persistence provider before saving to DB. @Column is a column definition in database and is used for schema generation :
    //http://stackoverflow.com/questions/1383229/java-persistence-jpa-column-vs-basic
    //http://stackoverflow.com/questions/2899073/basicoptional-false-vs-columnnullable-false-in-jpa
    @Basic(optional = false)
    @NotNull
    private Integer id;

    @Basic(optional = false)
    //@NotNull 属于JSR303验证框架， @Column(nullable)只用于指示数据库的定义
    @NotNull
    @Size(min = 1, max = 45)
    private String metaKey;

    @Size(max = 255)
    private String metaValue;

    @JoinColumn(name = "comment_id", referencedColumnName = "id")
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

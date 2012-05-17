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
    @NamedQuery(name = "Postmeta.findAll", query = "SELECT p FROM Postmeta p"),
    @NamedQuery(name = "Postmeta.findById", query = "SELECT p FROM Postmeta p WHERE p.id = :id"),
    @NamedQuery(name = "Postmeta.findByMetaKey", query = "SELECT p FROM Postmeta p WHERE p.metaKey = :metaKey"),
    @NamedQuery(name = "Postmeta.findByMetaValue", query = "SELECT p FROM Postmeta p WHERE p.metaValue = :metaValue")})
public class Postmeta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
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
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Posts postId;

    public Postmeta() {
    }

    public Postmeta(Integer id) {
        this.id = id;
    }

    public Postmeta(Integer id, String metaKey) {
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

    public Posts getPostId() {
        return postId;
    }

    public void setPostId(Posts postId) {
        this.postId = postId;
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
        if (!(object instanceof Postmeta)) {
            return false;
        }
        Postmeta other = (Postmeta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cherrot.govproject.model.Postmeta[ id=" + id + " ]";
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sai
 */
@Entity
@Table(name = "links")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Link.findAll", query = "SELECT l FROM Link l"),
    @NamedQuery(name = "Link.findById", query = "SELECT l FROM Link l WHERE l.id = :id"),
    @NamedQuery(name = "Link.findByUrl", query = "SELECT l FROM Link l WHERE l.url = :url"),
    @NamedQuery(name = "Link.findByName", query = "SELECT l FROM Link l WHERE l.name = :name"),
    @NamedQuery(name = "Link.findByTarget", query = "SELECT l FROM Link l WHERE l.target = :target"),
    @NamedQuery(name = "Link.findByDescription", query = "SELECT l FROM Link l WHERE l.description = :description")})
public class Link implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum LinkTarget {

        _blank("新页面打开"), _self("当前框架打开"), _parent("父框架中打开"), _top("当前窗口打开");
        private String description;

        private LinkTarget(String description) {
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
    @Size(min = 1, max = 255)
    @Column(name = "url", nullable = false, length = 255)
    private String url;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    @NotNull
//    @Size(min = 1, max = 20)
    @Column(name = "target", nullable = false, length = 20)
    private LinkTarget target;
    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private LinkCategory linkCategory;

    public Link() {
        target = LinkTarget._blank;
    }

//    public Link(Integer id) {
//        this.id = id;
//    }
    public Link(/*Integer id,*/String url, String name, LinkTarget target) {
//        this.id = id;
        this.url = url;
        this.name = name;
        this.target = target;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkTarget getTarget() {
        return target;
    }

    public void setTarget(LinkTarget target) {
        this.target = target;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LinkCategory getLinkCategory() {
        return linkCategory;
    }

    public void setLinkCategory(LinkCategory linkCategory) {
        this.linkCategory = linkCategory;
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
        if (!(object instanceof Link)) {
            return false;
        }
        Link other = (Link) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cherrot.govproject.model.Link[ id=" + id + " ]";
    }
}

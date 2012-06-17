/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Cherrot Luo<cherrot+dev@cherrot.com>
 */
@Entity
@Table(name = "tags", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"slug"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tag.findAll", query = "SELECT t FROM Tag t"),
    @NamedQuery(name = "Tag.findById", query = "SELECT t FROM Tag t WHERE t.id = :id"),
    @NamedQuery(name = "Tag.findByCount", query = "SELECT t FROM Tag t WHERE t.count = :count"),
    @NamedQuery(name = "Tag.findByName", query = "SELECT t FROM Tag t WHERE t.name = :name"),
    @NamedQuery(name = "Tag.findBySlug", query = "SELECT t FROM Tag t WHERE t.slug = :slug"),
    @NamedQuery(name = "Tag.findByDescription", query = "SELECT t FROM Tag t WHERE t.description = :description"),
})
public class Tag implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
//    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "count", nullable = false)
    private int count;
    @Basic(optional=false)
    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "slug", nullable = false, length = 200)
    private String slug;
    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;
    /**
     * TODO: orderColum 貌似还不被hibernate支持。 是否是hibernate版本问题？ （异常栈定位到Spring的Hibernate3而不是Hibernate4）
     *
     * If you choose to map the relationship in both directions, then one
     * direction must be defined as the owner and the other must use
     * the mappedBy attribute to define its mapping.
     * This also avoids having to duplicate the JoinTable information in both places.
     * Post.java为主控端， Category.java为被控端。因此将mappedBy定义在Term.java。这样，修改删除post会自动修改删除关系。
     */
//    @OrderColumn(name="termOrder")
    @ManyToMany(cascade={CascadeType.DETACH, CascadeType.REFRESH}, mappedBy = "tagList")
    private List<Post> postList;

    public Tag() {
        count = 0;
    }

    public Tag(int count, String name, String slug) {
        this.count = count;
        this.name = name;
        this.slug = slug;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
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
        if (!(object instanceof Tag)) {
            return false;
        }
        Tag other = (Tag) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cherrot.govproject.model.Tag[ id=" + id + " ]";
    }

}

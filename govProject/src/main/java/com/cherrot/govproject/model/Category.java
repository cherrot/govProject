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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(name = "categories", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"slug"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Category.findAll", query = "SELECT c FROM Category c"),
    @NamedQuery(name = "Category.findById", query = "SELECT c FROM Category c WHERE c.id = :id"),
    @NamedQuery(name = "Category.findByCount", query = "SELECT c FROM Category c WHERE c.count = :count"),
    @NamedQuery(name = "Category.findByName", query = "SELECT c FROM Category c WHERE c.name = :name"),
    @NamedQuery(name = "Category.findBySlug", query = "SELECT c FROM Category c WHERE c.slug = :slug"),
    @NamedQuery(name = "Category.findByDescription", query = "SELECT c FROM Category c WHERE c.description = :description"),
    @NamedQuery(name = "Category.findAllHavingNullParent", query = "SELECT c FROM Category c WHERE c.categoryParent IS NULL"),
    @NamedQuery(name = "Category.findAllHavingTopLevelParent", query = "SELECT c FROM Category c WHERE c.categoryParent IN (SELECT c FROM Category c WHERE c.categoryParent IS NULL)")
})
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

//    private void processSlug(String slug) {
//        slug = slug.replaceAll("(\\.|\\s)+", "-");
//        try {
//            this.slug = URLEncoder.encode(slug, "UTF-8");
//        }
//        catch (UnsupportedEncodingException ex) {
//        }
//    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
//    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;
    /**
     * @deprecated 已经在DAO中实现了对count的一致性操作，但不推荐使用该字段。推荐使用PostService中定义的getCount方法。
     */
    @Deprecated
    @Basic(optional = false)
    @NotNull
    @Column(name = "postCount", nullable = false)
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
    @OneToMany(mappedBy = "categoryParent")
    private List<Category> categoryList;
    @JoinColumn(name = "category_parent", referencedColumnName = "id")
    @ManyToOne
    private Category categoryParent;
    /**
     * TODO: orderColum 貌似还不被hibernate支持。 是否是hibernate版本问题？ （异常栈定位到Spring的Hibernate3而不是Hibernate4）
     * If you choose to map the relationship in both directions, then one
     * direction must be defined as the owner and the other must use
     * the mappedBy attribute to define its mapping.
     * This also avoids having to duplicate the JoinTable information in both places.
     * Post.java为主控端， Category.java为被控端。因此将mappedBy定义在Term.java。这样，修改删除post会自动修改删除关系。
     */
//    @OrderColumn(name="termOrder")
    @ManyToMany(cascade={CascadeType.DETACH, CascadeType.REFRESH}, mappedBy = "categoryList")
    private List<Post> postList;

    public Category() {
        count = 0;
//        processLists();
    }

//    public Category(Integer id) {
//        this.id = id;
//    }

    public Category(/*Integer id,*/ int count, String name, String slug) {
//        this.id = id;
        this.count = count;
        this.name = name;
        this.slug = slug;
//        processLists();
    }

    /**
     * 在创建实体时实例化实体中的所有集合对象。否则会在DAO层出现空指针异常（Netbeans自动生成JPA控制器的BUG）
     */
//    private void processLists() {
//        categoryList = new ArrayList<Category>();
//        postList = new ArrayList<Post>();
//    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @deprecated 已经在DAO中实现了对count的一致性操作，但不推荐使用该字段。推荐使用PostService中定义的getCount方法。
     */
    @Deprecated
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

    /**
     * Set the "slug" (i.e. the RESTful URL) of this term
     * 该记录的URI短链接。
     * @param slug
     */
    public void setSlug(String slug) {
//        processSlug(slug);
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<Category> getCategoryList() {
//        if (termList == null) termList = new ArrayList<Term>();
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public Category getCategoryParent() {
        return categoryParent;
    }

    public void setCategoryParent(Category categoryParent) {
        this.categoryParent = categoryParent;
    }

    @XmlTransient
    public List<Post> getPostList() {
//        if (postList == null) postList = new ArrayList<Post>();
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

    /**
     * Warning - this method won't work in the case the id fields are not set
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cherrot.govproject.model.Term[ id=" + id + " ]";
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@Table(name = "terms", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"slug"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Term.findAll", query = "SELECT t FROM Term t"),
    @NamedQuery(name = "Term.findById", query = "SELECT t FROM Term t WHERE t.id = :id"),
    @NamedQuery(name = "Term.findByCount", query = "SELECT t FROM Term t WHERE t.count = :count"),
    @NamedQuery(name = "Term.findByType", query = "SELECT t FROM Term t WHERE t.type = :type"),
    @NamedQuery(name = "Term.findByName", query = "SELECT t FROM Term t WHERE t.name = :name"),
    @NamedQuery(name = "Term.findByNameAndType", query = "SELECT t FROM Term t WHERE t.name = :name AND t.type = :type"),
    @NamedQuery(name = "Term.findBySlug", query = "SELECT t FROM Term t WHERE t.slug = :slug"),
    @NamedQuery(name = "Term.findByDescription", query = "SELECT t FROM Term t WHERE t.description = :description")})
public class Term implements Serializable {
    private static final long serialVersionUID = 1L;

    private void processSlug(String slug) {
        slug = slug.replaceAll("(\\.|\\s)+", "-");
        try {
            this.slug = URLEncoder.encode(slug, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Term.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public enum TermType {
        POST_TAG, CATEGORY
    }

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
    @Enumerated(EnumType.STRING)
    @Size(max = 8)
    @Column(name = "type", length = 8)
    private TermType type;
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
    @OneToMany(mappedBy = "termParent")
    private List<Term> termList;
    @JoinColumn(name = "term_parent", referencedColumnName = "id")
    @ManyToOne
    private Term termParent;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "term")
    private List<TermRelationship> termRelationshipList;

    public Term() {
    }

    public Term(Integer id) {
        this.id = id;
    }

    public Term(Integer id, int count, String slug) {
        this.id = id;
        this.count = count;
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

    public TermType getType() {
        return type;
    }

    public void setType(TermType type) {
        this.type = type;
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
     * FIXME slug may confilics when two terms have the same name.
     * @param slug
     */
    public void setSlug(String slug) {
        processSlug(slug);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<Term> getTermList() {
        return termList;
    }

    public void setTermList(List<Term> termList) {
        this.termList = termList;
    }

    public Term getTermParent() {
        return termParent;
    }

    public void setTermParent(Term termParent) {
        this.termParent = termParent;
    }

    @XmlTransient
    public List<TermRelationship> getTermRelationshipList() {
        return termRelationshipList;
    }

    public void setTermRelationshipList(List<TermRelationship> termRelationshipList) {
        this.termRelationshipList = termRelationshipList;
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
        if (!(object instanceof Term)) {
            return false;
        }
        Term other = (Term) object;
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

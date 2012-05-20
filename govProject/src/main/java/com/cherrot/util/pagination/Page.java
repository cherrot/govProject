/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.util.pagination;

import java.util.List;

/**
 * page object that used in pagination of items.
 *
 * @since 1.0
 * @author cherrot
 */
public class Page<Model> {
    /**
     * Dose this page has pre page.
     */
    private boolean hasPre;
    /**
     * Dose this page ha next page
     */
    private boolean hasNext;
    /**
     * items list include in this page.
     */
    private List<Model> items;
    /**
     * The page number of this page, start at 1.
     * e.g. If a page is the 2nd page, the pageNumber is 2.
     * By convension "number" is 1-indexed, and "index" is 0-indexed.
     */
    private int pageNum;
    /**
     * The PageContext object of this page.
     * @see PageContext
     */
    private PageContext<Model> context;


    public boolean isHasPre() {
        return hasPre;
    }

    public void setHasPre(boolean hasPre) {
        this.hasPre = hasPre;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<Model> getItems() {
        return items;
    }

    public void setItems(List<Model> items) {
        this.items = items;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public PageContext<Model> getContext() {
        return context;
    }

    public void setContext(PageContext<Model> context) {
        this.context = context;
    }
}

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
public class Page<Entity> {
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
    private List<Entity> items;
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
    private PageContext<Entity> context;
}

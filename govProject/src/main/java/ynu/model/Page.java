/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ynu.model;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author LaiWenGen
 */
public class Page {
        private static int DEFAULT_PAGE_SIZE = 20;
        private int pageSize = DEFAULT_PAGE_SIZE;
        private long start;
        private List data;
        private long totalCount;

        public Page(){
                this(0,0,DEFAULT_PAGE_SIZE,new ArrayList());
        }

        public Page(long start, long totalSize, int pageSize, List data){
                this.pageSize = pageSize;
                this.start = start;
                this.totalCount = totalSize;
                this.data = data;
        }
        
        public void setPageSize(int pageSize){
                this.pageSize = pageSize;
        }
        
        public void setStart(long start){
                this.start = start;
        }
                
        public void setData(List data){
                this.data = data;
        }       
        
        public void setTotalCount(long totalCount){
                this.totalCount = totalCount;
        }
        
        public long getTotalPageCount() {
                if(totalCount%pageSize == 0)
                        return totalCount/pageSize;
                else
                        return totalCount/pageSize + 1;
        }

        public long getCurrentPageNo(){
                return start/pageSize + 1;
        }

        public boolean isHasNextPage(){
                return this.getCurrentPageNo() < this.getTotalPageCount();
        }

        public boolean isHasPreviousPage(){
                return this.getCurrentPageNo() > 1;
        }

        protected static int getStartOfPage(int pageNo){
                return getStartOfPage(pageNo, DEFAULT_PAGE_SIZE);
        }

        public static int getStartOfPage(int pageNo, int pageSize) {
                return (pageNo - 1)*pageSize;
        }
    
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.dao;
import java.io.Serializable;
import java.util.List;
/**
 * TODO Unsupported yet!
 * @author cherrot
 */
public interface BaseDao<Model extends Serializable, PrimaryKey extends Serializable> {

    public void save(Model model);// 保存模型对象
    public void saveOrUpdate(Model model);// 保存或更新模型对象
    public void update(Model model);// 更新模型对象
    public void merge(Model model);// 合并模型对象状态到底层会话
    public void delete(PrimaryKey id);// 根据主键删除模型对象
    public Model get(PrimaryKey id);// 根据主键获取模型对象
    public int countAll();//统计模型对象对应数据库表中的记录数
    public List<Model> findAll();//查询所有模型对象
    public List<Model> findAll(int pn, int pageSize);// 分页获取所有模型对象
}

package org.mybatis.gii.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MAPPER接口基类。
 * @param <T>
 */
public interface Mapper<T> {
    /**
     * 查询一条记录。
     *
     * @param t
     * @return
     */
    T queryOne(T t);

    /**
     * 查询所有记录。
     *
     * @return
     */
    List<T> queryAll();

    /**
     * 查询一组记录。
     *
     * @param page
     * @param rows
     * @return
     */
//    List<T> queryAny(@Param("page") int page, @Param("rows") int rows);

    /**
     * 增加。
     *
     * @param t
     */
    void insert(T t);

    /**
     * 删除。
     *
     * @param t
     */
    void delete(T t);

    /**
     * 更新。
     *
     * @param t
     */
    void update(T t);
}

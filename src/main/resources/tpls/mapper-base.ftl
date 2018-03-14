package ${packageName};

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MAPPER接口基类。
 * @param <T>
 */
public interface BaseMapper<T> {
    /**
     * 查询一条记录。
     *
     * @param id
     * @return
     */
    T getOne(@Param("id") Integer id);

    /**
     * 查询所有记录。
     *
     * @return
     */
    List<T> getAll();

    /**
     * 增加。
     *
     * @param t
     */
    void insert(T t);

    /**
     * 删除。
     *
     * @param id
     */
    void delete(@Param("id") Integer id);

    /**
     * 更新。
     *
     * @param t
     */
    void update(T t);
}

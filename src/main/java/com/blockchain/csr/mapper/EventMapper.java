package com.blockchain.csr.mapper;

import com.blockchain.csr.model.entity.Event;
import org.apache.ibatis.annotations.Mapper;

/**
 * The interface Event mapper.
 *
 * @author zhangrucheng on 2025/5/19
 */

@Mapper
public interface EventMapper {
    /**
     * Delete by primary key int.
     *
     * @param id the id
     * @return the int
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * Insert int.
     *
     * @param record the record
     * @return the int
     */
    int insert(Event record);

    /**
     * Insert selective int.
     *
     * @param record the record
     * @return the int
     */
    int insertSelective(Event record);

    /**
     * Select by primary key event.
     *
     * @param id the id
     * @return the event
     */
    Event selectByPrimaryKey(Integer id);

    /**
     * Update by primary key selective int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKeySelective(Event record);

    /**
     * Update by primary key int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKey(Event record);
}
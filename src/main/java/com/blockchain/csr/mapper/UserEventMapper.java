package com.blockchain.csr.mapper;

import com.blockchain.csr.model.entity.UserEvent;
import org.apache.ibatis.annotations.Mapper;

/**
 * The interface User event mapper.
 *
 * @author zhangrucheng on 2025/5/19
 */

@Mapper
public interface UserEventMapper {
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
    int insert(UserEvent record);

    /**
     * Insert selective int.
     *
     * @param record the record
     * @return the int
     */
    int insertSelective(UserEvent record);

    /**
     * Select by primary key user event.
     *
     * @param id the id
     * @return the user event
     */
    UserEvent selectByPrimaryKey(Integer id);

    /**
     * Update by primary key selective int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKeySelective(UserEvent record);

    /**
     * Update by primary key int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKey(UserEvent record);
}
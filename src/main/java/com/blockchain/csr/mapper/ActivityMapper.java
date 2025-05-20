package com.blockchain.csr.mapper;

import com.blockchain.csr.model.entity.Activity;

/**
 * The interface Activity mapper.
 *
 * @author zhangrucheng on 2025/5/19
 */
public interface ActivityMapper {
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
    int insert(Activity record);

    /**
     * Insert selective int.
     *
     * @param record the record
     * @return the int
     */
    int insertSelective(Activity record);

    /**
     * Select by primary key activity.
     *
     * @param id the id
     * @return the activity
     */
    Activity selectByPrimaryKey(Integer id);

    /**
     * Update by primary key selective int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKeySelective(Activity record);

    /**
     * Update by primary key int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKey(Activity record);
}
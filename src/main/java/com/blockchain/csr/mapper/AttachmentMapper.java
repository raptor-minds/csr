package com.blockchain.csr.mapper;

import com.blockchain.csr.model.entity.Attachment;
import org.apache.ibatis.annotations.Mapper;

/**
 * The interface Attachment mapper.
 *
 * @author zhangrucheng on 2025/5/19
 */

@Mapper
public interface AttachmentMapper {
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
    int insert(Attachment record);

    /**
     * Insert selective int.
     *
     * @param record the record
     * @return the int
     */
    int insertSelective(Attachment record);

    /**
     * Select by primary key attachment.
     *
     * @param id the id
     * @return the attachment
     */
    Attachment selectByPrimaryKey(Integer id);

    /**
     * Update by primary key selective int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKeySelective(Attachment record);

    /**
     * Update by primary key int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKey(Attachment record);
}
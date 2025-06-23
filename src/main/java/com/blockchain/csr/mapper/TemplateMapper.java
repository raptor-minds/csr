package com.blockchain.csr.mapper;

import com.blockchain.csr.model.entity.Template;
import org.apache.ibatis.annotations.Mapper;

/**
 * The interface Template mapper.
 *
 * @author zhangrucheng on 2025/5/19
 */

@Mapper
public interface TemplateMapper {
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
    int insert(Template record);

    /**
     * Insert selective int.
     *
     * @param record the record
     * @return the int
     */
    int insertSelective(Template record);

    /**
     * Select by primary key template.
     *
     * @param id the id
     * @return the template
     */
    Template selectByPrimaryKey(Integer id);

    /**
     * Update by primary key selective int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKeySelective(Template record);

    /**
     * Update by primary key int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKey(Template record);
}
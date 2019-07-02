package com.xmcc.mll_product.mapper;

import com.xmcc.mll_common.rabbit.MllMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MllMessageMapper {

    int updateConsumerAndStatus(@Param("messageId") String messageId,@Param("status") int status);

    void updateConsumerAndStatusSuccess(@Param("messageId") String messageId,@Param("status") int status);

    int updateStatusByConsumerStatus(@Param("messageId") String messageId,@Param("status") int status);

}

package com.xmcc.mll_order.mapper;

import com.xmcc.mll_common.rabbit.MllMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


@Mapper
public interface MllMessageMapper {

    // 插入消息
    int insert(MllMessage mllMessage);

    MllMessage queryById(@Param("id") long id);

    void updateSendCount(@Param("id") Long id, @Param("expire") Date expire, @Param("status") int status);

    void updateStatusById(@Param("id") Long id, @Param("status") int status);

    List<MllMessage> queryByStatus(@Param("status") int status);


}

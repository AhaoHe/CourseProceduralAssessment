package com.study.project4.com.dao;

import com.study.project4.com.entity.Admin;
import org.apache.ibatis.annotations.Select;

//必须设置@Mapper在cProject4Application中设置@MapperScan扫描mapper文件
//在Mapper文件中用注解写SQL语句

public interface AdminMapper {

    @Select("select * from Admin where aid = #{aid}")
    public Admin getAdminByid(Integer id);

}

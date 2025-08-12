package com.web.kokoro.backend.mapper;
import com.web.kokoro.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface UserMapper {
    //查询所有用户数据
    @Select("select id, name, age, gender, phone from user")
    public List<User> list();

}
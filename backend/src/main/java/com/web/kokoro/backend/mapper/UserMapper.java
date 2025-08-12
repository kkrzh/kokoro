package com.web.kokoro.backend.mapper;
import com.web.kokoro.backend.core.user.RegisterRequest;
import com.web.kokoro.backend.pojo.UserEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface UserMapper {
    //查询所有用户数据
    @Select("select id, username, password, name from user")
    List<UserEntity> list();

    @Insert("insert into user (username, password,name) values (#{username}, #{password}, #{name})")
    void insert(UserEntity body);

    @Delete("delete from user where id = #{id}")
    void delete(Integer id);

    @Select("select id, username, password, name " +
            "from user " +
            "where username=#{username}")
    public UserEntity getByUsernameAndPassword(RegisterRequest body);
}
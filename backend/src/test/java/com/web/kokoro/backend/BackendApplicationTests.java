package com.web.kokoro.backend;

import com.web.kokoro.backend.mapper.UserMapper;
import com.web.kokoro.backend.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testList() {
        List<User> userList = userMapper.list();
        for (User user : userList) {
            System.out.println(user);
        }
    }

    @Test
    public void test1() {
        String url = "jdbc:mysql://localhost:3306/db01";
        String user = "root";
        String password = "1320";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("连接成功!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

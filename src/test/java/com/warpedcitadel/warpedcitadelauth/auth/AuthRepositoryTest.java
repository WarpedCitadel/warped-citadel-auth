package com.warpedcitadel.warpedcitadelauth.auth;

import com.warpedcitadel.warpedcitadelauth.auth.model.AuthModel;
import com.warpedcitadel.warpedcitadelauth.auth.model.UserModel;
import com.warpedcitadel.warpedcitadelauth.util.SQLFileReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthRepositoryTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private AuthRepository authRepository;

    private SQLFileReader loadSQL = new SQLFileReader();


    @Test
    void _test_createAppUser() throws SQLException {

        String insertSql = loadSQL.loadSQL("/auth/insert--create_app_user.sql");

        UserModel userModel = new UserModel(
                "JohnBlanche",
                "$2a$10$8Hdtn/Ih2Pjd1V5780RVHe8NOLnZZFdjOyk1kax8CpDFHInsDG7A6",
                "johnblanche@gmail.com",
                "32aa8760-2431-43f7-8993-5278dd478032",
                "546321");

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(insertSql)).thenReturn(preparedStatement);

        authRepository.createAppUser(userModel);

        verify(preparedStatement).setString(1, userModel.getUsername());
        verify(preparedStatement).setString(2, userModel.getPasswordHash());
        verify(preparedStatement).setString(3, userModel.getEmail());
        verify(preparedStatement).setString(4, userModel.getToken());
        verify(preparedStatement).setString(5, userModel.getPasscode());
        verify(preparedStatement).execute();
    }


    @Test
    void _test_authenticateUser() throws SQLException {

        String selectSql = loadSQL.loadSQL("/auth/select--get_app_user_details.sql");

        UserModel userModel = new UserModel(
                "JohnBlanche"
        );

        AuthModel validUser = new AuthModel(
                "019ea371-9498-7cb1-b4b9-4ee3db8dc132",
                "JohnBlanche",
                "$2a$10$8Hdtn/Ih2Pjd1V5780RVHe8NOLnZZFdjOyk1kax8CpDFHInsDG7A6",
                "user",
                true,
                true
        );

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(selectSql)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next())
                .thenReturn(true, false);
        when(resultSet.getString("user_uuid"))
                .thenReturn("019ea371-9498-7cb1-b4b9-4ee3db8dc132");
        when(resultSet.getString("username"))
                .thenReturn("JohnBlanche");
        when(resultSet.getString("password_hash"))
                .thenReturn("$2a$10$8Hdtn/Ih2Pjd1V5780RVHe8NOLnZZFdjOyk1kax8CpDFHInsDG7A6");
        when(resultSet.getString("role_type"))
                .thenReturn("user");

        AuthModel mockDbUser = authRepository.loginAppUser(userModel.getUsername());

        verify(preparedStatement).setString(1, userModel.getUsername());
        verify(preparedStatement).executeQuery();

        Assertions.assertEquals(validUser.getUuid(), mockDbUser.getUuid());
        Assertions.assertEquals(validUser.getUsername(), mockDbUser.getUsername());
        Assertions.assertEquals(validUser.getPasswordHash(), mockDbUser.getPasswordHash());
        Assertions.assertEquals(validUser.getRole(), mockDbUser.getRole());
    }
}

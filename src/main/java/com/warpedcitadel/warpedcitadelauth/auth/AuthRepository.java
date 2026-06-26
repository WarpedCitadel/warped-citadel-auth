package com.warpedcitadel.warpedcitadelauth.auth;

import com.warpedcitadel.warpedcitadelauth.auth.dto.VerificationTokenDto;
import com.warpedcitadel.warpedcitadelauth.auth.model.AuthModel;
import com.warpedcitadel.warpedcitadelauth.auth.model.EmailVerificationModel;
import com.warpedcitadel.warpedcitadelauth.auth.model.UserDetailsModel;
import com.warpedcitadel.warpedcitadelauth.auth.model.UserModel;
import com.warpedcitadel.warpedcitadelauth.util.SQLFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Repository
public class AuthRepository {

    @Autowired
    private DataSource database;

    SQLFileReader loadSQL = new SQLFileReader();


    public AuthModel loginAppUser(String username) {

        String selectSql = loadSQL.loadSQL("/auth/select--get_app_user_details.sql");

        try (Connection connection = database.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {

            selectStatement.setString(1, username);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {

                AuthModel dbUser = new AuthModel(
                        resultSet.getString("user_uuid"),
                        resultSet.getString("username"),
                        resultSet.getString("password_hash"),
                        resultSet.getString("role_type"),
                        resultSet.getBoolean("isactive"),
                        resultSet.getBoolean("isverified")
                );

                return dbUser;

            } else {

                throw new IllegalArgumentException("Invalid username or password");
            }
        } catch (SQLException exception) {

            throw new RuntimeException("Failed to login user");
        }
    }


    public void createAppUser(UserModel user) {

        String insertSql = loadSQL.loadSQL("/auth/insert--create_app_user.sql");

        try (Connection connection = database.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {

            insertStatement.setString(1, user.getUsername());
            insertStatement.setString(2, user.getPasswordHash());
            insertStatement.setString(3, user.getEmail());
            insertStatement.setString(4, user.getToken());
            insertStatement.setString(5, user.getPasscode());

            insertStatement.execute();

        } catch (SQLException exception) {
            throw new RuntimeException("Failed to create app user");
        }
    }


    public void verifyEnableUser(long appUserId) {

        String updateSql = loadSQL.loadSQL("/auth/update--update_app_user_verify.sql");

        try (Connection connection = database.getConnection();

             PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {

            updateStatement.setLong(1, appUserId);

            updateStatement.executeUpdate();

        } catch (SQLException exception) {

            throw new RuntimeException("Could not update user verification");
        }
    }


    public void updateTokenStatus(String uuid){

        String updateSQL = loadSQL.loadSQL("/auth/update--update_token_isused.sql");

        try (Connection connection = database.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {

            updateStatement.setString(1, uuid);
            updateStatement.execute();

        } catch (SQLException exception) {
            throw new RuntimeException("Failed to change token status of token id of : " + uuid, exception);
        }
    }


    public EmailVerificationModel emailVerificationToken(VerificationTokenDto tokenDto) {

        String selectSql = loadSQL.loadSQL("/auth/select--get_generated_token.sql");

        try (Connection connection = database.getConnection();

             PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {

            selectStatement.setString(1, tokenDto.token());
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {

                EmailVerificationModel emailVerification = new EmailVerificationModel(
                        resultSet.getLong("id"),
                        resultSet.getString("token"),
                        resultSet.getString("passcode"),
                        resultSet.getBoolean("isused")
                );
                return emailVerification;
            } else {
                throw new IllegalArgumentException("Token is expired");
            }

        } catch (SQLException exception) {
            throw new RuntimeException("Failed to retrieve user token");
        }
    }


    public String createNewEmailToken(EmailVerificationModel user) {

        String insertSql = loadSQL.loadSQL("/auth/insert--create_email_token.sql");

        try (Connection connection = database.getConnection();

             PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {

            insertStatement.setString(1, user.getEmail());
            insertStatement.setString(2, user.getToken());
            insertStatement.setString(3, user.getPasscode());
            ResultSet resultSet = insertStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("username");
            } else {
                throw new IllegalArgumentException("Could not return associated user");
            }

        } catch (SQLException exception) {
            throw new RuntimeException("Failed to create user token");
        }
    }


    public UserDetailsModel authenticateUser(String username) {

        String selectSql = loadSQL.loadSQL("/auth/select--authenticate_app_user.sql");

        try (Connection connection = database.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {

            selectStatement.setString(1, username);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                UserDetailsModel user = new UserDetailsModel(
                        resultSet.getString("user_uuid"),
                        resultSet.getString("username"),
                        resultSet.getString("role_type")
                );

                return user;

            } else {

                throw new IllegalArgumentException("database retrieval error");
            }
        } catch (SQLException exception) {

            throw new RuntimeException("Failed to authenticate user");
        }
    }
}
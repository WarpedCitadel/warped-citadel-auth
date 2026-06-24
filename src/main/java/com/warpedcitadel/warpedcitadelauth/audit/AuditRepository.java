package com.warpedcitadel.warpedcitadelauth.audit;

import com.warpedcitadel.warpedcitadelauth.util.SQLFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AuditRepository {

    @Autowired
    private DataSource database;

    SQLFileReader loadSQL = new SQLFileReader();


    public void updateLastActiveDtm(String uuid){

        String updateSQL = loadSQL.loadSQL("/audit/insert--update_last_active_dtm.sql");

        try (Connection connection = database.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {

            updateStatement.setString(1, uuid);
            updateStatement.execute();

        } catch (SQLException exception) {
            throw new RuntimeException("failed to log user session");
        }
    }

    public List<String> getAppUserSessions(String uuid) {

        String selectSQL = loadSQL.loadSQL("/audit/select--get_app_user_sessions.sql");

        List<String> userSessions = new ArrayList<>();

        try (Connection connection = database.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {

            selectStatement.setString(1, uuid);

            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {

                String isSession = resultSet.getString(1);
                if (isSession == null) {
                    continue;
                }

                String session = resultSet.getString("lastactive_dtm");
                userSessions.add(session);
            }

            return userSessions;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to retrieve user session history", exception);
        }
    }
}

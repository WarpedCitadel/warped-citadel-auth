package com.warpedcitadel.warpedcitadelauth.audit;

import com.warpedcitadel.warpedcitadelauth.util.SQLFileReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
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
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditRepositoryTest {


    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private SQLFileReader loadSQL = new SQLFileReader();

    @InjectMocks
    private AuditRepository auditRepository;


    @Nested
    class positiveTests {

        @Test
        void _test_returnUserSessions() throws SQLException {

            String selectSQL = loadSQL.loadSQL("/audit/select--get_app_user_sessions.sql");

            List<String> mockUserSessions = List.of(
                    "2026-03-12 23:17:37.290907",
                    "2026-03-17 10:43:27.290907",
                    "2026-05-01 06:34:17.290907",
                    "2026-06-23 14:45:37.290907",
                    "2026-07-12 01:54:17.290907",
                    "2026-06-06 08:12:57.290907");

            String validUUID = "019ea371-9498-7cb1-b4b9-4ee3db8dc132";
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(selectSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            when(resultSet.next()).thenReturn(true, true, true, true, true, true, false);

            when(resultSet.getString(1)).thenReturn(
                    "valid_session_1",
                    "valid_session_2",
                    "valid_session_3",
                    "valid_session_4",
                    "valid_session_5",
                    "valid_session_6");

            when(resultSet.getString("lastactive_dtm")).thenReturn(
                    "2026-03-12 23:17:37.290907",
                    "2026-03-17 10:43:27.290907",
                    "2026-05-01 06:34:17.290907",
                    "2026-06-23 14:45:37.290907",
                    "2026-07-12 01:54:17.290907",
                    "2026-06-06 08:12:57.290907");

            List<String> result = auditRepository.getAppUserSessions(validUUID);

            verify(preparedStatement).setString(1, validUUID);
            verify(preparedStatement).executeQuery();
            Assertions.assertNotNull(result);
            Assertions.assertEquals(mockUserSessions, result);
        }


        @Test
        void _test_CheckForNull() throws SQLException {

            String selectSQL = loadSQL.loadSQL("/audit/select--get_app_user_sessions.sql");

            List<String> mockUserSessions = List.of();

            String validUUID = "019ea371-9498-7cb1-b4b9-4ee3db8dc132";
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(selectSQL)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            when(resultSet.next()).thenReturn(true, true, true, false);

            when(resultSet.getString(1)).thenReturn(null, null, null);

            List<String> result = auditRepository.getAppUserSessions(validUUID);

            verify(preparedStatement).setString(1, validUUID);
            verify(preparedStatement).executeQuery();
            Assertions.assertNotNull(result);
            Assertions.assertEquals(mockUserSessions, result);
        }


        @Test
        void _test_updateLastActiveDtm() throws SQLException {

            String updateSQL = loadSQL.loadSQL("/audit/insert--update_last_active_dtm.sql");
            String validUUID = "19ea371-9498-7cb1-b4b9-4ee3db8dc132";

            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(updateSQL)).thenReturn(preparedStatement);

            auditRepository.updateLastActiveDtm(validUUID);

            verify(preparedStatement).setString(1, validUUID);
            verify(preparedStatement).execute();
        }
    }
}
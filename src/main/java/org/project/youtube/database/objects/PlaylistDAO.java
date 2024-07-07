package org.project.youtube.database.objects;

import org.project.youtube.models.ServerResponse;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlaylistDAO
{
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/WeTube-DataBase";
    private static final String USER = "postgres";
    private static final String PASSWORD = "0000";
    private static Connection connection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    //region Existence checking methods
    public static boolean checkUserSubscription (UUID playlistID, UUID userID) {
        System.out.println("> Database: Checking the subscription of user in playlist...");
        String query = "SELECT EXISTS (SELECT 1 FROM playlistSubscription WHERE playlistID = ? AND subscriberID = ? )";
        try (Connection connection = connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, playlistID);
            preparedStatement.setObject(2, userID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("> Database: Subscription checked.");
                    return resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    //endregion

    //region Create accout and setting attributes
    public static void createPlaylist (UUID creatorID, String title, String description, boolean isPublic,
                                       boolean isOnlyComrade) {
        System.out.println("> Database: Creating playlist " + title + " of user " + UserDAO.getUsernameViaID(creatorID)
                + " ...");
        UUID ID = UUID.randomUUID();
        LocalDate creationDate = LocalDate.now();
        String query = "INSERT INTO playlists (ID, creatorID, channelID, title, description, isPublic, isOnlyComrade," +
                " creationDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, ID);
            preparedStatement.setObject(2, creatorID);
            preparedStatement.setObject(3, creatorID);
            preparedStatement.setString(4, title);
            preparedStatement.setString(5, description);
            preparedStatement.setBoolean(6, isPublic);
            preparedStatement.setBoolean(7, isOnlyComrade);
            preparedStatement.setObject(8, creationDate);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: Playlist created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void deletePlaylist (UUID playlistID) {
        System.out.println("> Database: Deleting playlist " + playlistID + " ...");
        ArrayList <String> queries = new ArrayList<>();
        queries.add("DELETE FROM playlists WHERE ID = ?");
        queries.add("DELETE FROM playlistSubscription WHERE playlistID = ?");
        queries.add("DELETE FROM playlistAdmins WHERE playlistID = ?");
        queries.add("DELETE FROM videoInPlayList WHERE playlistID = ?");

        try (Connection connection = connection()) {
            connection.setAutoCommit(false);
            for (String query : queries) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setObject(1, playlistID);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    connection.rollback();
                    System.out.println(e.getMessage());
                }
            }
            connection.commit();
            System.out.println("> Database: Playlist deleted.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void setPublicity (UUID playlistID, boolean isPublic) {
        System.out.println("> Database: Setting publicity of playlist " + playlistID + " ...");
        String query = "UPDATE playlists SET isPublic = ? WHERE ID = ?";
        try (Connection connection = connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            preparedStatement.setBoolean(1, isPublic);
            preparedStatement.setObject(2, playlistID);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: Publicity of playlist set.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void setOnlyComrade (UUID playlistID, boolean isOnlyComrade) {
        System.out.println("> Database: Setting only comrade of playlist " + playlistID + " ...");
        String query = "UPDATE playlists SET isOnlyComrade = ? WHERE ID = ?";
        try (Connection connection = connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            preparedStatement.setBoolean(1, isOnlyComrade);
            preparedStatement.setObject(2, playlistID);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: Only comrade of playlist set.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void setTitle (UUID playlistID, String title) {
        System.out.println("> Database: Setting title of playlist " + playlistID + " ...");
        String query = "UPDATE playlists SET title = ? WHERE ID = ?";
        try (Connection connection = connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, title);
            preparedStatement.setObject(2, playlistID);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: Title of playlist set.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void setDescription (UUID playlistID, String description) {
        System.out.println("> Database: Setting description of playlist " + playlistID + " ...");
        String query = "UPDATE playlists SET description = ? WHERE ID = ?";
        try (Connection connection = connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, description);
            preparedStatement.setObject(2, playlistID);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: Description of playlist set.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void setAdmin (UUID playlistID, UUID adminID) {
        System.out.println("> Database: Adding " + UserDAO.getUsernameViaID(adminID) + " as an admin to play list " +
                playlistID + " ...");
        String query = "INSERT INTO playlistAdmins (playlistID, adminID) VALUES (?, ?)";
        try (Connection connection = connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, playlistID);
            preparedStatement.setObject(2, adminID);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: Admin added.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void disAdmin (UUID playlistID, UUID adminID) {
        System.out.println("> Database: Disadmining " + UserDAO.getUsernameViaID(adminID) + " of play list " +
                playlistID + " ...");
        String query = "DELETE FROM playlistSubscriptions WHERE playlistID = ? AND adminID = ?";
        try (Connection connection = connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, playlistID);
            preparedStatement.setObject(2, adminID);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: User disadmined.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void subscribe (UUID playlistID, UUID subscriberID) {
        System.out.println("> Database: User " + UserDAO.getUsernameViaID(subscriberID) + " subscribing to playlist " +
                playlistID + " ...");
        String query = "INSERT INTO playlistSubscriptions (playlistID, subscriberID) VALUES (?, ?)";
        try (Connection connection = connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, playlistID);
            preparedStatement.setObject(2, subscriberID);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: User subscribed.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void unsubscribe (UUID playlistID, UUID subscriberID) {
        System.out.println("> Database: User " + UserDAO.getUsernameViaID(subscriberID) + " unsubscribing from playlist " +
                playlistID + " ...");
        String query = "DELETE FROM playlistSubscriptions WHERE playlistID = ? AND subscriberID = ?";
        try (Connection connection = connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, playlistID);
            preparedStatement.setObject(2, subscriberID);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: User unsubscribed.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void addVideoToPlaylist (UUID playlistID, UUID videoID) {
        System.out.println("> Database: Adding video to playlist...");
        LocalDate addedDate = LocalDate.now();
        String query = "INSERT INTO videoInPlayList (videoID, playlistID, addedDate) VALUES (?, ?, ?)";
        try (Connection connection = connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, videoID);
            preparedStatement.setObject(2, playlistID);
            preparedStatement.setObject(3, addedDate);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: Video added to playlist.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void deleteVideoOfPlaylist (UUID playlistID, UUID videoID) {
        System.out.println("> Database: Deleting video from playlist... ");
        String query = "DELETE FROM videoInPlayList WHERE playlistID = ? AND videoID = ?";
        try (Connection connection = connection();PreparedStatement preparedStatement = connection.prepareStatement(query);){
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, playlistID);
            preparedStatement.setObject(2, videoID);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: Video deleted of playlist.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //endregion

    //region Getting data from playlist-connected tables
    public static ServerResponse getAllPlaylistData (int requestID, UUID playlistID) {
        System.out.println("> Database: Receiving all data of playlist " + playlistID + " ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id (requestID);
        String query ="SELECT * FROM playlists WHERE ID = ?";
        try(Connection connection = connection();PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setObject(1 , playlistID);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()){
                    serverResponse.add_part("ID" , resultSet.getString("ID"));
                    serverResponse.add_part("creatorID" , resultSet.getString("creatorID"));
                    serverResponse.add_part("title" , resultSet.getString("title"));
                    serverResponse.add_part("description" , resultSet.getString("description"));
                    serverResponse.add_part("isPublic" , resultSet.getString("isPublic"));
                    serverResponse.add_part("isOnlyComrade" , resultSet.getString("isOnlyComrade"));
                    serverResponse.add_part("creationDate" , resultSet.getString("creationDate"));
                }
            }
            System.out.println("> Database: All data received.");
            return serverResponse;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public static int getSubscribersCount (UUID playlistID) {
        String query = "SELECT COUNT(*) AS rowCount FROM playlistSubscriptions WHERE playlistID = ?";
        try (Connection connection = connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1,playlistID);
            try (ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    return resultSet.getInt("rowCount");
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
    public static int getVideosCount (UUID playlistID) {
        String query = "SELECT COUNT(*) AS rowCount FROM videoInPlayList WHERE playlistID = ?";
        try (Connection connection = connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1,playlistID);
            try (ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    return resultSet.getInt("rowCount");
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public static int getVideosCount (UUID playlistID) {
        String query = "SELECT COUNT(*) AS rowCount FROM videoInPlayList WHERE playlistID = ?";
        try (Connection connection = connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1,playlistID);
            try (ResultSet resultSet = preparedStatement.executeQuery();){
                if (resultSet.next()) {
                    return resultSet.getInt("rowCount");
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
    public static List<UUID> getPlaylistsCreatedByUser (UUID userID) {
        System.out.println("> Database: Receiving playlists created by user " + userID + " ...");
        List<UUID> IDs = new ArrayList<>();
        String sql = "SELECT ID FROM playlists WHERE creatorID = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, userID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    UUID playlistId = (UUID) resultSet.getObject("ID");
                    IDs.add(playlistId);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("> Database: Playlists created by user received.");
        return IDs;
    }
    public static List<UUID> getPlaylistVideos (UUID playlistID) {
        System.out.println("> Database: Receiving videos in playlist " + playlistID + " ...");
        List<UUID> IDs = new ArrayList<>();
        String sql = "SELECT ID FROM videoInPlayList WHERE playlistID = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, playlistID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    UUID videoID = (UUID) resultSet.getObject("videoID");
                    IDs.add(videoID);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("> Database: Videos in playlist received.");
        return IDs;
    }
    public static List<UUID> getPlaylistSubscribers (UUID playlistID) {
        System.out.println("> Database: Receiving subscribers of playlist " + playlistID + " ...");
        List<UUID> IDs = new ArrayList<>();
        String sql = "SELECT ID FROM playlistSubscriptions WHERE playlistID = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, playlistID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    UUID subscriberID = (UUID) resultSet.getObject("subscriberID");
                    IDs.add(subscriberID);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("> Database: Subscribers of playlist received.");
        return IDs;
    }

    //endregion
}

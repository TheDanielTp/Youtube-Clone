package org.project.youtube.database;

import java.sql.*;
import java.time.LocalDate;
import java.util.UUID;

public class Database
{
    public static void createDatabase() {
        System.out.println("> Database: Creating wetube database...");
        String query = "CREATE DATABASE wetube;";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/WeTube-DataBase",
                "postgres", "0000"); Statement statement = connection.createStatement())
        {
            statement.executeUpdate(query);
            System.out.println("> Database: Wetube database created.");
        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static void createTables() {
        System.out.println("> Database: Creating database tables...");
        String[] queries = {
                "CREATE TABLE IF NOT EXISTS users (ID UUID PRIMARY KEY, channelID UUID REFERENCES channels(ID)," +
                        " firstname VARCHAR(50), lastname VARCHAR(50), username VARCHAR(50), email VARCHAR(50)," +
                        " password VARCHAR(255), birthdate DATE, joinDate DATE, isPremium BOOLEAN," +
                        " balance NUMERIC(10, 2), profilePicture VARCHAR(512));",
                "CREATE TABLE IF NOT EXISTS channels (ID UUID PRIMARY KEY, userID UUID REFERENCES users(ID)," +
                        " channelName VARCHAR(255), description TEXT, subscribers INTEGER, totalVideos INTEGER," +
                        " totalViews INTEGER, watchTime INTEGER, creationDate DATE, isVerified BOOLEAN," +
                        " outcome NUMERIC(10, 2), channelPicture VARCHAR(512));",
                "CREATE TABLE IF NOT EXISTS categories (ID UUID PRIMARY KEY, title VARCHAR(50));",
                "CREATE TABLE IF NOT EXISTS subscriptions (channelID UUID REFERENCES channels(ID)," +
                        " subscriberID UUID REFERENCES users(ID), subscriptionDate DATE, isOnlyComrade BOOLEAN);",
                "CREATE TABLE IF NOT EXISTS communities (ID UUID PRIMARY KEY, channelID UUID REFERENCES channels(ID));",
                "CREATE TABLE IF NOT EXISTS contents (ID UUID PRIMARY KEY, contentType VARCHAR(50), creatorID UUID REFERENCES users(ID)," +
                        " categoryID UUID REFERENCES category(ID), channelID UUID REFERENCES channels(ID)," +
                        " postedDate TIMESTAMP WITHOUT TIME ZONE, likedCount INTEGER, dislikedCount INTEGER," +
                        " isOnlyComrade BOOLEAN);",
                "CREATE TABLE IF NOT EXISTS playlists (ID UUID PRIMARY KEY, creatorID UUID REFERENCES users(ID)," +
                        " channelID UUID REFERENCES channels(ID), title VARCHAR(100), description TEXT," +
                        " isPublic BOOLEAN, isOnlyComrade BOOLEAN, creationDate DATE);",
                "CREATE TABLE IF NOT EXISTS playlistAdmins (playlistID UUID REFERENCES playlists(ID)," +
                        " adminID UUID REFERENCES users(ID));",
                "CREATE TABLE IF NOT EXISTS playlistSubscriptions (playlistID UUID REFERENCES playlists(ID)," +
                        " subscriberID UUID REFERENCES users(ID), subscriptionDate DATE);",
                "CREATE TABLE IF NOT EXISTS contentAction (ID UUID PRIMARY KEY, contentID UUID REFERENCES contents(ID)," +
                        " userID UUID REFERENCES users(ID), liked BOOLEAN);",
                "CREATE TABLE IF NOT EXISTS notifications (userID UUID REFERENCES users(ID)," +
                        " contentID UUID REFERENCES contents(ID), receivedDate TIMESTAMP WITHOUT TIME ZONE," +
                        " title VARCHAR(100));",
                "CREATE TABLE IF NOT EXISTS posts (ID UUID PRIMARY KEY, contentID UUID REFERENCES contents(ID)," +
                        " creatorID UUID REFERENCES users(ID), communityID UUID REFERENCES community(ID), " +
                        "title VARCHAR(100), description TEXT, imageURL VARCHAR(255), commentCount INTEGER);",
                "CREATE TABLE IF NOT EXISTS comments (ID UUID PRIMARY KEY, contentID UUID REFERENCES contents(ID)," +
                        " commenterID UUID REFERENCES users(ID), postedDate TIMESTAMP WITHOUT TIME ZONE, body TEXT," +
                        " replyCount INTEGER, isReply BOOLEAN, parentCommentID UUID REFERENCES comment(ID));",
                "CREATE TABLE IF NOT EXISTS videos (ID UUID PRIMARY KEY, contentID UUID REFERENCES contents(ID)," +
                        " creatorID UUID REFERENCES users(ID), communityID UUID REFERENCES community(ID)," +
                        " title VARCHAR(100), description TEXT, videoURL VARCHAR(255), thumbnailURL VARCHAR(255)," +
                        " commentCount INTEGER);",
                "CREATE TABLE IF NOT EXISTS commentAction (ID UUID PRIMARY KEY, commentID UUID REFERENCES comment(ID)," +
                        " userID UUID REFERENCES users(ID), liked BOOLEAN);",
                "CREATE TABLE IF NOT EXISTS videoInPlayList (videoID UUID REFERENCES video(ID)," +
                        " playlistID UUID REFERENCES playlist(ID), indexInPlaylist INTEGER," +
                        " addedDate TIMESTAMP WITHOUT TIME ZONE);"
        };
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/WeTube-DataBase",
                "postgres", "0000");Statement statement = connection.createStatement())
        {
            connection.setAutoCommit(false);
            for (String query : queries) {
                statement.executeUpdate(query);
            }
            connection.commit();
            System.out.println("> Database: Wetube database tables created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}

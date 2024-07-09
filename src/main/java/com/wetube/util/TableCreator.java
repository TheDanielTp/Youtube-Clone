package com.wetube.util;

import java.sql.Connection;
import java.sql.Statement;

public class TableCreator
{
    public static void createTables ()
    {
        String createUsersTable = """
                                  CREATE TABLE IF NOT EXISTS Users (
                                      ID UUID PRIMARY KEY,
                                      channelID UUID,
                                      firstName VARCHAR(255),
                                      lastName VARCHAR(255),
                                      username VARCHAR(255) UNIQUE,
                                      email VARCHAR(255) UNIQUE,
                                      password VARCHAR(255),
                                      birthdate DATE,
                                      joinDate DATE,
                                      isPremium BOOLEAN,
                                      balance DOUBLE PRECISION,
                                      profilePictureURL VARCHAR(255)
                                  );
                                  """;

        String createChannelsTable = """
                                     CREATE TABLE IF NOT EXISTS Channels (
                                         ID UUID PRIMARY KEY,
                                         userID UUID REFERENCES Users(ID),
                                         name VARCHAR(255),
                                         description TEXT,
                                         subscribersCount INT DEFAULT 0,
                                         totalVideos INT DEFAULT 0,
                                         totalViews INT DEFAULT 0,
                                         watchTime INT DEFAULT 0,
                                         creationDate DATE,
                                         isVerified BOOLEAN DEFAULT FALSE,
                                         outcome DOUBLE PRECISION DEFAULT 0.0,
                                         channelPictureURL VARCHAR(255)
                                     );
                                     """;

        String createVideosTable = """
                                   CREATE TABLE IF NOT EXISTS Videos (
                                       ID UUID PRIMARY KEY,
                                       creatorID UUID REFERENCES Users(ID),
                                       channelID UUID REFERENCES Channels(ID),
                                       communityID UUID,
                                       title VARCHAR(255),
                                       description TEXT,
                                       dataType VARCHAR(255),
                                       videoURL VARCHAR(255),
                                       thumbnailURL VARCHAR(255),
                                       commentsCount INT DEFAULT 0,
                                       likesCount INT DEFAULT 0,
                                       dislikesCount INT DEFAULT 0,
                                       viewsCount INT DEFAULT 0,
                                       creationDate TIMESTAMP,
                                       isOnlyComrade BOOLEAN
                                   );
                                   """;

        String createPlaylistsTable = """
                                      CREATE TABLE IF NOT EXISTS Playlists (
                                          ID UUID PRIMARY KEY,
                                          creatorID UUID REFERENCES Users(ID),
                                          channelID UUID REFERENCES Channels(ID),
                                          title VARCHAR(255),
                                          description TEXT,
                                          isPublic BOOLEAN,
                                          isOnlyComrade BOOLEAN,
                                          creationDate TIMESTAMP
                                      );
                                      """;

        String createCommentsTable = """
                                     CREATE TABLE IF NOT EXISTS Comments (
                                         ID UUID PRIMARY KEY,
                                         contentID UUID,
                                         creatorID UUID REFERENCES Users(ID),
                                         parentCommentID UUID,
                                         content TEXT,
                                         replyCount INT DEFAULT 0,
                                         creationDate TIMESTAMP,
                                         isReply BOOLEAN,
                                         likesCount INT DEFAULT 0,
                                         dislikesCount INT DEFAULT 0
                                     );
                                     """;

        String createNotificationsTable = """
                                          CREATE TABLE IF NOT EXISTS Notifications (
                                              ID UUID PRIMARY KEY,
                                              userID UUID REFERENCES Users(ID),
                                              contentID UUID,
                                              title VARCHAR(255),
                                              receiveDate TIMESTAMP,
                                              isSeen BOOLEAN
                                          );
                                          """;

        String createCategoriesTable = """
                                       CREATE TABLE IF NOT EXISTS Categories (
                                           ID UUID PRIMARY KEY,
                                           title VARCHAR(255)
                                       );
                                       """;

        String createCommunitiesTable = """
                                        CREATE TABLE IF NOT EXISTS Communities (
                                            ID UUID PRIMARY KEY,
                                            channelID UUID REFERENCES Channels(ID)
                                        );
                                        """;

        String createPostsTable = """
                                  CREATE TABLE IF NOT EXISTS Posts (
                                      ID UUID PRIMARY KEY,
                                      communityID UUID REFERENCES Communities(ID),
                                      creatorID UUID REFERENCES Users(ID),
                                      channelID UUID REFERENCES Channels(ID),
                                      title VARCHAR(255),
                                      description TEXT,
                                      imageURL VARCHAR(255),
                                      commentsCount INT DEFAULT 0,
                                      likesCount INT DEFAULT 0,
                                      dislikesCount INT DEFAULT 0,
                                      creationDate TIMESTAMP,
                                      isOnlyComrade BOOLEAN
                                  );
                                  """;

        String createSubscribersTable = """
                                        CREATE TABLE IF NOT EXISTS Subscribers (
                                            userID UUID REFERENCES Users(ID),
                                            channelID UUID REFERENCES Channels(ID),
                                            PRIMARY KEY (userID, channelID)
                                        );
                                        """;

        String createVideoPlaylistsTable = """
                                           CREATE TABLE IF NOT EXISTS VideoPlaylists (
                                               playlistID UUID REFERENCES Playlists(ID),
                                               videoID UUID REFERENCES Videos(ID),
                                               PRIMARY KEY (playlistID, videoID)
                                           );
                                           """;

        String createContentsActionTable = """
                                     CREATE TABLE IF NOT EXISTS ContentsAction (
                                     contentID UUID,
                                     userID UUID REFERENCES Users(ID),
                                     liked BOOLEAN,
                                     disliked BOOLEAN,
                                     PRIMARY KEY (contentID, userID)
                                     );
                                     """;

        try (Connection conn = DatabaseConnection.getConnection ();
             Statement stmt = conn.createStatement ())
        {
            stmt.execute (createUsersTable);
            stmt.execute (createChannelsTable);
            stmt.execute (createVideosTable);
            stmt.execute (createPlaylistsTable);
            stmt.execute (createCommentsTable);
            stmt.execute (createNotificationsTable);
            stmt.execute (createCategoriesTable);
            stmt.execute (createCommunitiesTable);
            stmt.execute (createPostsTable);
            stmt.execute (createSubscribersTable);
            stmt.execute (createVideoPlaylistsTable);
            stmt.execute (createContentsActionTable);
            System.out.println ("Tables created successfully.");
        }
        catch (Exception e)
        {
            e.printStackTrace ();
        }
    }

    public static void main (String[] args)
    {
        createTables ();
    }
}

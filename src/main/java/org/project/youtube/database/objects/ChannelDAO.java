package org.project.youtube.database.objects;

import org.project.youtube.models.ServerResponse;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChannelDAO
{
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/WeTube-DataBase";
    private static final String USER = "postgres";
    private static final String PASSWORD = "0000";
    private static Connection connection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    //region Create channel and setting attributes
    public static void createChannel (UUID userID) {
        System.out.println("> Database: Creating the channel of user " + userID + " ...");
        UUID ID = userID;
        String channelName = "Your Channel";
        String description = "No Description";
        int subscribers = 0;
        int totalVideos = 0;
        int totalViews = 0;
        int watchTime = 0;
        LocalDate creationDate = LocalDate.now();
        boolean isVerified = false;
        double outcome = 0.00;
        String channelPicture = getRandomChannelPicture();
        String query = "INSERT INTO channels (ID, userID, channelName, description, subscribers, totalVideos," +
                " totalViews, watchTime, creationDate, isVerified, outcome, channelPicture) VALUES" +
                " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connection();PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, ID);
            preparedStatement.setObject(2, userID);
            preparedStatement.setString(3, channelName);
            preparedStatement.setString(1, description);
            preparedStatement.setInt(2, subscribers);
            preparedStatement.setInt(3, totalVideos);
            preparedStatement.setInt(1, totalViews);
            preparedStatement.setInt(2, watchTime);
            preparedStatement.setObject(3, creationDate);
            preparedStatement.setBoolean(1, isVerified);
            preparedStatement.setDouble(2, outcome);
            preparedStatement.setString(3, channelPicture);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: Channel created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void setName (UUID channelID, String channelName) {
        System.out.println("> Database: Setting new name for " + UserDAO.getUsernameViaID(channelID) + "'s channel...");
        String query = "UPDATE channels SET channelName = ? WHERE ID = ?";
        try (Connection connection = connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, channelName);
            preparedStatement.setObject(2, channelID);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: New name set.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void setDescription (UUID channelID, String description) {
        System.out.println("> Database: Setting new description for " + UserDAO.getUsernameViaID(channelID) + "'s channel...");
        String query = "UPDATE channels SET description = ? WHERE ID = ?";
        try (Connection connection = connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, description);
            preparedStatement.setObject(2, channelID);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: New description set.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void setSubscribers (UUID channelID, int subscribers) {
        System.out.println("> Database: Setting new subscribers count for " + UserDAO.getUsernameViaID(channelID) + "'s channel...");
        String query = "UPDATE channels SET subscribers = ? WHERE ID = ?";
        try (Connection connection = connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            connection.setAutoCommit(false);
            preparedStatement.setInt(1, subscribers);
            preparedStatement.setObject(2, channelID);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: New subscribers count set.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void setTotalVideos (UUID channelID, int totalVideos) {
        System.out.println("> Database: Setting new videos count for " + UserDAO.getUsernameViaID(channelID) + "'s channel...");
        String query = "UPDATE channels SET totalVideos = ? WHERE ID = ?";
        try (Connection connection = connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            connection.setAutoCommit(false);
            preparedStatement.setInt(1, totalVideos);
            preparedStatement.setObject(2, channelID);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: New videos count set.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void setTotalViews (UUID channelID, int totalViews) {
        System.out.println("> Database: Setting new view count for " + UserDAO.getUsernameViaID(channelID) + "'s channel...");
        String query = "UPDATE channels SET totalViews = ? WHERE ID = ?";
        try (Connection connection = connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            connection.setAutoCommit(false);
            preparedStatement.setInt(1, totalViews);
            preparedStatement.setObject(2, channelID);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: New view count set.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void setWatchTime (UUID channelID, int watchTime) {
        System.out.println("> Database: Setting new watchTime for " + UserDAO.getUsernameViaID(channelID) + "'s channel...");
        String query = "UPDATE channels SET watchTime = ? WHERE ID = ?";
        try (Connection connection = connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            connection.setAutoCommit(false);
            preparedStatement.setInt(1, watchTime);
            preparedStatement.setObject(2, channelID);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: New watchTime set.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void setVerification (UUID channelID, boolean isVerified) {
        System.out.println("> Database: Setting verification of " + UserDAO.getUsernameViaID(channelID) + "'s channel...");
        String query = "UPDATE channels SET isVerified = ? WHERE ID = ?";
        try (Connection connection = connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            connection.setAutoCommit(false);
            preparedStatement.setBoolean(1, isVerified);
            preparedStatement.setObject(2, channelID);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: Channel's verification set.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void setOutcome (UUID channelID, double outcome) {
        System.out.println("> Database: Setting new outcome for " + UserDAO.getUsernameViaID(channelID) + "'s channel...");
        String query = "UPDATE channels SET outcome = ? WHERE ID = ?";
        try (Connection connection = connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            connection.setAutoCommit(false);
            preparedStatement.setDouble(1, outcome);
            preparedStatement.setObject(2, channelID);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: New outcome set.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void setChannelPicture (UUID channelID, String channelPicture) {
        System.out.println("> Database: Setting new channel picture for " + UserDAO.getUsernameViaID(channelID) + "'s channel...");
        String query = "UPDATE channels SET channelPicture = ? WHERE ID = ?";
        try (Connection connection = connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, channelPicture);
            preparedStatement.setObject(2, channelID);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: New channel picture set.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void subscribe (UUID channelID, UUID subscriberID) {
        System.out.println("> Database: User " + UserDAO.getUsernameViaID(subscriberID) + " subscribing to channel " +
                getNameViaID(channelID) + " ...");
        LocalDate subscriptionDate = LocalDate.now();
        boolean isOnlyComrade = false;
        String query = "INSERT INTO subscriptions (channelID, subscriberID, subscriptionDate, isOnlyComrade) VALUES" +
                " (?, ?, ?, ?)";
        try (Connection connection = connection();PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, channelID);
            preparedStatement.setObject(2, subscriberID);
            preparedStatement.setObject(3, subscriptionDate);
            preparedStatement.setBoolean(4, isOnlyComrade);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: User subscribed.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void unsubscribe (UUID channelID, UUID subscriberID) {
        System.out.println("> Database: User " + UserDAO.getUsernameViaID(subscriberID) + " unsubscribing from" +
                " channel " + getNameViaID(channelID) + " ...");
        String query = "DELETE FROM subscriptions WHERE channelID = ? AND subscriberID = ?";
        try (Connection connection = connection();PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            connection.setAutoCommit(false);
            preparedStatement.setObject(1, channelID);
            preparedStatement.setObject(2, subscriberID);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: User unsubscribed.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void setOnlyComrade (UUID channelID, boolean isOnlyComrade) {
        System.out.println("> Database: Setting " + UserDAO.getUsernameViaID(channelID) + " an only comrade of channel...");
        String query = "UPDATE channels SET isOnlyComrade = ? WHERE ID = ?";
        try (Connection connection = connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {
            connection.setAutoCommit(false);
            preparedStatement.setBoolean(1, isOnlyComrade);
            preparedStatement.setObject(2, channelID);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("> Database: User being an only comrade set.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    //endregion

    //region Checking methods
    public static boolean checkSubscription (UUID channelID, UUID userID) {
        System.out.println("> Database: Checking the subscription of user " +
                UserDAO.getUsernameViaID(userID) + " to channel " + getNameViaID(channelID) + " ...");
        String query = "SELECT EXISTS (SELECT 1 FROM subscriptions WHERE channelID = ? AND subscriberID = ? )";
        try (Connection connection = connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, channelID);
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

    //region Getting data from channel-connected tables
    public static String getNameViaID (UUID ID) {
        String query = "SELECT channelName FROM users WHERE ID = ?";
        try (Connection connection = connection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, ID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("channelName");
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public static ServerResponse getAllChannelData (int requestID, UUID channelID) {
        System.out.println("> Database: Receiving all data of channel " + getNameViaID(channelID) + " ...");
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.setRequest_id(requestID);
        String query ="SELECT * FROM channels WHERE ID = ?";
        try (Connection connection = connection();PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1 , channelID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    serverResponse.add_part("ID" , resultSet.getString("ID"));
                    serverResponse.add_part("userID" , resultSet.getString("userID"));
                    serverResponse.add_part("channelName" , resultSet.getString("channelName"));
                    serverResponse.add_part("description" , resultSet.getString("description"));
                    serverResponse.add_part("subscribers" , resultSet.getString("subscribers"));
                    serverResponse.add_part("totalVideos" , resultSet.getString("totalVideos"));
                    serverResponse.add_part("totalViews" , resultSet.getString("totalViews"));
                    serverResponse.add_part("watchTime" , resultSet.getString("watchTime"));
                    serverResponse.add_part("creationDate" , resultSet.getString("creationDate"));
                    serverResponse.add_part("isVerified" , resultSet.getString("isVerified"));
                    serverResponse.add_part("outcome" , resultSet.getString("outcome"));
                    serverResponse.add_part("channelPicture" , resultSet.getString("channelPicture"));
                }
            }
            System.out.println("> Database: all data received.");
            return serverResponse;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public static List <UUID> getSubscribers (UUID channelID) {
        System.out.println("> Database: Receiving all subscribers of channel " + getNameViaID(channelID) + " ...");
        List <UUID> IDs = new ArrayList <> ();
        String sql = "SELECT subscriberID FROM subscriptions WHERE channelID = ?";
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, channelID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    UUID subscriberId = (UUID) resultSet.getObject("subscriberID");
                    IDs.add(subscriberId);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("> Database: all subscribers received.");
        return IDs;
    }
    //endregion
}

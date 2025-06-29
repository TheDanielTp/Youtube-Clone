package com.wetube.dao.impl;

import com.wetube.model.Community;
import com.wetube.model.Notification;
import com.wetube.model.Playlist;
import com.wetube.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommunityDAOImpl
{
    public UUID generateID ()
    {
        UUID uuid = UUID.randomUUID ();

        List <Community> all = findAll ();
        for (Community object : all)
        {
            if (object.getID () == uuid)
            {
                uuid = generateID ();
                break;
            }
        }
        return uuid;
    }

    public void create (Community community)
    {
        String sql = "INSERT INTO Communities (ID, channelID) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, community.getID ());
            preparedStatement.setObject (2, community.getChannelID ());
            preparedStatement.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void update (Community community)
    {
        String sql = "UPDATE Communities SET channelID = ? WHERE ID = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, community.getChannelID ());
            preparedStatement.setObject (2, community.getID ());
            preparedStatement.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void delete (UUID id)
    {
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ())
        {
            String deleteCommunity = "DELETE FROM Communities WHERE ID = '" + id + "'";
            statement.executeUpdate (deleteCommunity);
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public Community findById (UUID id)
    {
        String sql = "SELECT * FROM Communities WHERE ID = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, id);
            ResultSet  resultSet = preparedStatement.executeQuery ();
            if  (resultSet.next ())
            {
                return new Community (
                         resultSet.getObject ("ID", UUID.class),
                         resultSet.getObject ("channelID", UUID.class)
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return null;
    }

    public List <Community> findAll ()
    {
        List <Community> communities = new ArrayList <> ();
        String      sql   = "SELECT * FROM Communities";
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet  resultSet = statement.executeQuery (sql))
        {
            while  (resultSet.next ())
            {
                communities.add (new Community (
                         resultSet.getObject ("ID", UUID.class),
                         resultSet.getObject ("channelID", UUID.class)
                ));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return communities;
    }
}

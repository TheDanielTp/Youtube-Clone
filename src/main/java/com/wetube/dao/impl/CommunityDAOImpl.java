package com.wetube.dao.impl;

import com.wetube.model.Community;
import com.wetube.model.Playlist;
import com.wetube.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommunityDAOImpl
{
    public void create (Community community)
    {
        String sql = "INSERT INTO Communities (ID, channelID) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, community.getID ());
            pstmt.setObject (2, community.getChannelID ());
            pstmt.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void update (Community community)
    {
        String sql = "UPDATE Communities SET channelID = ? WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, community.getChannelID ());
            pstmt.setObject (2, community.getID ());
            pstmt.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void delete (UUID id)
    {
        try (Connection conn = DatabaseConnection.getConnection ();
             Statement stmt = conn.createStatement ())
        {
            String deleteCommunity = "DELETE FROM Communities WHERE ID = '" + id + "'";
            stmt.executeUpdate (deleteCommunity);
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public Community findById (UUID id)
    {
        String sql = "SELECT * FROM Communities WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, id);
            ResultSet rs = pstmt.executeQuery ();
            if (rs.next ())
            {
                return new Community (
                        rs.getObject ("ID", UUID.class),
                        rs.getObject ("channelID", UUID.class)
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
        try (Connection conn = DatabaseConnection.getConnection ();
             Statement stmt = conn.createStatement ();
             ResultSet rs = stmt.executeQuery (sql))
        {
            while (rs.next ())
            {
                communities.add (new Community (
                        rs.getObject ("ID", UUID.class),
                        rs.getObject ("channelID", UUID.class)
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

package com.wetube.dao.impl;

import com.wetube.model.Category;
import com.wetube.model.Community;
import com.wetube.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoryDAOImpl
{
    public void create (Category category)
    {
        String sql = "INSERT INTO Categories (ID, title) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, category.getID ());
            pstmt.setString (2, category.getTitle ());
            pstmt.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void update (Category category)
    {
        String sql = "UPDATE Categories SET title = ? WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setString (1, category.getTitle ());
            pstmt.setObject (2, category.getID ());
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
            String deleteCategory = "DELETE FROM Categories WHERE ID = '" + id + "'";
            stmt.executeUpdate (deleteCategory);
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public Category findById (UUID id)
    {
        String sql = "SELECT * FROM Categories WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, id);
            ResultSet rs = pstmt.executeQuery ();
            if (rs.next ())
            {
                return new Category (
                        rs.getObject ("ID", UUID.class),
                        rs.getString ("title")
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return null;
    }

    public List <Category> findAll ()
    {
        List <Category> categories = new ArrayList <> ();
        String      sql   = "SELECT * FROM Categories";
        try (Connection conn = DatabaseConnection.getConnection ();
             Statement stmt = conn.createStatement ();
             ResultSet rs = stmt.executeQuery (sql))
        {
            while (rs.next ())
            {
                categories.add (new Category (
                        rs.getObject ("ID", UUID.class),
                        rs.getString ("title")
                ));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return categories;
    }
}

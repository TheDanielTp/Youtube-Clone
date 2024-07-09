package com.wetube.dao.impl;

import com.wetube.model.Category;
import com.wetube.model.Channel;
import com.wetube.model.Community;
import com.wetube.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoryDAOImpl
{
    public UUID generateID ()
    {
        UUID uuid = UUID.randomUUID ();

        List <Category> all = findAll ();
        for (Category object : all)
        {
            if (object.getID () == uuid)
            {
                uuid = generateID ();
            }
        }
        System.out.println ("> Database: ID generated");
        return uuid;
    }

    public void create (Category category)
    {
        String sql = "INSERT INTO Categories (ID, title) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, category.getID ());
            pstmt.setString (2, category.getTitle ());
            pstmt.executeUpdate ();
            System.out.println ("> Database: category created");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to create category");
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
            System.out.println ("> Database: category updated");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to update category");
        }
    }

    public void delete (UUID id)
    {
        try (Connection conn = DatabaseConnection.getConnection ();
             Statement stmt = conn.createStatement ())
        {
            String deleteCategory = "DELETE FROM Categories WHERE ID = '" + id + "'";
            stmt.executeUpdate (deleteCategory);
            System.out.println ("> Database: category deleted");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to delete category");
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
                System.out.println ("> Database: category found");
                return new Category (
                        rs.getObject ("ID", UUID.class),
                        rs.getString ("title")
                );
            }
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to find category by ID");
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
            System.out.println ("> Database: categories found");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to find categories");
        }
        return categories;
    }
}

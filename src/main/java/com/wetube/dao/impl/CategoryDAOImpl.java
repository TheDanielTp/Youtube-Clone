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
                break;
            }
        }
        return uuid;
    }

    public void create (Category category)
    {
        String query = "INSERT INTO Categories (ID, title) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (query))
        {
            preparedStatement.setObject (1, category.getID ());
            preparedStatement.setString (2, category.getTitle ());
            preparedStatement.executeUpdate ();
            System.out.println ("> Database: category created");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to create category");
            System.out.println (e.getMessage ());
        }
    }

    public void update (Category category)
    {
        String query = "UPDATE Categories SET title = ? WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = conn.prepareStatement (query))
        {
            preparedStatement.setString (1, category.getTitle ());
            preparedStatement.setObject (2, category.getID ());
            preparedStatement.executeUpdate ();
            System.out.println ("> Database: category updated");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to update category");
            System.out.println (e.getMessage ());
        }
    }

    public void delete (UUID id)
    {
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ())
        {
            String deleteCategory = "DELETE FROM Categories WHERE ID = '" + id + "'";
            statement.executeUpdate (deleteCategory);
            System.out.println ("> Database: category deleted");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to delete category");
            System.out.println (e.getMessage ());
        }
    }

    public Category findById (UUID id)
    {
        String query = "SELECT * FROM Categories WHERE ID = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (query))
        {
            preparedStatement.setObject (1, id);
            ResultSet resultSet = preparedStatement.executeQuery ();
            if (resultSet.next ())
            {
                System.out.println ("> Database: category found");
                return new Category (
                        resultSet.getObject ("ID", UUID.class),
                        resultSet.getString ("title")
                );
            }
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to find category");
            System.out.println (e.getMessage ());
        }
        return null;
    }

    public Category findByTitle (String title)
    {
        String query = "SELECT * FROM Categories WHERE title = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (query))
        {
            preparedStatement.setObject (1, title);
            ResultSet resultSet = preparedStatement.executeQuery ();
            if (resultSet.next ())
            {
                System.out.println ("> Database: category found");
                return new Category (
                        resultSet.getObject ("ID", UUID.class),
                        resultSet.getString ("title")
                );
            }
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to find category");
            System.out.println (e.getMessage ());
        }
        return null;
    }

    public List <Category> findAll ()
    {
        List <Category> categories = new ArrayList <> ();
        String      query   = "SELECT * FROM Categories";
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet = statement.executeQuery (query))
        {
            while (resultSet.next ())
            {
                categories.add (new Category (
                        resultSet.getObject ("ID", UUID.class),
                        resultSet.getString ("title")
                ));
            }
            System.out.println ("> Database: categories found");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to find categories");
            System.out.println (e.getMessage ());
        }
        return categories;
    }

    public ArrayList <UUID> findAllVideos (UUID id)
    {
        ArrayList <UUID> videosID = new ArrayList <> ();
        String           sql      = "SELECT * FROM Videos";
        try (Connection connection= DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet= statement.executeQuery (sql))
        {
            while (resultSet.next ())
            {
                if (id.equals (resultSet.getObject ("categoryID", UUID.class)))
                {
                    videosID.add (resultSet.getObject ("ID", UUID.class));
                }
            }
            System.out.println ("> Database: category videos found");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to find category videos");
            System.out.println (e.getMessage ());
        }
        return videosID;
    }
}

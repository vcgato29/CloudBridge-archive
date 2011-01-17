package com.cloud.bridge.persist.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.cloud.bridge.util.ConfigurationHelper;

public class BucketPolicyDao {
	public static final Logger logger = Logger.getLogger(BucketPolicyDao.class);

	private Connection conn       = null;
	private String     dbName     = null;
	private String     dbUser     = null;
	private String     dbPassword = null;
	
	public BucketPolicyDao() 
	{
	    File propertiesFile = ConfigurationHelper.findConfigurationFile("ec2-service.properties");
	    Properties EC2Prop = null;
	       
	    if (null != propertiesFile) {
	   	    EC2Prop = new Properties();
	    	try {
				EC2Prop.load( new FileInputStream( propertiesFile ));
			} catch (FileNotFoundException e) {
				logger.warn("Unable to open properties file: " + propertiesFile.getAbsolutePath(), e);
			} catch (IOException e) {
				logger.warn("Unable to read properties file: " + propertiesFile.getAbsolutePath(), e);
			}
		    dbName     = EC2Prop.getProperty( "dbName" );
		    dbUser     = EC2Prop.getProperty( "dbUser" );
		    dbPassword = EC2Prop.getProperty( "dbPassword" );
		}
	}

	public void addPolicy( long sbucketId, String policy ) 
        throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
    {
        PreparedStatement statement = null;

        openConnection();	
        try {            
            statement = conn.prepareStatement ( "INSERT INTO bucket_policies (SBucketID, Policy) VALUES (?,?)" );
            statement.setLong( 1, sbucketId );
            statement.setString( 2, policy );
            int count = statement.executeUpdate();
            statement.close();	

        } finally {
            closeConnection();
        }
    }

	public String getPolicy( long sbucketId ) 
        throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
    {
        PreparedStatement statement = null;
        String policy = null;
	
        openConnection();	
        try {            
	        statement = conn.prepareStatement ( "SELECT Policy FROM bucket_policies WHERE SBucketID=?" );
            statement.setLong( 1, sbucketId );
            ResultSet rs = statement.executeQuery();
	        if (rs.next()) policy = rs.getString( "Policy" );
            statement.close();	
            return policy;
    
        } finally {
            closeConnection();
        }
    }

	public void deletePolicy( long sbucketId )
        throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
    {
        PreparedStatement statement = null;
	
        openConnection();	
        try {
	        statement = conn.prepareStatement ( "DELETE FROM bucket_policies WHERE SBucketID=?" );
            statement.setLong( 1, sbucketId );
            int count = statement.executeUpdate();
            statement.close();	
    
        } finally {
            closeConnection();
        }
    }

	private void openConnection() 
        throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException 
    {
        if (null == conn) {
            Class.forName( "com.mysql.jdbc.Driver" ).newInstance();
            conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/"+dbName, dbUser, dbPassword );
        }
    }

    private void closeConnection() throws SQLException {
        if (null != conn) conn.close();
        conn = null;
    }
}

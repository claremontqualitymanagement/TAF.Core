package se.claremont.taf.core.technologiessupport.dbinteraction;

import se.claremont.taf.core.testcase.TestCase;

public class MySqlConnection extends DbConnection {

    public MySqlConnection(TestCase testCase, String dbUrl, String dbName, String username, String password){
        super(testCase, "com.mysql.jdbc.Driver");
        setDbUrl(dbUrl + "/" + dbName);
        setDbUser(username, password);
    }

}

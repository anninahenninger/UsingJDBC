import java.sql.*;
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        Connection conn1 = null;
        Connection conn2 = null;
        Statement stmt1 = null;
        Statement stmt2 = null;
        try{
            //STEP 2: Check if JDBC driver is available
            Class.forName("com.mysql.cj.jdbc.Driver");
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn1 = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/employees" +
                            "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                    "root",
                    "");
            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt1 = conn1.createStatement();

            // Write a program that gets all employees who are CLERK, creates a new database and writes these employees
            // in the new database. Afterwards, remove all CLERK employees from the first database.

            // Create the new database
            conn2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/" +
                    "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                    "root", "");
            stmt2  = conn2.createStatement();
            stmt2.executeUpdate("CREATE DATABASE IF NOT EXISTS clerk");
            conn2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/clerk" +
                            "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                    "root", "");

            // Create the new table
            String sql = "CREATE TABLE IF NOT EXISTS `clerk`.`onlyclerks` ( `emp_id` INT NOT NULL AUTO_INCREMENT ," +
                    " `emp_name` VARCHAR(20) NOT NULL , `job_name` VARCHAR(30) NOT NULL , `manager_id` INT(20) NOT NULL ," +
                    " `hire_date` DATE NOT NULL , `salary` FLOAT NOT NULL , `comission` FLOAT NOT NULL ," +
                    " `dep_id` INT NOT NULL , PRIMARY KEY (`emp_id`)) ENGINE = InnoDB;";
            stmt2.execute(sql);

            // Extract data from employees table
            String sql1;
            sql1 = "SELECT * FROM employees.employees WHERE job_name = 'CLERK'";
            ResultSet rs = stmt1.executeQuery(sql1);
            int empID;
            String empName;
            String jobName;
            int managerID;
            Date hireDate;
            float salary;
            float commission;
            int deptID;

            while(rs.next()){
                empID  = rs.getInt("emp_id");
                empName = rs.getString("emp_name");
                jobName = rs.getString("job_name");
                managerID = rs.getInt("manager_id");
                hireDate = rs.getDate("hire_date");
                salary = rs.getFloat("salary");
                commission = rs.getFloat("commission");
                deptID = rs.getInt("dep_id");

                String inserting = "INSERT INTO `clerk`.`onlyclerks` (`emp_id`, `emp_name`, `job_name`, `manager_id`," +
                        " `hire_date`, `salary`, `comission`, `dep_id`) VALUES (?,?,?,?,?,?,?,?)";
                //stmt1.executeUpdate(sql2);
                PreparedStatement preparedStmt = conn2.prepareStatement(inserting);
                preparedStmt.setInt (1, empID);
                preparedStmt.setString (2, empName);
                preparedStmt.setString (3, jobName);
                preparedStmt.setInt(4, managerID);
                preparedStmt.setDate (5, hireDate);
                preparedStmt.setFloat(6, salary);
                preparedStmt.setFloat(7, commission);
                preparedStmt.setInt(8, deptID);
                preparedStmt.executeUpdate();
            }
            /**
             * try (final Statement statement1 = connection1.createStatement();
             *      final PreparedStatement insertStatement =
             *      connection2.prepareStatement("insert into table2 values(?, ?)"))
             * {
             *     try (final ResultSet resultSet =
             *          statement1.executeQuery("select foo, bar from table1"))
             *     {
             *         while (resultSet.next())
             *         {
             *             // Get the values from the table1 record
             *             final String foo = resultSet.getString("foo");
             *             final int bar = resultSet.getInt("bar");
             *
             *             // Insert a row with these values into table2
             *             insertStatement.clearParameters();
             *             insertStatement.setString(1, foo);
             *             insertStatement.setInt(2, bar);
             *             insertStatement.executeUpdate();
             *         }
             *     }
             * }
             */

            //STEP 5: Extract data from result set
            String sql3 = "SELECT * FROM clerk.onlyclerks";
            Statement stmt3 = conn2.createStatement();
            ResultSet rs3 = stmt3.executeQuery(sql3);
            System.out.println("Emp-ID    Emp-Name       Job-Name      Manager-ID     Hire-Date         Salary    Commission    Dept-ID");
            System.out.println("-------------------------------------------------------------------------------------------------------");
            while(rs3.next()){
                //Retrieve by column name
                empID  = rs3.getInt("emp_id");
                empName = rs3.getString("emp_name");
                jobName = rs3.getString("job_name");
                managerID = rs3.getInt("manager_id");
                hireDate = rs3.getDate("hire_date");
                salary = rs3.getInt("salary");
                commission = rs3.getInt("comission");
                deptID = rs3.getInt("dep_id");
                //Display values
                System.out.printf("%-10s" + "%-15s" + "%-15s" + "%-9s" + "%15s" + "%14.2f" + "%14.2f" + "%10s%n",
                        empID, empName, jobName, managerID, hireDate, salary, commission, deptID);
                System.out.println();
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt1.close();
            stmt2.close();
            conn1.close();
            conn1.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt1!=null)
                    stmt1.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn1!=null)
                    conn1.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }
}




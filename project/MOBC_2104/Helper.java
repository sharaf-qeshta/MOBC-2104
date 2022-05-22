package MOBC_2104;

import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Helper
{
    public static final String IP = "127.0.0.1";
    public static final int PORT = 27_017;
    public static final String DB_NAME = "DB_course";
    public static final String COLLECTION_NAME = "employees";

    public static MongoCollection<Document> collection;

    /**
     * a main function to test the method
     * we created
     * */
    public static void main(String[] args)
    {
        initiate();
        /*
          [Employee{id='2', name='khaled', salary=1500},
          Employee{id='3', name='sharaf', salary=2000}]
         */
        System.out.println(searchEmployees(Operators.GREATER_THAN, "salary", 1_000));

        /* 1 */
        System.out.println(updateEmployee(Operators.LESS_THAN_OR_EQUALS, "salary",
                1_000, "salary", 1_500));

        /*
            [Employee{id='1', name='ahmed', salary=1500},
            Employee{id='2', name='khaled', salary=1500},
            Employee{id='3', name='sharaf', salary=2000}]
       */
        System.out.println(getAllEmployees());

        addEmployee(new Employee("4", "khaleel", 3_000));

        /*
          [Employee{id='1', name='ahmed', salary=1500},
          Employee{id='2', name='khaled', salary=1500},
          Employee{id='3', name='sharaf', salary=2000},
          Employee{id='4', name='khaleel', salary=3000}]
         */
        System.out.println(getAllEmployees());

        /* 1 */
        System.out.println(deleteEmployees(Operators.EQUALS, "salary", 3_000));

        /*
            [Employee{id='1', name='ahmed', salary=1500},
             Employee{id='2', name='khaled', salary=1500},
             Employee{id='3', name='sharaf', salary=2000}]
         */
        System.out.println(getAllEmployees());

        /* 1 */
        System.out.println(replaceEmployee(Operators.EQUALS, "id", "3",
                new Employee("4", "Sharaf Qeshta", 1_500)));

        /*
            [Employee{id='1', name='ahmed', salary=1500},
             Employee{id='2', name='khaled', salary=1500},
             Employee{id='4', name='Sharaf Qeshta', salary=1500}]
        */
        System.out.println(getAllEmployees());
    }

    /**
     * a function to create a connection with mongoDB
     * and access the collection we specify
     * */
    public static void initiate()
    {
        MongoClient connection = new MongoClient(IP, PORT);
        // if the db not exist it will create it
        MongoDatabase database = connection.getDatabase(DB_NAME);
        collection = database.getCollection(COLLECTION_NAME);
    }

    /**
     * a function to add an employee to the database
     * */
    public static void addEmployee(Employee employee)
    {
        collection.insertOne(new Document("id", employee.getId())
                .append("name", employee.getName())
                .append("salary", employee.getSalary())
        );
    }

    /**
     * a function to retrieve all documents form
     * the collection specified
     * */
    public static List<Employee> getAllEmployees()
    {
        List<Employee> employees = new ArrayList<>();

        for (Document document: collection.find())
            /*
            employees.add(new Employee(document.getString("id"),
                    document.getString("name"),
                    document.getInteger("salary")));

             */
            employees.add(new GsonBuilder().create()
                            .fromJson(document.toJson(), Employee.class));
        return employees;
    }

    /**
     * a function to search the collection for the specified
     * employee
     * */
    public static List<Employee> searchEmployees(String operator, String key, Object value)
    {
        List<Employee> employees = new ArrayList<>();
        FindIterable<Document> iterator = null;
        switch (operator)
        {
            case Operators.GREATER_THAN -> iterator = collection.find(Filters.gt(key, value));
            case Operators.GREATER_THAN_OR_EQUALS -> iterator = collection.find(Filters.gte(key, value));
            case Operators.LESS_THAN -> iterator = collection.find(Filters.lt(key, value));
            case Operators.LESS_THAN_OR_EQUALS -> iterator = collection.find(Filters.lte(key, value));
            case Operators.EQUALS -> iterator = collection.find(Filters.eq(key, value));
            case Operators.NOT_EQUALS -> iterator = collection.find(Filters.ne(key, value));
        }


        // to avoid null pointer exception
        if (iterator == null)
            System.out.println("Invalid operator => " + operator);
        else
           for (Document document: iterator)
                employees.add(new GsonBuilder().create()
                        .fromJson(document.toJson(), Employee.class));

        return employees;
    }

    /**
     * a function to replace an employee with another employee
     * */
    public static long replaceEmployee(String operator, String key,
                                       Object value, Employee employee)
    {
        UpdateResult result = null;
        Document document = new Document("id", employee.getId())
                .append("name", employee.getName())
                .append("salary", employee.getSalary());

        switch (operator)
        {
            case Operators.GREATER_THAN -> result = collection.replaceOne(Filters.gt(key, value), document);
            case Operators.GREATER_THAN_OR_EQUALS -> result = collection.replaceOne(Filters.gte(key, value), document);
            case Operators.LESS_THAN -> result = collection.replaceOne(Filters.lt(key, value), document);
            case Operators.LESS_THAN_OR_EQUALS -> result = collection.replaceOne(Filters.lte(key, value), document);
            case Operators.EQUALS -> result = collection.replaceOne(Filters.eq(key, value), document);
            case Operators.NOT_EQUALS -> result = collection.replaceOne(Filters.ne(key, value), document);
        }

        // to avoid null pointer exception
        if (result == null)
        {
            System.out.println("Invalid Operator => " + operator);
            return 0;
        }
        return result.getModifiedCount();
    }

    /**
     * a function to delete an employee from the collection
     * */
    public static long deleteEmployees(String operator, String key,
                                      Object value)
    {
        DeleteResult result = null;

        switch (operator)
        {
            case Operators.GREATER_THAN -> result = collection.deleteMany(Filters.gt(key, value));
            case Operators.GREATER_THAN_OR_EQUALS -> result = collection.deleteMany(Filters.gte(key, value));
            case Operators.LESS_THAN -> result = collection.deleteMany(Filters.lt(key, value));
            case Operators.LESS_THAN_OR_EQUALS -> result = collection.deleteMany(Filters.lte(key, value));
            case Operators.EQUALS -> result = collection.deleteMany(Filters.eq(key, value));
            case Operators.NOT_EQUALS -> result = collection.deleteMany(Filters.ne(key, value));
        }

        // to avoid null pointer exception
        if (result == null)
        {
            System.out.println("Invalid Operator => " + operator);
            return 0;
        }
        return result.getDeletedCount();
    }

    /**
     * a function to update employee fields that exist in the collection
     * */
    public static long updateEmployee(String operator, String key,
                                      Object value, String updateKey, Object updateValue)
    {
        UpdateResult result = null;
        Document document = new Document("$set", new Document(updateKey, updateValue));


        switch (operator)
        {
            case Operators.GREATER_THAN -> result = collection.updateMany(Filters.gt(key, value), document);
            case Operators.GREATER_THAN_OR_EQUALS -> result = collection.updateMany(Filters.gte(key, value), document);
            case Operators.LESS_THAN -> result = collection.updateMany(Filters.lt(key, value), document);
            case Operators.LESS_THAN_OR_EQUALS -> result = collection.updateMany(Filters.lte(key, value), document);
            case Operators.EQUALS -> result = collection.updateMany(Filters.eq(key, value), document);
            case Operators.NOT_EQUALS -> result = collection.updateMany(Filters.ne(key, value), document);
        }

        // to avoid null pointer exception
        if (result == null)
        {
            System.out.println("Invalid Operator => " + operator);
            return 0;
        }
        return result.getModifiedCount();
    }
}

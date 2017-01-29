package integrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private Repository repository;

    public static void main(String[] args) {
        Parser.parse();
        //SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //mongoExample();
    }

    private void mongoExample() {
        repository.deleteAll();

        // save a couple of customers
        repository.save(new GoogleProductCategory("Alice", "Smith"));
        repository.save(new GoogleProductCategory("Bob", "Smith"));

        // fetch all customers
        System.out.println("Customers found with findAll():");
        System.out.println("-------------------------------");
        for (GoogleProductCategory customer : repository.findAll()) {
            System.out.println(customer);
        }
        System.out.println();

        // fetch an individual customer
        System.out.println("Customer found with findByFirstName('Alice'):");
        System.out.println("--------------------------------");
        System.out.println(repository.findByFirstName("Alice"));

        System.out.println("Customers found with findByLastName('Smith'):");
        System.out.println("--------------------------------");
        for (GoogleProductCategory customer : repository.findByLastName("Smith")) {
            System.out.println(customer);
        }
    }

}

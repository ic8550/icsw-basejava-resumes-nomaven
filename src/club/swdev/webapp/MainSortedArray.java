package club.swdev.webapp;

import club.swdev.webapp.exception.StorageException;
import club.swdev.webapp.model.Resume;
import club.swdev.webapp.storage.SortedArrayStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Interactive test for SortedArrayStorage implementation
 * (just run, no need to understand)
 */
public class MainSortedArray {
    private final static SortedArrayStorage STORAGE = new SortedArrayStorage();

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Resume r;
        while (true) {
            System.out.print("\nВведите одну из команд - (list | size | save Name | get uuid | update uuid | delete uuid | clear | exit): ");
            String[] params = reader.readLine().trim().toLowerCase().split(" ");
            if (params.length < 1 || params.length > 2) {
                System.out.println("Неверная команда.");
                continue;
            }
            String userInput = null;
            if (params.length == 2) {
                userInput = params[1].intern();
            }
            switch (params[0]) {
                case "list":
                    printAll();
                    break;
                case "size":
                    System.out.println(STORAGE.size());
                    break;
                case "save":
                    r = new Resume(userInput);
                    try {
                        STORAGE.save(r);
                    } catch (StorageException e) {
                        System.out.println("Error save(): " + e.getMessage());
                    }
                    printAll();
                    break;
                case "update":
                    r = new Resume(userInput);
                    try {
                        STORAGE.update(r);
                    } catch (StorageException e) {
                        System.out.println("Error update(): " + e.getMessage());
                    }
                    printAll();
                    break;
                case "get":
                    try {
                        Resume resume = STORAGE.get(userInput);
                        System.out.println(resume);
                    } catch (StorageException e) {
                        System.out.println("Error in get(): " + e.getMessage());
                    }
                    break;
                case "delete":
                    try {
                        STORAGE.delete(userInput);
                    } catch (StorageException e) {
                        System.out.println("Error delete(): " + e.getMessage());
                    }
                    printAll();
                    break;
                case "clear":
                    STORAGE.clear();
                    printAll();
                    break;
                case "exit":
                    return;
                default:
                    System.out.println("Неверная команда.");
                    break;
            }
        }
    }

    static void printAll() {
        List<Resume> all = STORAGE.getAllSorted();
        System.out.println("----------------------------");
        if (all.size() == 0) {
            System.out.println("Empty");
        } else {
            for (Resume r : all) {
                System.out.println(r);
            }
        }
        System.out.println("----------------------------");
    }
}

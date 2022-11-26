import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.HashMap;

class ContactsApp {
    public static void main(String[] args) {
        try {
            new File("./contacts/temp").mkdirs();
            clearTempFolder();
            while (true) {
                Scanner sc = new Scanner(System.in);
                clearConsole();
                printHeader("# Home");
                System.out.println("1 - View Contacts");
                System.out.println("2 - Create New Contact");
                System.out.println("3 - Delete Contact");
                System.out.println(":quit - Quit\n");
                System.out.println(
                        "Use numbers to navigate around. For example to view all contacts, enter number 1 and press ENTER key.");
                System.out.print("\nSelect: ");
                String chosenNum = sc.nextLine();
                switch (chosenNum.strip()) {
                    case "1":
                        viewContacts(sc, false);
                        break;
                    case "2":
                        System.out.print("\nEnter Personal ID: ");
                        String personalID = sc.nextLine();
                        if (personalID.strip() == "") {
                            System.out.println("This value is mandatory and can not be empty.");
                            TimeUnit.SECONDS.sleep(3);
                        } else {
                            String contactFilename = personalID.replace(" ", "") + ".txt";
                            editContact(sc, contactFilename, true);
                        }
                        break;
                    case "3":
                        viewContacts(sc, true);
                        break;
                    case ":quit":
                        System.out.print("Quitting.");
                        System.exit(0);
                    default:
                        System.out.println("Value does not match with any option.");
                        TimeUnit.SECONDS.sleep(3);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("An error occurred: ");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void viewContacts(Scanner sc, boolean deleteMode) {
        try {
            boolean goBack = false;
            while (!goBack) {
                clearConsole();
                if (deleteMode) {
                    printHeader("# Delete Contacts");
                } else {
                    printHeader("# View Contacts");
                }

                File contactsFolder = new File("./contacts");
                File[] allContacts = contactsFolder.listFiles();
                Map<String, String> IDdict = new HashMap<String, String>();
                int counter = 1;
                for (File file : allContacts) {
                    if (file.isFile()) {
                        String filename = file.getName();
                        System.out.println(counter + " - " + filename.replace(".txt", ""));
                        IDdict.put(String.valueOf(counter), filename);
                        counter++;
                    }
                }
                System.out.println(":back - Go back");
                System.out.println(":quit - Quit\n");
                if (deleteMode) {
                    System.out.println(
                            "To delete contact enter its number.");
                } else {
                    System.out.println(
                            "To open contact enter its number.");
                }

                System.out.print("\nSelect: ");
                String chosenNum = sc.nextLine();
                switch (chosenNum.strip()) {
                    case ":back":
                        goBack = true;
                        break;
                    case ":quit":
                        System.out.print("Quitting.");
                        System.exit(0);
                    default:
                        String contactFilename = IDdict.get(String.valueOf(chosenNum));
                        if (contactFilename != null) {
                            if (deleteMode) {
                                deleteContact(contactFilename);
                            } else {
                                openContact(sc, contactFilename);
                            }
                        } else {
                            System.out.println("Value does not match with any option.");
                            TimeUnit.SECONDS.sleep(3);
                        }

                }
            }
        } catch (InterruptedException e) {
            System.out.println("An error occurred: ");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void openContact(Scanner sc, String contactFilename) {
        try {
            boolean goBack = false;
            while (!goBack) {
                clearConsole();
                printHeader("# Open Contact");
                BufferedReader br = new BufferedReader(new FileReader("./contacts/" + contactFilename));
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                br.close();
                System.out.println("\n:back - Go back");
                System.out.println(":edit - Edit values");
                System.out.println(":quit - Quit");
                System.out.print("\nSelect: ");
                String chosenNum = sc.nextLine();
                switch (chosenNum.strip()) {
                    case ":back":
                        goBack = true;
                        break;
                    case ":edit":
                        editContact(sc, contactFilename, false);
                        break;
                    case ":quit":
                        System.out.print("Quitting.");
                        System.exit(0);
                    default:
                        System.out.println("Value does not match with any option.");
                        TimeUnit.SECONDS.sleep(3);
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("An error occurred: ");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void editContact(Scanner sc, String contactFilename, boolean newContact) {
        try {
            boolean goBack = false;
            if (newContact) {
                updateContact(contactFilename.replace(".txt", ""), "", "", "", "", "", true);
            }
            while (!goBack) {
                BufferedReader br;
                String personalID = "";
                String firstName = "";
                String lastName = "";
                String phoneNumber = "";
                String address = "";
                String email = "";
                clearConsole();
                if (newContact) {
                    printHeader("# New Contact");
                } else {
                    printHeader("# Edit Contact");
                }
                if (newContact) {
                    br = new BufferedReader(new FileReader("./contacts/temp/" + contactFilename));
                } else {
                    br = new BufferedReader(new FileReader("./contacts/" + contactFilename));
                }
                String line;
                int counter = 1;
                while ((line = br.readLine()) != null) {
                    String value = "";
                    try {
                        value = line.split(":")[1];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // IF EMAIL OR ADDRESS IS EMPTY IT THROWS THIS OutOfBoundsException
                    }
                    switch (counter) {
                        case 1:
                            personalID = value;
                            System.out.println(counter + " - (*) " + line);
                            break;
                        case 2:
                            firstName = value;
                            System.out.println(counter + " - (*) " + line);
                            break;
                        case 3:
                            lastName = value;
                            System.out.println(counter + " - (*) " + line);
                            break;
                        case 4:
                            phoneNumber = value;
                            System.out.println(counter + " - (*) " + line);
                            break;
                        case 5:
                            address = value;
                            System.out.println(counter + " - " + line);
                            break;
                        case 6:
                            email = value;
                            System.out.println(counter + " - " + line);
                            break;
                    }
                    counter++;
                }
                br.close();
                if (newContact) {
                    System.out.println("\n:save - Saves new contact");
                    System.out.println(":back - Go back without saving");
                    System.out.println(":quit - Quit");
                    System.out.println(
                            "\n(*) = Mandatory values.");
                    System.out.print("\nSelect: ");
                    String chosenNum = sc.nextLine();
                    switch (chosenNum.strip()) {
                        case "1":
                            System.out.println("This value can not be edited.");
                            TimeUnit.SECONDS.sleep(3);
                            break;
                        case "2":
                            System.out.print("First Name: ");
                            String newValue = sc.nextLine();
                            if (newValue.strip() == "") {
                                System.out.println("This value is mandatory and can not be empty.");
                                TimeUnit.SECONDS.sleep(3);
                            } else {
                                firstName = newValue;
                                updateContact(personalID, firstName, lastName, phoneNumber, address, email, true);
                            }
                            break;
                        case "3":
                            System.out.print("Last Name: ");
                            String newLastName = sc.nextLine();
                            if (newLastName.strip() == "") {
                                System.out.println("This value is mandatory and can not be empty.");
                                TimeUnit.SECONDS.sleep(3);
                            } else {
                                lastName = newLastName;
                                updateContact(personalID, firstName, lastName, phoneNumber, address, email, true);
                            }
                            break;
                        case "4":
                            System.out.print("Phone Number: ");
                            String newPhoneNumber = sc.nextLine();
                            if (newPhoneNumber.strip() == "") {
                                System.out.println("This value is mandatory and can not be empty.");
                                TimeUnit.SECONDS.sleep(3);
                            } else {
                                phoneNumber = newPhoneNumber;
                                updateContact(personalID, firstName, lastName, phoneNumber, address, email, true);
                            }
                            break;
                        case "5":
                            System.out.print("Address: ");
                            String newAddress = sc.nextLine();
                            address = newAddress;
                            updateContact(personalID, firstName, lastName, phoneNumber, address, email, true);
                            break;
                        case "6":
                            System.out.print("Email: ");
                            String newEmail = sc.nextLine();
                            email = newEmail;
                            updateContact(personalID, firstName, lastName, phoneNumber, address, email, true);
                            break;
                        case ":save":
                            if (personalID == "" || firstName == "" || lastName == "" || phoneNumber == "") {
                                System.out.println("Please fill all mandatory values before saving.");
                                TimeUnit.SECONDS.sleep(3);
                            } else {
                                updateContact(personalID, firstName, lastName, phoneNumber, address, email, false);
                                goBack = true;
                            }
                            break;
                        case ":back":
                            goBack = true;
                            break;
                        case ":quit":
                            System.out.print("Quitting.");
                            System.exit(0);
                        default:
                            System.out.println("Value does not match with any option.");
                            TimeUnit.SECONDS.sleep(3);
                    }
                } else {
                    System.out.println("\n:back - Exit editing mode");
                    System.out.println(":quit - Quit");
                    System.out.println(
                            "\nValues are going to be saved as you edit them.");
                    System.out.println(
                            "(*) = Mandatory values.");
                    System.out.print("\nSelect: ");
                    String chosenNum = sc.nextLine();
                    switch (chosenNum.strip()) {
                        case "1":
                            System.out.println("This value can not be edited.");
                            TimeUnit.SECONDS.sleep(3);
                            break;
                        case "2":
                            System.out.print("First Name: ");
                            String newValue = sc.nextLine();
                            if (newValue.strip() == "") {
                                System.out.println("This value is mandatory and can not be empty.");
                                TimeUnit.SECONDS.sleep(3);
                            } else {
                                firstName = newValue;
                                updateContact(personalID, firstName, lastName, phoneNumber, address, email, false);
                            }
                            break;
                        case "3":
                            System.out.print("Last Name: ");
                            String newLastName = sc.nextLine();
                            if (newLastName.strip() == "") {
                                System.out.println("This value is mandatory and can not be empty.");
                                TimeUnit.SECONDS.sleep(3);
                            } else {
                                lastName = newLastName;
                                updateContact(personalID, firstName, lastName, phoneNumber, address, email, false);
                            }
                            break;
                        case "4":
                            System.out.print("Phone Number: ");
                            String newPhoneNumber = sc.nextLine();
                            if (newPhoneNumber.strip() == "") {
                                System.out.println("This value is mandatory and can not be empty.");
                                TimeUnit.SECONDS.sleep(3);
                            } else {
                                phoneNumber = newPhoneNumber;
                                updateContact(personalID, firstName, lastName, phoneNumber, address, email, false);
                            }
                            break;
                        case "5":
                            System.out.print("Address: ");
                            String newAddress = sc.nextLine();
                            address = newAddress;
                            updateContact(personalID, firstName, lastName, phoneNumber, address, email, false);
                            break;
                        case "6":
                            System.out.print("Email: ");
                            String newEmail = sc.nextLine();
                            email = newEmail;
                            updateContact(personalID, firstName, lastName, phoneNumber, address, email, false);
                            break;
                        case ":back":
                            goBack = true;
                            break;
                        case ":quit":
                            System.out.print("Quitting.");
                            System.exit(0);
                        default:
                            System.out.println("Value does not match with any option.");
                            TimeUnit.SECONDS.sleep(3);
                    }
                }

            }
        } catch (IOException | InterruptedException e) {
            System.out.println("An error occurred: ");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void updateContact(String personalID, String firstName, String lastName, String phoneNumber,
            String address, String email, boolean temporaryContact) {
        try {
            FileWriter fw;
            if (temporaryContact) {
                fw = new FileWriter("./contacts/temp/" + personalID + ".txt");
            } else {
                fw = new FileWriter("./contacts/" + personalID + ".txt");
            }
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Personal ID:" + personalID);
            bw.newLine();
            bw.write("First Name:" + firstName);
            bw.newLine();
            bw.write("Last Name:" + lastName);
            bw.newLine();
            bw.write("Phone Number:" + phoneNumber);
            bw.newLine();
            bw.write("Address:" + address);
            bw.newLine();
            bw.write("Email:" + email);
            bw.close();
        } catch (IOException e) {
            System.out.println("An error occurred: ");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void printHeader(String tab) {
        System.out.println("----------------");
        System.out.println("- Contacts App -");
        System.out.println("----------------\n" + tab + "\n");
    }

    public static void deleteContact(String contactFilename) {
        try {
            File contact = new File("./contacts/" + contactFilename);
            if (contact.delete()) {
                System.out.println("Deleted the contact: " + contactFilename.replace(".txt", ""));
                TimeUnit.SECONDS.sleep(2);
            } else {
                System.out.println("Failed to delete the contact.");
                TimeUnit.SECONDS.sleep(2);
            }
        } catch (InterruptedException e) {
            System.out.println("An error occurred: ");
            e.printStackTrace();
            System.exit(0);
        }

    }

    public static void clearTempFolder() {
        File dir = new File("./contacts/temp");
        for (File file : dir.listFiles())
            if (!file.isDirectory())
                file.delete();
    }

    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException e) {
        }
    }
}
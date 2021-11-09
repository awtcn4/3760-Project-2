package Project2;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.security.Security;
import java.security.Provider;

public class Project2 {

	final static String secretKey = "ssshhhhhhhhhhh!!!!";
	private static final int PASSLENGTH = 10;
	private static final int STOPINT = 4;
	private static final int RANGE = 124;
	static int passwordRangeHigh;

	public static void main(String args[]) throws Exception {
		printTask1();
		startTask1();
		printEnd();

	}

	public static void startTask1() throws Exception {
		Scanner scan = new Scanner(System.in);
		System.out.println(
				"Would you like to create(1),authenticate an account(2), start Task 2(3). start Task3(4) or exit(5)");
		System.out.print("To make a decision, please input the indicated number: ");
		int choice = scan.nextInt();
		switch (choice) {
		case 1:
			String username;
			String password;
			System.out.print("Please enter a username(no numbers allowed, 10 or less characters): ");
			username = scan.next();
			if (username.length() > 10) {
				System.out.println("More than 10 characters, Program Terminated");
				System.exit(1);
			}
			System.out.print("Please enter a password(no letters allowed, 10 or less characters): ");
			password = scan.next();
			if (password.length() > PASSLENGTH) {
				System.out.println("More than 10 numbers, Program Terminated");
				System.exit(1);
			}
			createAccount(username, password);
			break;
		case 2:
			authenticateAccount();
			break;
		case 3:
			Task2();
			break;
		case 4:
			Task3();
			break;
		case 5:
			System.exit(1);
			break;
		default:
			break;
		}
		startTask1();
	}

	public static void authenticateAccount() throws Exception {
		Scanner scan = new Scanner(System.in);
		String enteredName;
		String enteredPassword;
		System.out.print("Enter your name: ");
		enteredName = scan.next();
		System.out.print("Enter your password: ");
		enteredPassword = scan.next();
		if (authenticateFile1(enteredName, enteredPassword)) {
			System.out.println("Account validated from file 1");
		} else {
			System.out.println("Account could not be validated from file 1. Program Terminated");
			System.exit(1);
		}
		if (authenticateFile2(enteredName, enteredPassword)) {
			System.out.println("Account validated from file 2");
		} else {
			System.out.println("Account could not be validated from file 2. Program Terminated");
			System.exit(1);
		}
		if (authenticateFile3(enteredName, enteredPassword)) {
			System.out.println("Account validated from file 3");
			System.out.println("Login Successful");
		} else {
			System.out.println("Account could not be validated from file 3. Program Terminated");
			System.exit(1);
		}
	}

	private static boolean authenticateFile3(String name, String password) throws IOException {
		FileReader fileReader = new FileReader("src/Resources/PasswordFile3");
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String s;
		String[] words = null;
		while ((s = bufferedReader.readLine()) != null) {
			words = s.split(" ");
			for (String word : words) {
				if (word.equals(name)) {
					String storedPassword = bufferedReader.readLine();
					storedPassword = AES.decryptPlusSalt(storedPassword);
					if (storedPassword.equals(password)) {
						return true;
					} else {
						return false;
					}
				}
			}
		}
		bufferedReader.close();
		return false;
	}

	private static boolean authenticateFile2(String name, String password) throws Exception {
		FileReader fileReader = new FileReader("src/Resources/PasswordFile2");
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String s;
		String[] words = null;
		while ((s = bufferedReader.readLine()) != null) {
			words = s.split(" ");
			for (String word : words) {
				if (word.equals(name)) {
					String storedPassword = bufferedReader.readLine();
					storedPassword = AES.decrypt(storedPassword, secretKey);
					if (storedPassword.equals(password)) {
						return true;
					} else {
						return false;
					}
				}
			}
		}
		bufferedReader.close();
		return false;
	}

	private static boolean authenticateFile1(String name, String password) throws IOException {
		FileReader fileReader = new FileReader("src/Resources/PasswordFile1");
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String s;
		String[] words = null;
		while ((s = bufferedReader.readLine()) != null) {
			words = s.split(" ");
			for (String word : words) {
				if (word.equals(name)) {
					String storedPassword = bufferedReader.readLine();
					if (storedPassword.equals(password)) {
						return true;
					} else {
						return false;
					}
				}
			}
		}
		bufferedReader.close();
		return false;
	}

	public static void createAccount(String username, String password) throws Exception {
		Integer.parseInt(password);
		Random random = new Random();
		byte[] salt = new byte[1];
		random.nextBytes(salt);

		FileWriter PasswordFile1 = new FileWriter("src/Resources/PasswordFile1", true);
		FileWriter PasswordFile2 = new FileWriter("src/Resources/PasswordFile2", true);
		FileWriter PasswordFile3 = new FileWriter("src/Resources/PasswordFile3", true);

		PasswordFile1.write("Name: " + username + " \n" + password + "\n");
		String hashedPassword = AES.encrypt(password, secretKey);
		PasswordFile2.write("Name: " + username + " \n" + hashedPassword + "\n");
		hashedPassword = AES.encryptPlusSalt(password);
		PasswordFile3.write("Name: " + username + " \n" + hashedPassword + "\n");
		PasswordFile1.close();
		PasswordFile2.close();
		PasswordFile3.close();
		System.out.println("Account Created");
		// System.out.println(accounts);
	}

	public static void printEnd() {
		System.out.println("------------------------------------");
	}

	private static void printTask1() {
		System.out.println("---------------TASK 1---------------");
	}

	public static void Task2() throws Exception {

		String template = "awt";
		String password = "";
		Scanner scan = new Scanner(System.in);
		int passwordRangeLow;
		System.out.println("What is the password length range?");
		System.out.print("Lowest number: ");
		passwordRangeLow = scan.nextInt();
		System.out.print("Highest number: ");
		passwordRangeHigh = scan.nextInt();
		System.out.print("How Many Accounts: ");
		int accounts = scan.nextInt();

		int i = 0;
		while (i < accounts) {
			template = "awt";
			int k = 0;
			while (k < i) {
				template += "t";
				k++;
			}
			int j = 0;
			password = "";
			while (j < passwordRangeHigh) {
				Random rand = new Random();
				int randomNum = rand.nextInt(10);
				password += randomNum + "";
				// System.out.println("Password is " + password);
				// System.out.println("Here");
				if (j > passwordRangeLow) {
					// System.out.println("Here 2");
					Random stop = new Random();
					int stopper = stop.nextInt(STOPINT);
					// System.out.println("Stoppper is: " + stopper);
					if (stopper == 1) {
						j = passwordRangeHigh;
						break;
					}
				}
				j++;
			}
			createAccount(template, password);
			i++;

		}

		// Might have to change create account to so that this is easier
		// usernames, aw + i; where i is incremented each time

	}

	public static void Task3() throws IOException, Exception {

		Scanner scan = new Scanner(System.in);
		String crackUserName = "awt";
		String password = "";
		int file;
		int count = 0;
		int cracked = 0;
		int accounts;
		System.out.print("Which file are we using(2 or 3): ");
		file = scan.nextInt();

		
		if (file == 2) {
			long startTime1 = System.currentTimeMillis();
			File fileName = new File("src/Resources/PasswordFile2");
			Scanner fileScanner = new Scanner(fileName);
			while (fileScanner.hasNextLine()) {
				fileScanner.nextLine();
				count++;
			}
			accounts = count / 2;
			fileScanner.close();
			for (int i = 0; i <= accounts; i++) {
				for (int j = 0; authenticateFile2(crackUserName, password) == false; j++) {
					// System.out.println("Here");
					password = "";
					int pass = j;
					password += pass;
					if (authenticateFile2(crackUserName, password) == true) {
						cracked++;
						System.out.println("User " + crackUserName + " cracked with password " + password);
						j = 0;
						password = "";
						crackUserName += "t";
						if(cracked == accounts) {
							long endTime1 = System.currentTimeMillis();
							long time1 = endTime1 - startTime1;
							System.out.println("Time to crack " + accounts + " is " + time1/1000 + " seconds");
							startTask1();
						}
					}
				}
			}
		} else if (file == 3) {
			long startTime2 = System.currentTimeMillis();
			File fileName = new File("src/Resources/PasswordFile2");
			Scanner fileScanner = new Scanner(fileName);
			while (fileScanner.hasNextLine()) {
				fileScanner.nextLine();
				count++;
			}
			accounts = count / 2;
			fileScanner.close();
			for (int i = 0; i <= accounts; i++) {
				for (int j = 0; authenticateFile3(crackUserName, password) == false; j++) {
					password = "";
					int pass = j;
					password += pass;
					if (authenticateFile3(crackUserName, password) == true) {
						cracked++;
						System.out.println("User " + crackUserName + " cracked with password " + password);
						j = 0;
						password = "";
						crackUserName += "t";
						if(cracked == accounts) {
							long endTime2 = System.currentTimeMillis();
							long time2 = endTime2 - startTime2;
							System.out.println("Time to crack " + accounts + " is " + time2/1000 + "seconds");
							startTask1();
						}
					}
				}
			}
		}
	}
}

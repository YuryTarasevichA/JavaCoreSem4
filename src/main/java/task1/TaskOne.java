package task1;

import java.util.Scanner;

public class TaskOne {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input login: ");
        String login = scanner.nextLine();
        System.out.println("Input password: ");
        String password = scanner.nextLine();
        System.out.println("Input confirm password: ");
        String confirmPassword = scanner.nextLine();
        while (login == null) {
            System.out.println("Input login: ");
            login = scanner.nextLine();
        }
        while (password.isEmpty() || confirmPassword.isEmpty()) {
            System.out.println("Input password: ");
            password = scanner.nextLine();
            System.out.println("Input confirm password: ");
            confirmPassword = scanner.nextLine();
        }
        try {
            boolean check = checkLoginAndPassword(login, password, confirmPassword);
            System.out.println(check);
        } catch (WrongLoginException | WrongPasswordException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    public static boolean checkLoginAndPassword(String login, String password, String confirmPassword)
            throws WrongLoginException, WrongPasswordException {
//        if (login == null || login.isBlank() || password == null || confirmPassword == null) {
//            throw new NullPointerException("The one of parameters is null");
//        }
        if (login.length() > 20) {
            throw new WrongLoginException("The length of login is more than 20");
        }
        if (password.length() > 20) {
            throw new WrongPasswordException("The length of password is more than 20");
        }
        if (!password.equals(confirmPassword)) {
            throw new WrongPasswordException("The password is not equals confirm password");
        }
        return true;

    }

}

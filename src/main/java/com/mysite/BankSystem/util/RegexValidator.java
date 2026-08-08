package com.mysite.BankSystem.util;

public class RegexValidator {

    public static boolean regexNumber(String number) {
        return (number != null)&&
                number.matches("^0\\d{10}$|^00\\d{12}$|\\+\\d{12}$");

    }

    public static boolean regexEmail(String email) {
        return (email != null)&&
                email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }


    public static boolean regexNationalCode(String nationalCode) {
        return (nationalCode != null)&&
                nationalCode.matches("^\\d{10}$");
    }

    public static boolean regexCompanyRegistration(String registration) {
        return (registration != null)&&
                registration.matches("^\\d{1,20}$");
    }
}

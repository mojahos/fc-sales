import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sale {
    private final String country; // ISO 3 country code
    private final double price; // price of sale
    private final Date dateOfSale; // date of sale

    // Main Constructor: create an object with desired country code, price and date.
    public Sale(String country, double price, Date dateOfSale) {
        this.country = country;
        this.price = price;
        this.dateOfSale = dateOfSale;
    }

    // create an object with the current date
    public Sale(String country, double price) {
        this(country, price, new Date());
    }

    // create an object with the current date and country as USA
    public Sale(double price) {
        this("USA", price, new Date());
    }

    // create an object from String arguments, parsing them first.
    public Sale(String country, String stringPrice, String stringDateOfSale) {
        this(country, parsePrice(stringPrice), parseDate(stringDateOfSale));
    }

    public String getCountry() {
        return country;
    }

    public double getPrice() {
        return price;
    }

    public Date getDate() {
        return dateOfSale;
    }

    /*
     * Check if country code is valid, if not, then set it to USA
     *
     * return String object of valid country code
     */
    public static String parseCountryCode(String countryCode) {
        if (validateCountryCode(countryCode)) {
            return countryCode;
        } else {
            return "USA";
        }
    }

    /*
     * Try parsing stringPrice as double and check if it is >= 0.0, if unsuccessful, return 0.0
     *
     * return double value of valid price
     */
    public static double parsePrice(String stringPrice) {
        double price = 0.0;

        // Parse stringPrice into Double price
        try {
            price = Double.parseDouble(stringPrice);
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
        }

        if (validatePrice(price)) {
            return price;
        } else {
            return 0.0;
        }
    }

    /*
     * Check if stringDate is valid and parse it as Date, if not, then set it to current date
     *
     * return Date object of a valid date
     */
    public static Date parseDate(String stringDate) {
        Date date;

        // Parse stringDate into Date date
        try {
                return new SimpleDateFormat("MM/dd/yyyy").parse(stringDate);
            } catch (ParseException e) {
            return new Date();
        }
    }

    /*
     * Check if given ISO 3 country code is valid and return a boolean value.
     */
    public static boolean validateCountryCode(String countryCode) {
        for (String validCountryCode : CurrencyUtil.countryCodes) {
            if (validCountryCode.equals(countryCode.trim())) {
                return true;
            }
        }
        return false;
    }

    /*
     * Check if given price is valid and above 0.0 and return a boolean value.
     */
    public static boolean validatePrice(double price) {
        if (price >= 0.0) {
            return true;
        }
        return false;
    }

    /*
     * Check if given date is valid and return a boolean value.
     */
    public static boolean validateDate(String stringDate) {
        try {
            //if not valid, it will throw ParseException
            Date date = new SimpleDateFormat("MM/dd/yyyy").parse(stringDate);
            return true;

        } catch (ParseException e) {

            return false;
        }
    }

    /*
     * Print out country code, price and date separating them with '   ·   '
     * This is used to display elements on the JList
     */
    public String toString() {

        DateFormat localDateFormat = DateFormat.getDateInstance();
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);

        return getCountry() + "  ·  " + localDateFormat.format(getDate()) + "  ·  " + numberFormat.format(getPrice());
    }
}

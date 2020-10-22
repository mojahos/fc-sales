import java.util.HashMap;
import java.util.regex.*;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;
import java.util.Currency;

class CurrencyUtil {

    public static ArrayList<String> countryCodes;  // contains ISO 3 country codes
    public static HashMap<String, String> countryCodesISO2; // maps ISO 3 to ISO 2 for currency conversion API.

    /* intialize countryCodes */

    /* Originally, the project description asked for ISO 3 country codes, so I implemented ISO 3
     * But since the API for the currency conversion requires ISO 2, I had to use a work around
     * to convert ISO 3 into ISO 2 when converting currency by using HashMap (below).
     */
    static {
        countryCodes = new ArrayList<String>();
        countryCodesISO2 = new HashMap<>();
        for (String s: Locale.getISOCountries()) {
            Locale l = new Locale("en", s);

            countryCodes.add(l.getISO3Country());
            countryCodesISO2.put(l.getISO3Country(), l.getCountry());
        }

    }


    /*
     * The currency conversion API cannot convert currencies of every country.
     * So, this checks if the currency conversion of a given ISO3 country code is supported.
     * It returns a true or false value.
     */
    public static boolean checkCountryCurrencyCodeAvailability(String countryCodeISO3) {
        String countryCurrency = getCurrencyCode(countryCodesISO2.get(countryCodeISO3));

        try {
            URL url = new URL("https://api.exchangeratesapi.io/latest?base=" + countryCurrency);
            BufferedReader reader = new
                    BufferedReader(new InputStreamReader(url.openStream()));
            String line = reader.readLine();
            return !line.contains("not supported");
        } catch (Exception e)  {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /*
     * returns the currency of a country from a given country code (ISO 2).
     */
    private static String getCurrencyCode(String countryCode) {
        Locale aLocale = new Locale.Builder().setRegion(countryCode).build();
        return Currency.getInstance(aLocale).getCurrencyCode();
    }
    /**
     *
     * @param fromISO3CountryCode ISO country code representing country of currency expressed in amount.
     * @param toISO3CountryCode ISO country code representing country of target currency
     * @param amount currency amount to be converted
     *
     * @return value of amount, converted to currency of "to" country
     *
     * Function uses an online conversion API, simple takes a decimal number from the
     * JSON string.
     *
     * Using exchangeratesapi.io API, a country's currency is converted to the currency of another country's currency
     * using ISO3 country codes. ISO3 country codes are converted to ISO 2 using countryCodesISO2 HashMap and
     * and then used to get the currency. The currency will be used to get the currency rates from the API. Then
     * it will convert the amount from and to the given counties' currencies.
     *
     * See https://exchangeratesapi.io/ for more info.
     */
    public static Double currConvert(String fromISO3CountryCode, String toISO3CountryCode, double amount) {

        if (fromISO3CountryCode.equals(toISO3CountryCode)) {
            return amount;
        }

        String fromCurrency = getCurrencyCode(countryCodesISO2.get(fromISO3CountryCode));
        String toCurrency = getCurrencyCode(countryCodesISO2.get(toISO3CountryCode));

        try {
            URL url = new
                    URL("https://api.exchangeratesapi.io/latest?base=" +
                    fromCurrency + "&symbols=" + toCurrency);
            // System.out.println("Getting Currency Rates...  " + url.toString());
            BufferedReader reader = new
                    BufferedReader(new InputStreamReader(url.openStream()));
            String jsonString = reader.readLine();
            // System.out.println("Result: " + jsonString);
            if (jsonString.length() > 0) {
                Pattern pattern =
                        Pattern.compile("\\d+\\.\\d*");
                Matcher matcher = pattern.matcher(jsonString);
                if (matcher.find()) {
                    return Double.parseDouble(matcher.group()) * amount;
                }
                else return null;
            }
            reader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}

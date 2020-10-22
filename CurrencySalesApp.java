
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CurrencySalesApp extends JFrame {

    /*
     * this is the master list, contain all the entries. the JList uses this and displays entries
     * based on the date range the user selects using the JSpinner.
     */
    ArrayList<Sale> sales;

    private JPanel mainPanel; // added to this JFrame
    private JPanel topPanel; // add to mainPanel on top
    private JPanel centerPanel; // add to mainPanel on center
    private JPanel bottomPanel; // add to mainPanel on bottom

    private JSpinner startDateSpinner; // start date of the values to display on JList
    private JSpinner endDateSpinner; // end date of the values to display on JList
    private SpinnerDateModel startDateSpinnerModel;
    private SpinnerDateModel endDateSpinnerModel;

    private JList list; // displays all the entries
    private DefaultListModel listModel;

    private JLabel addCountryLBL;
    private JLabel addPriceLBL;
    private JLabel addDateLBL;
    private JLabel totalLBL;

    private JTextField addCountryTF;
    private JTextField addPriceTF;
    private JTextField addDateTF;

    // when the user enters a country code, date and price and clicks this button, it is added to the list.
    private JButton addBTN;
    // displayed dates are changed when the user clicks this button. I decided this was a b
    private JButton changeDatesBTN;


    // main constructor
    public CurrencySalesApp() {
        sales = new ArrayList<Sale>();
        initializeUI();
    }

    /*
     * main method: creates an object of this class and example hardcoded entries are put on the JList list.
     */
    public static void main(String[] args) {
        CurrencySalesApp p2t = new CurrencySalesApp();

        /* Hard coded entries */
        p2t.addSaleToList(new Sale("BRA", "96.99", "10/14/2006"));
        p2t.addSaleToList(new Sale("CHN", "411.32", "10/11/2015"));
        p2t.addSaleToList(new Sale("AUS", "335.94", "7/20/1996"));
        p2t.addSaleToList(new Sale("FRA", "293.15", "2/25/2010"));
        p2t.addSaleToList(new Sale("THA", "249.11", "3/25/2016"));
        p2t.addSaleToList(new Sale("USA", "195.66", "6/16/2018"));
        p2t.addSaleToList(new Sale("SWE", "160.37", "8/27/2018"));
        p2t.addSaleToList(new Sale("CAN", "51.49", "3/1/2012"));
        p2t.addSaleToList(new Sale("HUN", "31.49", "5/12/2012"));
        p2t.addSaleToList(new Sale("IND", "149.1", "9/26/2014"));
    }

    /*
     * creates and displays the GUI
     */
    private void initializeUI() {

        // create Panels
        mainPanel = new JPanel(new BorderLayout());
        topPanel = new JPanel(new GridLayout(1, 3,0,0));
        centerPanel = new JPanel(new GridLayout(1,1,0,0));
        bottomPanel = new JPanel(new GridLayout(2,4,2,0));

        // For topPanel
        Calendar cal = Calendar.getInstance();

        Date now = cal.getTime();

        cal.add(Calendar.YEAR, -100); // allow the user to select a date up to 100 years back.
        Date startDate = cal.getTime();

        cal.add(Calendar.YEAR, 200); // disallow the user from picking a date past 100 years.
        Date endDate = cal.getTime();

        startDateSpinnerModel = new SpinnerDateModel(startDate, startDate, endDate, Calendar.YEAR);
        startDateSpinner = new JSpinner(startDateSpinnerModel);
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, "MM/dd/yyyy"));


        endDateSpinnerModel = new SpinnerDateModel(now, startDate, endDate, Calendar.YEAR);
        endDateSpinner = new JSpinner(endDateSpinnerModel);
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner, "MM/dd/yyyy"));


        changeDatesBTN = new JButton("Check Dates");
        changeDatesBTN.addActionListener(new ChangeDatesActionListener());

        topPanel.add(startDateSpinner);
        topPanel.add(endDateSpinner);
        topPanel.add(changeDatesBTN);


        // For centerPanel
        listModel = new DefaultListModel<Sale>();

        list = new JList<Sale>(listModel);
        list.setCellRenderer(new SalesJListRenderer());

        centerPanel.add(list);

        // For bottomPanel

        addCountryLBL = new JLabel("\tCountry Code");
        addPriceLBL = new JLabel("\tPrice");
        addDateLBL = new JLabel("\tDate");
        totalLBL = new JLabel("Total: ");

        addCountryTF = new JTextField("BRA");
        addPriceTF = new JTextField("100.00");
        addDateTF = new JTextField("1/1/2018");
        addBTN = new JButton("Add");
        addBTN.addActionListener(new AddButtonListener());

        bottomPanel.add(addCountryLBL);
        bottomPanel.add(addPriceLBL);
        bottomPanel.add(addDateLBL);
        bottomPanel.add(totalLBL);
        bottomPanel.add(addCountryTF);
        bottomPanel.add(addPriceTF);
        bottomPanel.add(addDateTF);
        bottomPanel.add(addBTN);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // add mainPanel to JFrame
        add(mainPanel);


        setVisible(true);
        setSize(600, 400);
        setTitle("Foreign Currency Sales | Mohammed J. Hossain");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    /*
     * returns a reference ArrayList<Sale> which is created from
     * all the entries in the JList. Since the JList displays countries based on the
     * user selected dates (using JSpinner), the displayed entries are not always the same
     * as those the master list of ArrayList<Sale> sales.
     *
     */
    public ArrayList<Sale> getSalesFromJList() {

        ArrayList<Sale> displayedRES = new ArrayList<>();

        for (int i = 0; i < list.getModel().getSize(); i++) {

            // country code, date and price is separated by '·'
            String[] tokens = list.getModel().getElementAt(i).toString().split("·");

            String country = tokens[0].trim();
            String unformattedDate = tokens[1].trim();
            double price = Double.parseDouble(tokens[2].trim().replaceAll(",", ""));

            Date date = null;

            // Parse unformattedDate into Date date
            try {
                date = new SimpleDateFormat("MMMMM dd, yyyy").parse(unformattedDate);
            } catch (ParseException ignored) {

            }

            displayedRES.add(new Sale(country, price, date));
        }

        return displayedRES; // return the new ArrayList<Sale>
    }

    class ChangeDatesActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            updateSalesJList();
            calculateTotal();
        }
    }

    //  updates the JList based on user selected date range
    public void updateSalesJList() {
        String startDateString = new SimpleDateFormat("MM/dd/yyyy").format(startDateSpinner.getModel().getValue());
        String endDateString = new SimpleDateFormat("MM/dd/yyyy").format(endDateSpinner.getModel().getValue());

        Date startDate = null, endDate = null;

        try {
            startDate = new SimpleDateFormat("MM/dd/yyyy").parse(startDateString);
            endDate = new SimpleDateFormat("MM/dd/yyyy").parse(endDateString);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
        }


            /*for (int i = 0; i < listModel.getSize(); i++) {
                listModel.remove(i);
            }*/

        ArrayList<Sale> tmpRES = new ArrayList<>();


        for (Sale sale : sales) {

            Date dateOfSale = sale.getDate();

            // dateOfSale is equal or after startDate and is before endDate
            if ((dateOfSale.compareTo(startDate) >= 0)) {
                if (dateOfSale.compareTo(endDate) <= 0) {
                    tmpRES.add(sale);
                }
            }
        }

        listModel.removeAllElements();

        for (Sale res : tmpRES) {
            listModel.addElement(res);
        }
    }

    // calculate total sales displayed on JList and displays it on the bottomPanel
    public void calculateTotal() {
        double total = 0;

        ArrayList<Sale> displayedRES = getSalesFromJList();

        for (Sale res : displayedRES) {

            if (CurrencyUtil.checkCountryCurrencyCodeAvailability(res.getCountry())) {
                total += CurrencyUtil.currConvert(res.getCountry(), "USA", res.getPrice());
            } else {
                System.err.println("Currency for " + res.getCountry()
                        + " is not supported. Value not added to total price.");
            }

            // Round the displayed total to the hundredths place.
            total = (double) Math.round(total * 100d) / 100d;

        }

        totalLBL.setText("Total: $" + total);
    }


    // adds entry to JList
    public void addSaleToList(String country, double price, Date date) {

        sales.add(new Sale(country, price, date));
        updateSalesJList();
        calculateTotal();
    }

    // adds entry to JList using a Sale reference
    public void addSaleToList(Sale res) {
        addSaleToList(res.getCountry(), res.getPrice(), res.getDate());
    }

    // to display the JList for Sale
    static class SalesJListRenderer extends DefaultListCellRenderer { // extend
        public Component getListCellRendererComponent(
                JList<?> list,           // the list
                Object value,            // value to display
                int index,               // cell index
                boolean isSelected,      // is the cell selected
                boolean cellHasFocus)    // does the cell have focus
        {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Sale res = (Sale) value;
            setText(res.toString());
            return this;
        }
    }


    // used to add entry to JList AFTER validating user entries.
    class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String userCountryCode = addCountryTF.getText();
            String userPrice = addPriceTF.getText();
            String userDate = addDateTF.getText();

            boolean userCountryCodeisValid = false;
            boolean userPriceisValid = false;
            boolean userDateisValid = false;


            // Validate user country code6
            userCountryCodeisValid = Sale.validateCountryCode(userCountryCode);

            if (!userCountryCodeisValid) {
                JOptionPane.showMessageDialog(bottomPanel, "That is not a valid country code.",
                        "Invalid Country Code",  JOptionPane.ERROR_MESSAGE);
            }

            // Validate user price
            double price =  0;

            if (new Scanner(userPrice).hasNextDouble()) {
                userPriceisValid = Sale.validatePrice(Double.parseDouble(userPrice));
                price = Double.parseDouble(userPrice);
                if (!userPriceisValid) {
                    JOptionPane.showMessageDialog(bottomPanel, "Price must be greater than 0.",
                            "Invalid Price",  JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(bottomPanel, "That is not a valid price.",
                        "Invalid Price",  JOptionPane.ERROR_MESSAGE);
            }

            // Validate user date
            userDateisValid = Sale.validateDate(userDate);
            Date date = null;

            if (userDateisValid) {
                try {
                    date = new SimpleDateFormat("MM/dd/yyyy").parse(userDate);
                } catch (ParseException e) {
                    System.err.println();
                }
            }

            if (!userDateisValid) {
                JOptionPane.showMessageDialog(bottomPanel,
                        "That is not a valid date.\n Valid Example: 12/24/2018", "Invalid Date",
                        JOptionPane.ERROR_MESSAGE);
            }


            // Now: if everything is valid, then add it to the JList
            if (userCountryCodeisValid && userPriceisValid && userDateisValid) {
                // exchangeratesapi.io does not support all currency, so make sure the added country is supported first.
                if (CurrencyUtil.checkCountryCurrencyCodeAvailability(userCountryCode)) {
                    addSaleToList(userCountryCode, price, date);
                    //updateSalesListFromJList();
                } else {
                    JOptionPane.showMessageDialog(bottomPanel,
                            "The currency for this country is not supported in this application.",
                            "Unsupported Country Code",  JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}

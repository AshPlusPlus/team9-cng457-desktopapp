package sample;

import threads.ReviewsThread;
import threads.StatsThread;
import helpers.Product;
import helpers.Review;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Controller {
    // Computers Menu
    @FXML
    private TextField tfCompBrand;
    @FXML
    private TextField tfCompSSize;
    @FXML
    private TextField tfCompSRes;
    @FXML
    private TextField tfCompProc;
    @FXML
    private TextField tfCompMem;
    @FXML
    private TextField tfCompStor;
    @FXML
    private TextField tfCompPrice;
    @FXML
    private RadioButton radiobtnCompAllday;
    @FXML
    private RadioButton radiobtnCompExtralong;
    @FXML
    private CheckBox checkboxCompTouch;
    @FXML
    private CheckBox checkboxCompFace;
    @FXML
    private CheckBox checkboxCompFinger;
    // Phones Menu
    @FXML
    private TextField tfPhoneBrand;
    @FXML
    private TextField tfPhoneSSize;
    @FXML
    private TextField tfPhoneMem;
    @FXML
    private TextField tfPhonePrice;
    @FXML
    private RadioButton radiobtnPhoneAllday;
    @FXML
    private RadioButton radiobtnPhoneExtralong;
    @FXML
    private CheckBox checkboxPhoneTouch;
    @FXML
    private CheckBox checkboxPhoneFace;
    @FXML
    private CheckBox checkboxPhoneFinger;
    @FXML
    private ListView lvProds;
    @FXML
    private ListView lvComp1;
    @FXML
    private ListView lvComp2;

    private ArrayList<Product> products;

    /**
     * Function that handles the event of the "Get Computers" button being pressed.
     * It fetches the computers according the criteria entered by the user and stores them in products list.
     * It also displays computers with their labels in the list view.
     * @param event
     * @throws IOException
     * @throws org.json.simple.parser.ParseException
     */
    public void compBtnPressed(ActionEvent event) throws IOException, org.json.simple.parser.ParseException {
        // making up endpoints strings:
        String brand = "";
        String minss = "";
        String maxss = "";
        String minsr = "";
        String maxsr = "";
        String minproc = "";
        String maxproc = "";
        String minmem = "";
        String maxmem = "";
        String minstor = "";
        String maxstor = "";
        String minprice = "";
        String maxprice = "";
        String battery = "";
        String extras = "";
        String [] feats;
        this.products = new ArrayList<>();
        lvProds.getItems().clear();
        if (tfCompBrand.getText().length() > 0)
            brand = "&brand=" + tfCompBrand.getText();
        if (tfCompSSize.getText().length() > 0) {
            feats = getranges(tfCompSSize,"screensize");
            minss = "&" + feats[0];
            maxss = "&" + feats[1];

        }
        if (tfCompSRes.getText().length() > 0) {
            feats = getranges(tfCompSRes,"resolution");
            minsr = "&" + feats[0];
            maxsr = "&" + feats[1];
        }
        if (tfCompProc.getText().length() > 0) {
            feats = getranges(tfCompProc,"processor");
            minproc = "&" + feats[0];
            maxproc = "&" + feats[1];
        }
        if (tfCompMem.getText().length() > 0) {
            feats = getranges(tfCompMem,"memory");
            minmem = "&" + feats[0];
            maxmem = "&" + feats[1];
        }
        if (tfCompStor.getText().length() > 0) {
            feats = getranges(tfCompStor,"storage");
            minstor = "&" + feats[0];
            maxstor = "&" + feats[1];
        }
        if (tfCompPrice.getText().length() > 0) {
            feats = getranges(tfCompPrice,"price");
            minprice = "&" + feats[0];
            maxprice = "&" + feats[1];
        }
        if (radiobtnCompAllday.isSelected())
            battery = "&batterylife=allday";
        else if (radiobtnCompExtralong.isSelected())
            battery = "&batterylife=extra";
        if (checkboxCompFace.isSelected() || checkboxCompFinger.isSelected() || checkboxCompTouch.isSelected()){
            extras = "&extrafeatures=";
            if (checkboxCompFace.isSelected())
                extras += "facerecognition,";
            if (checkboxCompTouch.isSelected())
                extras += "touchscreen,";
            if (checkboxCompFinger.isSelected())
                extras += "fingerprint,";
        }

        // getting data from web service as JSON objects and parsing it into the list
        // first response is for products with ranges criteria
        Object obj1 = getJSONresponse("getcomputerswithranges?"
                + brand + minss + maxss + minsr + maxsr + minproc + maxproc + minmem + maxmem + minstor + maxstor + minprice + maxprice);
        // second response is for products with battery and extra features criteria
        Object obj2 = getJSONresponse("getcomputers?" + brand + battery + extras);
        JSONArray array1 = (JSONArray) obj1;
        JSONArray array2 = (JSONArray) obj2;
        // getting common products between the 2 responses
        ArrayList<JSONObject> array = getCommonProds((JSONArray) obj1, (JSONArray) obj2);
        // parsing into list
        parseProducts(array);
    }

    /**
     * Function that handles the event of the "Get Phones" button being pressed.
     * It fetches the phones according the criteria entered by the user and stores them in products list.
     * It also displays phones with their labels in the list view.
     * @param event
     * @throws IOException
     * @throws org.json.simple.parser.ParseException
     */
    public void phoneBtnPressed(ActionEvent event) throws IOException, org.json.simple.parser.ParseException {
        // making up endpoints strings:
        String brand = "";
        String minss = "";
        String maxss = "";
        String minmem = "";
        String maxmem = "";
        String minprice = "";
        String maxprice = "";
        String battery = "";
        String extras = "";
        String [] feats;
        this.products = new ArrayList<>();
        lvProds.getItems().clear();
        if (tfPhoneBrand.getText().length() > 0)
            brand = "&brand=" + tfPhoneBrand.getText();
        if (tfPhoneSSize.getText().length() > 0) {
            feats = getranges(tfPhoneSSize,"screensize");
            minss = "&" + feats[0];
            maxss = "&" + feats[1];

        }
        if (tfPhoneMem.getText().length() > 0) {
            feats = getranges(tfPhoneMem,"memory");
            minmem = "&" + feats[0];
            maxmem = "&" + feats[1];
        }
        if (tfPhonePrice.getText().length() > 0) {
            feats = getranges(tfPhonePrice,"price");
            minprice = "&" + feats[0];
            maxprice = "&" + feats[1];
        }
        if (radiobtnPhoneAllday.isSelected())
            battery = "&batterylife=allday";
        else if (radiobtnPhoneExtralong.isSelected())
            battery = "&batterylife=extra";
        if (checkboxPhoneFace.isSelected() || checkboxPhoneFinger.isSelected() || checkboxPhoneTouch.isSelected()){
            extras = "&extrafeatures=";
            if (checkboxPhoneFace.isSelected())
                extras += "facerecognition,";
            if (checkboxPhoneTouch.isSelected())
                extras += "touchscreen,";
            if (checkboxPhoneFinger.isSelected())
                extras += "fingerprint,";
        }

        // getting data from web service as JSON objects and parsing it into the list
        // first response is for products with ranges criteria
        Object obj1 = getJSONresponse("getphoneswithranges?" + brand + minss + maxss + minmem + maxmem + minprice + maxprice);
        // second response is for products with battery and extra features criteria
        Object obj2 = getJSONresponse("getphones?" + brand + battery + extras);
        // getting common products between the 2 responses
        ArrayList<JSONObject> array = getCommonProds((JSONArray) obj1, (JSONArray) obj2);
        // parsing into list
        parseProducts(array);
    }

    /** Function that handles the event of the "Sort" button being pressed.
     *
     * @param e
     */
    public void sortBtnPressed(ActionEvent e){
        // uses comparator class to sort list by descending order of price
        Comparator<Product> compareByPrice = Comparator.comparing(Product::getPrice);
        products.sort(compareByPrice.reversed());
        // displays sorted list in listview
        lvProds.getItems().clear();
        for(Product product:products){
            lvProds.getItems().add(product.getBrand() + " " + product.getModel() + product.getLabel());
        }
    }

    /**
     * Function that handles the event of the "Compare" button being pressed.
     * @param e
     */
    public void compareBtnPressed(ActionEvent e){
        // I create the runnables then I run them using Platform.runlater() function
        // The reason I do this is because I change JavaFX components in the threads in order to
        // display the comparisons in the list views, but the threads are not JavaFX threads
        // and I was getting an IllegalStateException as a result.
        Platform.runLater(new StatsThread(products, lvProds.getSelectionModel().getSelectedIndices(), lvComp1));
        Platform.runLater(new ReviewsThread(products, lvProds.getSelectionModel().getSelectedIndices(), lvComp2));
    }

    /**
     * Function to get the ranges mentioned in the text fields and parse them into a String list.
     * If no range is specified both lower and upper boundaries are equal to specified value.
     * @param tf
     * @param feature
     * @return
     */
    private String[] getranges(TextField tf, String feature){
        String[] ranges;
        String[] feats;
        int size = tf.getText().length();
        if (tf.getText().charAt(size-1) == '-'){
            feats = new String[]{"min_" + feature + "=" + tf.getText().substring(0,size - 1), "max_" + feature + "=" + Integer.MAX_VALUE};
        }
        else if (tf.getText().charAt(0) == '-'){
            feats = new String[]{"min_" + feature + "=" + "0", "max_" + feature + "=" + tf.getText().substring(1,size)};
        }
        else {
            ranges = tf.getText().split("-");
            if (ranges.length > 1) {
                feats = new String[]{"min_" + feature + "=" + ranges[0], "max_" + feature + "=" + ranges[1]};
            } else {
                feats = new String[]{"min_" + feature + "=" + ranges[0], "max_" + feature + "=" + ranges[0]};
            }
        }
        return feats;
    }

    /**
     * Function that makes the request to the webservice and gets the JSON response.
     * @param endpoints
     * @return
     * @throws IOException
     * @throws org.json.simple.parser.ParseException
     */
    private Object getJSONresponse(String endpoints) throws IOException, org.json.simple.parser.ParseException  {
        String response = "";
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/" + endpoints).openConnection();
        connection.setRequestMethod("GET");
        int responsecode = connection.getResponseCode();
        if(responsecode == 200){
            Scanner scanner = new Scanner(connection.getInputStream());
            while(scanner.hasNextLine()){
                response += scanner.nextLine();
                response += "\n";
            }
            scanner.close();
        }
        JSONParser parser = new JSONParser();
        return parser.parse(response);
    }

    /**
     * Function that finds common products between two JSON arrays using product ids.
     * @param array1
     * @param array2
     * @return
     */
    private ArrayList<JSONObject> getCommonProds(JSONArray array1, JSONArray array2) {
        ArrayList<JSONObject> array = new ArrayList<>();
        for (int i = 0; i < array1.size(); i++){
            JSONObject temp = (JSONObject) array1.get(i);
            for (int j = 0; j<array2.size(); j++){
                JSONObject temp2 = (JSONObject) array2.get(j);
                if (temp.get("prod_id") == temp2.get("prod_id"))
                    array.add(temp);
            }
        }
        return array;
    }

    /**
     * Function that parses the JSON reponse data into product objects and adds them to the list.
     * @param array
     */
    private void parseProducts (ArrayList<JSONObject> array){
        for (int i = 0; i < array.size(); i++){
            JSONObject productJSON = (JSONObject) array.get(i);
            JSONArray productfeatures = (JSONArray) productJSON.get("prodFeatures");
            JSONArray reviews = (JSONArray) productJSON.get("comments");
            Product product = new Product();
            try {
                JSONObject brandjson = (JSONObject) productJSON.get("brand");
                product.setBrand((String) brandjson.get("name"));
            }catch(Exception e){
                product.setBrand((String) productJSON.get("brand"));
            }
            product.setId(productJSON.get("prod_id").toString());
            product.setModel((String) productJSON.get("model"));
            product.setPrice(Double.parseDouble(productJSON.get("price").toString()));

            for (int j = 0; j < productfeatures.size(); j++){
                JSONObject prodfeatureJSON = (JSONObject) productfeatures.get(j);
                JSONObject prodfeatureIdJSON = (JSONObject) prodfeatureJSON.get("id");
                String featurename = (String) prodfeatureIdJSON.get("feat_name");
                if(featurename.equals("memory"))
                    product.setMemory((String) prodfeatureJSON.get("value"));
                if(featurename.equals("processor"))
                    product.setProcessor((String) prodfeatureJSON.get("value"));
                if(featurename.equals("screen_resolution"))
                    product.setResolution((String) prodfeatureJSON.get("value"));
                if(featurename.equals("screen_size"))
                    product.setScreensize((String) prodfeatureJSON.get("value"));
                if(featurename.equals("memory"))
                    product.setMemory((String) prodfeatureJSON.get("value"));
                if(featurename.equals("storage_capacity"))
                    product.setStorage((String) prodfeatureJSON.get("value"));
                if(featurename.equals("battery_life_all_day"))
                    product.setBattery("All Day Battery");
                else if(featurename.equals("battery_life_extra_long"))
                    product.setBattery("Extra Long Battery");
                if (featurename.equals("face_recognition"))
                    product.setFace("Face Recognition");
                if (featurename.equals("fingerprint_reader"))
                    product.setFinger("Fingerprint Reader");
                if (featurename.equals("touchscreen"))
                    product.setTouch("Touchscreen");
            }

            Review review;
            for (int j = 0; j < reviews.size(); j++) {
                JSONObject temp3 = (JSONObject) reviews.get(j);
                JSONObject temp4 = (JSONObject) temp3.get("id");
                review  = new Review();;
                review.setId((Long) temp4.get("com_id"));
                if (temp3.get("rating")!=null)
                    review.setRating(temp3.get("rating").toString());
                if (temp3.get("message")!=null)
                    review.setComment((String) temp3.get("message"));
                product.getReviews().add(review);
            }
            Comparator<Review> compareById = Comparator.comparing(Review::getId);
            product.getReviews().sort(compareById.reversed());

            product.setLabel(" ");
            if (product.getScreensize() != null && Double.parseDouble(product.getScreensize()) > 6)
                product.setLabel(product.getLabel() + " Large Screen");
            if (product.getMemory() != null && Integer.parseInt(product.getMemory()) > 128)
                product.setLabel(product.getLabel() + " Large Storage");
            this.products.add(product);

            lvProds.getItems().add(product.getBrand() + " " + product.getModel() + product.getLabel());
        }
    }

    /**
     * Function that puts restrictions on what the user can enter in features textfields.
     * Restrictions are that the user can only enter numbers or 1 dash character for making ranges.
     * @param tf
     */
    private void inputRestrictions(TextField tf){
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length()<oldValue.length() && newValue.length() == 0)
                newValue = "a";
            if (!Character.isDigit(newValue.charAt(newValue.length() - 1))) {
                if ((newValue.charAt(newValue.length() - 1) == '-') && !newValue.substring(0,newValue.length() - 1).contains("-")){
                   tf.setText(newValue);
               }
                else if (newValue.length()>1) {
                    StringBuilder str = new StringBuilder(newValue);
                    str.deleteCharAt(newValue.length() - 1);
                    tf.setText(str.toString());
                }
                else{
                    tf.setText("");
                }
            }
        });
    }

    /**
     * Function for initialization.
     */
    public void initialize(){
        inputRestrictions(tfCompSSize);
        inputRestrictions(tfCompMem);
        inputRestrictions(tfCompSRes);
        inputRestrictions(tfCompProc);
        inputRestrictions(tfCompStor);
        inputRestrictions(tfCompPrice);
        inputRestrictions(tfPhoneSSize);
        inputRestrictions(tfPhoneMem);
        inputRestrictions(tfPhonePrice);
        lvProds.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


    }

}

package sample;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.concurrent.RecursiveAction;

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
    @FXML
    private Button btnGetComps;
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
    private Button btnGetPhones;
    @FXML
    private ListView lvProds;
    @FXML
    private ListView lvComp1;
    @FXML
    private ListView lvComp2;
    @FXML
    private Button btnSort;

    private ArrayList<Product> products;


    public void compBtnPressed(ActionEvent event) throws IOException, ParseException, org.json.simple.parser.ParseException {
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



        String response = "";

        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/getcomputerswithranges?"
        + brand + minss + maxss + minsr + maxsr + minproc + maxproc + minmem + maxmem + minstor + maxstor + minprice + maxprice).openConnection();
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
        Object obj1 = parser.parse(response);
        JSONArray array1 = (JSONArray) obj1;
        response = "";
        connection = (HttpURLConnection) new URL("http://localhost:8080/getcomputers?"
                + brand + battery + extras).openConnection();
        connection.setRequestMethod("GET");
        responsecode = connection.getResponseCode();
        if(responsecode == 200){
            Scanner scanner = new Scanner(connection.getInputStream());
            while(scanner.hasNextLine()){
                response += scanner.nextLine();
                response += "\n";
            }
            scanner.close();
        }
        parser = new JSONParser();
        Object obj2 = parser.parse(response);
        JSONArray array2 = (JSONArray) obj2;

        ArrayList<JSONObject> array = new ArrayList<>();


        for (int i = 0; i < array1.size(); i++){
            JSONObject temp1 = (JSONObject) array1.get(i);
            for (int j = 0; j<array2.size(); j++){
                JSONObject temp2 = (JSONObject) array2.get(j);
                if (temp1.get("prod_id") == temp2.get("prod_id"))
                    array.add(temp1);
            }
        }

        for (int i = 0; i < array.size(); i++){
            JSONObject temp = (JSONObject) array.get(i);


            JSONArray productfeatures = (JSONArray) temp.get("prodFeatures");

            JSONArray reviews = (JSONArray) temp.get("comments");
            Product product = new Product();
            try {
                JSONObject brandjson = (JSONObject) temp.get("brand");
                product.setBrand((String) brandjson.get("name"));
            }catch(Exception e){
                product.setBrand((String) temp.get("brand"));
            }
            product.setId(temp.get("prod_id").toString());
            product.setModel((String) temp.get("model"));
            product.setPrice(Double.parseDouble(temp.get("price").toString()));

            for (int j = 0; j < productfeatures.size(); j++){
                JSONObject temp2 = (JSONObject) productfeatures.get(j);
                JSONObject temp2id = (JSONObject) temp2.get("id");
                String featurename = (String) temp2id.get("feat_name");
                if(featurename.equals("memory"))
                    product.setMemory((String) temp2.get("value"));
                if(featurename.equals("processor"))
                    product.setProcessor((String) temp2.get("value"));
                if(featurename.equals("screen_resolution"))
                    product.setResolution((String) temp2.get("value"));
                if(featurename.equals("screen_size"))
                    product.setScreensize((String) temp2.get("value"));
                if(featurename.equals("memory"))
                    product.setMemory((String) temp2.get("value"));
                if(featurename.equals("storage_capacity"))
                    product.setStorage((String) temp2.get("value"));
                if(featurename.equals("battery_life_all_day"))
                    product.setBattery("All Day Battery");
                else if(featurename.equals("battery_life_extra_long"))
                    product.setBattery("Extra Long Battery");
                if (featurename.equals("face_recognition"))
                    product.setFace("Face Recognition");
                if (featurename.equals("fingerprint_reader"))
                    product.setFinger("Fingerprint Reader");
                if (featurename.equals("touchscreen"))
                    product.setFace("Touchscreen");
            }
            Review review;
            for (int j =0; j<reviews.size();j++){
                JSONObject temp3 = (JSONObject) reviews.get(j);
                review  = new Review();
                if (temp3.get("rating")!=null)
                    review.setRating(temp3.get("rating").toString());
                if (temp3.get("message")!=null)
                    review.setComment((String) temp3.get("message"));
                product.getReviews().add(review);
            }
            product.setLabel(" ");
            if (Integer.parseInt(product.getMemory()) > 16)
                product.setLabel(product.getLabel() + "Large Memory ");
            if (Integer.parseInt(product.getStorage()) > 1000)
                product.setLabel(product.getLabel() + "Large Storage");
            lvProds.getItems().add(product.getBrand() + " " + product.getModel() + product.getLabel());
            this.products.add(product);
        }

    }

    public void phoneBtnPressed(ActionEvent event) throws IOException, ParseException, org.json.simple.parser.ParseException {
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



        String response = "";

        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/getphoneswithranges?"
                + brand + minss + maxss + minsr + maxsr + minproc + maxproc + minmem + maxmem + minstor + maxstor + minprice + maxprice).openConnection();
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
        Object obj1 = parser.parse(response);
        JSONArray array1 = (JSONArray) obj1;
        response = "";
        connection = (HttpURLConnection) new URL("http://localhost:8080/getphones?"
                + brand + battery + extras).openConnection();
        connection.setRequestMethod("GET");
        responsecode = connection.getResponseCode();
        if(responsecode == 200){
            Scanner scanner = new Scanner(connection.getInputStream());
            while(scanner.hasNextLine()){
                response += scanner.nextLine();
                response += "\n";
            }
            scanner.close();
        }
        parser = new JSONParser();
        Object obj2 = parser.parse(response);
        JSONArray array2 = (JSONArray) obj2;

        ArrayList<JSONObject> array = new ArrayList<>();


        for (int i = 0; i < array1.size(); i++){
            JSONObject temp1 = (JSONObject) array1.get(i);
            for (int j = 0; j<array2.size(); j++){
                JSONObject temp2 = (JSONObject) array2.get(j);
                if (temp1.get("prod_id") == temp2.get("prod_id"))
                    array.add(temp1);
            }
        }

        for (int i = 0; i < array.size(); i++){
            JSONObject temp = (JSONObject) array.get(i);


            JSONArray productfeatures = (JSONArray) temp.get("prodFeatures");

            JSONArray reviews = (JSONArray) temp.get("comments");
            Product product = new Product();
            try {
                JSONObject brandjson = (JSONObject) temp.get("brand");
                product.setBrand((String) brandjson.get("name"));
            }catch(Exception e){
                product.setBrand((String) temp.get("brand"));
            }
            product.setId(temp.get("prod_id").toString());
            product.setModel((String) temp.get("model"));
            product.setPrice(Double.parseDouble(temp.get("price").toString()));

            for (int j = 0; j < productfeatures.size(); j++){
                JSONObject temp2 = (JSONObject) productfeatures.get(j);
                JSONObject temp2id = (JSONObject) temp2.get("id");
                String featurename = (String) temp2id.get("feat_name");
                if(featurename.equals("memory"))
                    product.setMemory((String) temp2.get("value"));
                if(featurename.equals("screen_size"))
                    product.setScreensize((String) temp2.get("value"));
                if(featurename.equals("memory"))
                    product.setMemory((String) temp2.get("value"));
                if(featurename.equals("battery_life_all_day"))
                    product.setBattery("All Day Battery");
                else if(featurename.equals("battery_life_extra_long"))
                    product.setBattery("Extra Long Battery");
                if (featurename.equals("face_recognition"))
                    product.setFace("Face Recognition");
                if (featurename.equals("fingerprint_reader"))
                    product.setFinger("Fingerprint Reader");
                if (featurename.equals("touchscreen"))
                    product.setFace("Touchscreen");
            }
            for (int j = 0; j < reviews.size(); j++){
                JSONObject temp3 = (JSONObject) reviews.get(j);
                product.getReviews().add(new Review((String) temp3.get("rating"), (String) temp3.get("message")));
            }
            product.setLabel(" ");
            if (Double.parseDouble(product.getScreensize()) > 6)
                product.setLabel(product.getLabel() + " Large Screen");
            if (Integer.parseInt(product.getMemory()) > 128)
                product.setLabel(product.getLabel() + " Large Storage");
            lvProds.getItems().add(product.getBrand() + " " + product.getModel() + product.getLabel());
            this.products.add(product);
        }

    }

    public void sortBtnPressed(ActionEvent e){
        Comparator<Product> compareByPrice = Comparator.comparing(Product::getPrice);
        products.sort(compareByPrice.reversed());
        lvProds.getItems().clear();
        for(Product product:products){
            lvProds.getItems().add(product.getBrand() + " " + product.getModel() + product.getLabel());
        }
    }

    public void compareBtnPressed(ActionEvent e){
        lvComp1.getItems().clear();
        lvComp2.getItems().clear();
        Product product = null;
        for (int i = 0; i<lvProds.getSelectionModel().getSelectedIndices().size(); i++) {
            product = products.get((int) lvProds.getSelectionModel().getSelectedIndices().get(i));
            lvComp1.getItems().add("Product " + (i+1));
            lvComp1.getItems().add("\tBrand: " + product.getBrand());
            lvComp1.getItems().add("\tModel: " + product.getModel());
            lvComp1.getItems().add("\tPrice: " + product.getPrice());
            lvComp1.getItems().add("\tScreen Size: " + product.getScreensize());
            lvComp1.getItems().add("\tMemory: " + product.getMemory());
            lvComp1.getItems().add("\tBattery: " + product.getBattery());
            if(product.getStorage()!=null) {
                lvComp1.getItems().add("\tStorage: " + product.getStorage());
                lvComp1.getItems().add("\tProcessor: " + product.getProcessor());
                lvComp1.getItems().add("\tResolution: " + product.getResolution());
            }
            if (product.getFace()!=null)
                lvComp1.getItems().add("\tFace Recognition: ");
            if (product.getFinger()!=null)
                lvComp1.getItems().add("\tFingerprint Reader: ");
            if (product.getTouch()!=null)
                lvComp1.getItems().add("\tTouchscreen: ");

            lvComp2.getItems().add("Product " + (i+1));
            int j = 0;
            for (Review review: product.getReviews()){
                lvComp2.getItems().add("Rating: " + review.getRating());
                if (review.getComment()!=null)
                    lvComp2.getItems().add("Comment: " + review.getComment());
                j++;
                if (j==3)
                    break;
            }
        }

    }


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

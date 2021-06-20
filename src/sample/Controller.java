package sample;

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
            feats = getranges(tfCompSRes,"memory");
            minmem = "&" + feats[0];
            maxmem = "&" + feats[1];
        }
        if (tfCompStor.getText().length() > 0) {
            feats = getranges(tfCompSRes,"storage");
            minstor = "&" + feats[0];
            maxstor = "&" + feats[1];
        }
        if (tfCompPrice.getText().length() > 0) {
            feats = getranges(tfCompSRes,"price");
            minprice = "&" + feats[0];
            maxprice = "&" + feats[1];
        }
        if (radiobtnCompAllday.isSelected())
            battery = "&batterylife=allday";
        else
            battery = "&batterylife=extra";
        if (checkboxCompFace.isSelected() || checkboxCompFinger.isSelected() || checkboxCompTouch.isSelected()){
            extras = "& extrafeatures=";
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
                + battery + extras).openConnection();
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
        System.out.println(array1.size());
        System.out.println(array2.size());

        for (int i = 0; i < array1.size(); i++){
            JSONObject temp1 = (JSONObject) array1.get(i);
            for (int j = 0; j<array2.size(); j++){
                JSONObject temp2 = (JSONObject) array2.get(j);
                if (temp1.equals(temp2))
                    array.add(temp1);
            }
        }
        System.out.println(array.size());
        for (int i = 0; i < array.size(); i++){
            JSONObject temp = (JSONObject) array.get(i);
            JSONObject brandjson = (JSONObject) temp.get("brand");
            JSONArray productfeatures = (JSONArray) temp.get("prod_features");
            JSONArray reviews = (JSONArray) temp.get("comments");
            Product product = new Product();
            product.setId(temp.get("prod_id").toString());
            product.setModel((String) temp.get("model"));
            product.setPrice(temp.get("price").toString());
            product.setBrand((String) brandjson.get("name"));
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
            for (int j =0; j<reviews.size();j++){
                JSONObject temp3 = (JSONObject) reviews.get(j);
                product.getReviews().add(new Review((String) temp3.get("rating"), (String) temp3.get("message")));
            }

            System.out.println(product.getBrand() + product.getModel());
            this.products.add(product);
        }
        //HBox hbox = new HBox(lvProds);




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

    }





}

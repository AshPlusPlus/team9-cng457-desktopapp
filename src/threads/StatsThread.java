package threads;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import helpers.Product;

import java.util.ArrayList;

public class StatsThread implements Runnable {
    ArrayList<Product> products;
    ObservableList<Integer> selections;
    ListView lv;
    public StatsThread(ArrayList<Product> products, ObservableList<Integer> selections, ListView lv) {
        this.products = products;
        this.selections = selections;
        this.lv = lv;
    }

    @Override
    public void run() {
        Product product;
        lv.getItems().clear();
        for (int i = 0; i < Math.min(3, selections.size()); i++){
            product = products.get((int) selections.get(i));
            lv.getItems().add("Product " + (i+1) + ":");
            if (product.getBrand() != null)
                lv.getItems().add("\tBrand: " + product.getBrand());
            if (product.getModel() != null)
                lv.getItems().add("\tModel: " + product.getModel());
            if (product.getPrice() != null)
                lv.getItems().add("\tPrice: " + product.getPrice());
            if (product.getScreensize() != null)
                lv.getItems().add("\tScreen Size: " + product.getScreensize());
            if (product.getMemory() != null)
                lv.getItems().add("\tMemory: " + product.getMemory());
            if (product.getBattery() != null)
                lv.getItems().add("\tBattery: " + product.getBattery());
            if(product.getStorage()!=null)
                lv.getItems().add("\tStorage: " + product.getStorage());
            if (product.getProcessor() != null)
                lv.getItems().add("\tProcessor: " + product.getProcessor());
            if (product.getResolution() != null)
                lv.getItems().add("\tResolution: " + product.getResolution());
            if (product.getFace()!=null)
                lv.getItems().add("\tFace Recognition ");
            if (product.getFinger()!=null)
                lv.getItems().add("\tFingerprint Reader ");
            if (product.getTouch()!=null)
                lv.getItems().add("\tTouchscreen ");
        }
    }
}

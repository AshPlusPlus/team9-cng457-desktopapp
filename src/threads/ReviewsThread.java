package threads;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import helpers.Product;
import helpers.Review;

import java.util.ArrayList;

public class ReviewsThread implements Runnable {
    ArrayList<Product> products;
    ObservableList<Integer> selections;
    ListView lv;
    public ReviewsThread(ArrayList<Product> products, ObservableList<Integer> selections, ListView lv) {
        this.products = products;
        this.selections = selections;
        this.lv = lv;
    }

    @Override
    public void run() {
        Product product;
        double avgrating;
        lv.getItems().clear();
        for (int i = 0; i < Math.min(3, selections.size()); i++) {
            product = products.get((int) selections.get(i));
            lv.getItems().add("Product " + (i+1) + ":");
            avgrating = 0.0;
            for (int j = 0; j < product.getReviews().size(); j++)
                avgrating += Double.parseDouble(product.getReviews().get(j).getRating());
            if (product.getReviews().size() > 0)
                avgrating /= product.getReviews().size();
            lv.getItems().add("\tAverage Rating = " + avgrating);
            lv.getItems().add("\tMost Recent Reviews:");
            int j = 0;
            for (Review review: product.getReviews()){
                lv.getItems().add("\t\tRating: " + review.getRating());
                if (review.getComment()!=null)
                    lv.getItems().add("\t\tComment: " + review.getComment());
                j++;
                if (j==3)
                    break;
            }
        }
    }
}

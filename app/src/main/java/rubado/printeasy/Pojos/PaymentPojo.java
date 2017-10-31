package rubado.printeasy.Pojos;

/**
 * Created by Sol Rubado on 10/04/2017.
 */

public class PaymentPojo {
    private String id;
    private String filePrinted;
    private int pages;
    private double price;
    private String created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilePrinted() {
        return filePrinted;
    }

    public void setFilePrinted(String filePrinted) {
        this.filePrinted = filePrinted;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}

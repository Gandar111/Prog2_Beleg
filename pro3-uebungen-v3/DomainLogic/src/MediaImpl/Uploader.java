package MediaImpl;

public class Uploader implements contract.Uploader{

String producer;


    public Uploader(String producer) {
        this.producer = producer;
    }

    @Override
    public String getName() {
        return "";
    }
}

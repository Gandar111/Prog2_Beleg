package MediaImpl;
import java.io.Serializable;
public class Uploader implements contract.Uploader, Serializable {

String producer;


    public Uploader(String producer) {
        this.producer = producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getProducer() {
        return producer;
    }

    @Override
    public String getName() {
        return this.producer;
    }
}

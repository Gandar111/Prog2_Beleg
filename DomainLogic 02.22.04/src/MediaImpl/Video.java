package MediaImpl;

import contract.Tag;
import contract.Uploader;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.io.Serializable;
import java.util.List;

public class Video implements contract.MediaContent, contract.Uploadable, Serializable {
    private List<Tag> tags;
    private long accessCount;
    private String address;
    private long size;
    private Uploader uploader;
    private Duration duration;
    private BigDecimal volume;

    public Video(List<Tag> tags, long accessCount, String address, long size, Uploader uploader, Duration duration, BigDecimal volume) {
        this.tags = tags;
        this.accessCount = accessCount;
        this.address = address;
        this.size = size;
        this.uploader = uploader;
        this.duration = duration;
        this.volume = volume;
    }


    public String getType() {
        return "video";
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public Collection<Tag> getTags() {
        return tags;
    }

    @Override
    public long getAccessCount() {
        return accessCount;
    }

    @Override
    public void incrementAccessCount() {
        accessCount++;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public Uploader getUploader() {
        return uploader;
    }

    @Override
    public Duration getAvailability() {
        return duration;
    }

    @Override
    public BigDecimal getCost() {
        return volume;
    }

    @Override
    public String toString() {
        return "Video{" +
                "tags=" + tags +
                ", accessCount=" + accessCount +
                ", address='" + address + '\'' +
                ", size=" + size +
                ", uploader=" + uploader +
                ", duration=" + duration +
                ", volume=" + volume +
                '}';
    }

    public Duration getDuration() {
        return duration;
    }

    public BigDecimal getVolume() {
        return volume;
    }
}

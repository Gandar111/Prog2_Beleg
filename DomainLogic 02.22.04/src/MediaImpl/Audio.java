package MediaImpl;

import contract.Tag;
import contract.Uploader;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.io.Serializable;

public class Audio implements contract.Audio , Serializable  {
    private final List<Tag> tags;
    private long accessCount;
    private final String address;
    private long size;
    private final Uploader uploader;
    private final Duration duration;
    private final BigDecimal volume;

    public Audio(List<Tag> tags, long accessCount, String address, long size, Uploader uploader, Duration duration, BigDecimal volume) {
        this.tags = tags;
        this.accessCount = accessCount;
        this.address = address;
        this.size = size;
        this.uploader = uploader;
        this.duration = duration;
        this.volume = volume;
    }

    @Override
    public int getSamplingRate() {

        return 44100;
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
        return "Audio{" +
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
    public String getType() {
        return "audio";
    }
}

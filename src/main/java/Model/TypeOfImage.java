package Model;

public enum TypeOfImage {
    RGB(3),
    HSV(3),
    HSI(3),
    YIQ(3),
    CMY(3),
    CMYK(4),
    LMS(3),
    LAB(3);

    private final int channels;

    TypeOfImage(int channels) {
        this.channels = channels;
    }

    public int getChannels() {
        return channels;
    }
}


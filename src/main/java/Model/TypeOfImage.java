package Model;

public enum TypeOfImage {
    RGB(3, "RGB"),
    HSV(3, "HSV"),
    HSI(3, "HSI"),
    YIQ(3, "YIQ"),
    CMY(3, "CMY"),
    CMYK(4, "CMYK"),
    LMS(3, "LMS"),
    LAB(3, "LAB");

    private final int numberOfChannels;
    private final String nameOfChannels;

    TypeOfImage(int channels, String nameOfChannels) {
        this.numberOfChannels = channels;
        this.nameOfChannels = nameOfChannels;
    }

    public int getNumberOfChannels() {
        return numberOfChannels;
    }

    public String getNameOfChannels() {return nameOfChannels;}
}


package pl.representation;

public class ContentFormatterImpl implements ContentFormatter {

    private static final String PUNCTIATION_REGEX = "[!\"#$%&'()*+," +
            "\\-./:;<=>?@\\[" +
            "\\\\\\]^_`{|}~]";

    @Override
    public String format(String content) {
        return content.replaceAll(PUNCTIATION_REGEX, "").toLowerCase().trim();
    }
}

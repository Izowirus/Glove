package pl.representation;

public class ContentSanitizerImpl implements ContentSanitizer {

    private static final String PUNCTIATION_REGEX = "[!\"#$%&'()*+," +
            "\\-./:;<=>?@\\[" +
            "\\\\\\]^_`{|}~]";

    @Override
    public String sanitize(String content) {
        return content.replaceAll(PUNCTIATION_REGEX, "").toLowerCase().trim();
    }
}

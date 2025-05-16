package org.example.utils;

public class StringReplacement {
    public static void main(String[] args) {
        // Original XPath string with dynamic value
        String original = "//span[text()='TEXT_VALUE']";

        // Replace "[text()='DYNAMIC_VALUE']" with "[contains(text(),'DYNAMIC_VALUE')]"
        String updated = original.replaceAll("\\[text\\(\\)='(.*?)'\\]", "[contains(text(),'$1')]");

        // Output the updated string
        System.out.println("Updated String: " + updated);
        System.out.println("Original String: " + original);

        String xpath = "//span[contains(text(),'TEXT_VALUE')]/div[@class='btn']";

        String updatedxpath = xpath.replaceAll("(?<!/)/(?!/)", "//");
        System.out.println("Updated String: " + xpath);
        System.out.println("Original String: " + updatedxpath);

        String css = "tagName[attributeName=’attributeValue’]";
        String updatedcss = css.replaceAll("=", "*=");

        System.out.println("Updated String: " + css);
        System.out.println("Original String: " + updatedcss);

        String url = "https://www.amazon.com/";
        String extractedDomain = url.replace("https://", "").split("/")[0];
        System.out.println("Extracted Domain: " + extractedDomain.split("\\.")[1]);
    }
}
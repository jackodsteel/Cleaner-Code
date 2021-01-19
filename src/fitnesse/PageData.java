package fitnesse;

public class PageData {
    public WikiPage getWikiPage() {
        return new WikiPage();
    }

    public String getHtml() {
        return "";
    }

    public boolean hasAttribute(String attr) {
        return false;
    }

    public String getContent() {
        return "";
    }

    public void setContent(String content) {
        // Stub
    }
}

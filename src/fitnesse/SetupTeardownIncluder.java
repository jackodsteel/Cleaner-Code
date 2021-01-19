package fitnesse;

public class SetupTeardownIncluder {
    private final PageData pageData;
    private final WikiPage testPage;
    private final StringBuffer newPageContent;
    private final PageCrawler pageCrawler;


    public static String render(PageData pageData) throws Exception {
        return render(pageData, false);
    }

    public static String render(PageData pageData, boolean isSuite)
            throws Exception {
        return new SetupTeardownIncluder(pageData).render(isSuite);
    }

    private SetupTeardownIncluder(PageData pageData) {
        this.pageData = pageData;
        testPage = pageData.getWikiPage();
        pageCrawler = testPage.getPageCrawler();
        newPageContent = new StringBuffer();
    }

    private String render(boolean isSuite) throws Exception {
        if (isTestPage()) {
            if (isSuite) {
                include(SuiteResponder.SUITE_SETUP_NAME, "-setup");
            }
            include("SetUp", "-setup");
            newPageContent.append(pageData.getContent());
            include("TearDown", "-teardown");
            if (isSuite) {
                include(SuiteResponder.SUITE_TEARDOWN_NAME, "-teardown");
            }
            pageData.setContent(newPageContent.toString());
        }
        return pageData.getHtml();
    }

    private boolean isTestPage() throws Exception {
        return pageData.hasAttribute("Test");
    }

    private void include(String pageName, String arg) throws Exception {
        WikiPage inheritedPage = PageCrawlerImpl.getInheritedPage(pageName, testPage);
        if (inheritedPage != null) {
            String pagePathName = getPathNameForPage(inheritedPage);
            buildIncludeDirective(pagePathName, arg);
        }
    }

    private String getPathNameForPage(WikiPage page) throws Exception {
        WikiPagePath pagePath = pageCrawler.getFullPath(page);
        return PathParser.render(pagePath);
    }

    private void buildIncludeDirective(String pagePathName, String arg) {
        newPageContent
                .append("\n!include ")
                .append(arg)
                .append(" .")
                .append(pagePathName)
                .append("\n");
    }
}
package fitnesse;

public class SetupTeardownIncluder {
    private final PageData pageData;
    private final WikiPage testPage;
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
    }

    private String render(boolean isSuite) throws Exception {
        if (isTestPage()) {
            StringBuffer newPageContent = new StringBuffer();
            if (isSuite) {
               newPageContent.append(include(SuiteResponder.SUITE_SETUP_NAME, "-setup"));
            }
            newPageContent.append(include("SetUp", "-setup"));
            newPageContent.append(pageData.getContent());
            newPageContent.append(include("TearDown", "-teardown"));
            if (isSuite) {
                newPageContent.append(include(SuiteResponder.SUITE_TEARDOWN_NAME, "-teardown"));
            }
            pageData.setContent(newPageContent.toString());
        }
        return pageData.getHtml();
    }

    private boolean isTestPage() throws Exception {
        return pageData.hasAttribute("Test");
    }

    private String include(String pageName, String arg) throws Exception {
        WikiPage inheritedPage = PageCrawlerImpl.getInheritedPage(pageName, testPage);
        if (inheritedPage != null) {
            String pagePathName = getPathNameForPage(inheritedPage);
            return buildIncludeDirective(pagePathName, arg);
        }
        return "";
    }

    private String getPathNameForPage(WikiPage page) throws Exception {
        WikiPagePath pagePath = pageCrawler.getFullPath(page);
        return PathParser.render(pagePath);
    }

    private String buildIncludeDirective(String pagePathName, String arg) {
        return String.format("\n!include %s .%s\n", arg, pagePathName);
    }
}
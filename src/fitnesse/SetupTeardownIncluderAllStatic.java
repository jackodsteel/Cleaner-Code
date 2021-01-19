package fitnesse;

public class SetupTeardownIncluderAllStatic {

    public static String render(PageData pageData) throws Exception {
        return render(pageData, false);
    }

    private static String render(PageData pageData, boolean isSuite) throws Exception {
        if (isTestPage(pageData)) {
            WikiPage wikiPage = pageData.getWikiPage();
            StringBuffer newPageContent = new StringBuffer();
            if (isSuite) {
                newPageContent.append(getIncludePageDirective(wikiPage, SuiteResponder.SUITE_SETUP_NAME, "-setup"));
            }
            newPageContent.append(getIncludePageDirective(wikiPage, "SetUp", "-setup"));
            newPageContent.append(pageData.getContent());
            newPageContent.append(getIncludePageDirective(wikiPage, "TearDown", "-teardown"));
            if (isSuite) {
                newPageContent.append(getIncludePageDirective(wikiPage, SuiteResponder.SUITE_TEARDOWN_NAME, "-teardown"));
            }
            pageData.setContent(newPageContent.toString());
        }
        return pageData.getHtml();
    }

    private static boolean isTestPage(PageData pageData) throws Exception {
        return pageData.hasAttribute("Test");
    }

    private static String getIncludePageDirective(WikiPage wikiPage, String pageName, String arg) throws Exception {
        WikiPage inheritedPage = PageCrawlerImpl.getInheritedPage(pageName, wikiPage);
        if (inheritedPage != null) {
            String pagePathName = getPathNameForPage(wikiPage.getPageCrawler(), inheritedPage);
            return buildIncludeDirective(pagePathName, arg);
        }
        return "";
    }

    private static String getPathNameForPage(PageCrawler pageCrawler, WikiPage page) throws Exception {
        WikiPagePath pagePath = pageCrawler.getFullPath(page);
        return PathParser.render(pagePath);
    }

    private static String buildIncludeDirective(String pagePathName, String arg) {
        return String.format("\n!include %s .%s\n", arg, pagePathName);
    }
}
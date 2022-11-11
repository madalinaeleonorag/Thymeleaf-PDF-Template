package services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import types.TableData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class PDFDocumentUtil {

    public static final int MAX_NR_ROWS_FIT_ON_FIRST_PAGE = 25;
    public static final int MAX_NR_ROWS_FIT_ON_FIRST_PAGE_WITH_SIGNATURE = 18;
    public static final int MAX_NR_ROWS_FIT_ON_NORMAL_PAGE = 36;
    public static final int MAX_NR_ROWS_FIT_ON_NORMAL_PAGE_WITH_SIGNATURE_ON_THE_SAME_PAGE = 27;
    static List<TableData> tableListData = new ArrayList<>();
    static Context context = new Context();

    public static void main(String[] args) {
        System.out.println("test");
    }

    public PDFDocumentUtil(List<TableData> tableListData) {
        this.tableListData = tableListData;
    }

    public static Context setupData() {
        Map<String, Object> mapFirstPageData = new HashMap<>();
        Map<String, Object> mapOtherPagesData = new HashMap<>();
        Map<String, Object> setSpaceBeforeSignature = new HashMap<>();
        List<List<TableData>> otherPagesList = new ArrayList<>();

        int nrOfPdfPages = 0;

        if (tableListData.size() != 0) {

//          One page with signature on same page
            if (tableListData.size() <= MAX_NR_ROWS_FIT_ON_FIRST_PAGE_WITH_SIGNATURE) {
                mapFirstPageData.put("dataFirstPage", new ArrayList<>(tableListData));
                nrOfPdfPages = 1;

//          One page with signature on next page
            } else if (tableListData.size() <= MAX_NR_ROWS_FIT_ON_FIRST_PAGE) {
                mapFirstPageData.put("dataFirstPage", new ArrayList<>(tableListData));
                nrOfPdfPages = 1;

//              Set space by filling the missed lines and header/footer space
                if (tableListData.size() > MAX_NR_ROWS_FIT_ON_FIRST_PAGE_WITH_SIGNATURE) {
                    setSpaceBeforeSignature.put("setSpaceBeforeSignature", MAX_NR_ROWS_FIT_ON_FIRST_PAGE - tableListData.size() + 3);
                }

//          Multiple pages
            } else {
//              Set first page content
                mapFirstPageData.put("dataFirstPage", new ArrayList<>(tableListData.subList(0, MAX_NR_ROWS_FIT_ON_FIRST_PAGE)));

                int nrOfRows = MAX_NR_ROWS_FIT_ON_FIRST_PAGE;
                int size = tableListData.size() - MAX_NR_ROWS_FIT_ON_FIRST_PAGE;

                while (size > 0) {
                    if ((nrOfRows + MAX_NR_ROWS_FIT_ON_NORMAL_PAGE) < tableListData.size()) {
                        otherPagesList.add(new ArrayList<>(tableListData.subList(nrOfRows, nrOfRows + MAX_NR_ROWS_FIT_ON_NORMAL_PAGE)));
                        nrOfRows = nrOfRows + MAX_NR_ROWS_FIT_ON_NORMAL_PAGE;
                    } else {
                        otherPagesList.add(new ArrayList<>(tableListData.subList(nrOfRows, tableListData.size())));
                        break;
                    }
                }

//              Set space by filling the missed lines and header/footer space
                if (otherPagesList.get(otherPagesList.size() - 1).size() > MAX_NR_ROWS_FIT_ON_NORMAL_PAGE_WITH_SIGNATURE_ON_THE_SAME_PAGE) {
                    setSpaceBeforeSignature.put("setSpaceBeforeSignature", MAX_NR_ROWS_FIT_ON_NORMAL_PAGE - otherPagesList.get(otherPagesList.size() - 1).size() + 3);
                }

                nrOfPdfPages = 1 + otherPagesList.size();
            }

            mapOtherPagesData.put("dataOtherPages", otherPagesList);
            context.setVariable("numberOfPages", nrOfPdfPages);
            context.setVariables(setSpaceBeforeSignature);
            context.setVariables(mapFirstPageData);
            context.setVariables(mapOtherPagesData);
        }

        return context;
    }
}

package club.swdev.webapp.util;

import club.swdev.webapp.model.Organization;

public class UtilHtml {
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String formatDates(Organization.Activity activity) {
        return UtilDates.format(activity.getStartDate()) + " - " + UtilDates.format(activity.getEndDate());
    }

}


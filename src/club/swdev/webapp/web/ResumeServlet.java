package club.swdev.webapp.web;

import club.swdev.webapp.AppConfig;
import club.swdev.webapp.model.*;
import club.swdev.webapp.storage.Storage;
import club.swdev.webapp.util.UtilDates;
import club.swdev.webapp.util.UtilHtml;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResumeServlet extends HttpServlet {

    private enum THEME {
        dark, light, purple
    }

    private final Storage storage = AppConfig.getConfigInstance().getStorage();
    private final Set<String> themes = new HashSet<>(); // https://stackoverflow.com/a/4936895/548473

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        for (THEME t : THEME.values()) {
            themes.add(t.name());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        final boolean isUuidEmpty = (uuid == null || uuid.length() == 0);
        Resume r;

        if (isUuidEmpty) {
            r = new Resume(fullName);
        } else {
            r = storage.get(uuid);
            r.setFullName(fullName);
        }

        for (ContactType contactType : ContactType.values()) {
            String value = request.getParameter(contactType.name());
            if (UtilHtml.isEmpty(value)) {
                r.getContacts().remove(contactType);
            } else {
                r.addContact(contactType, value);
            }
        }
        for (SectionType sectionType : SectionType.values()) {
            String requestParameter = request.getParameter(sectionType.name());
            String[] requestParameterValues = request.getParameterValues(sectionType.name());
            if (UtilHtml.isEmpty(requestParameter) && requestParameterValues.length < 2) {
                r.getSections().remove(sectionType);
            } else {
                switch (sectionType) {
                    case OBJECTIVE, PERSONAL -> r.addSection(sectionType, new TextSection(requestParameter));
                    case ACHIEVEMENTS, QUALIFICATIONS ->
                            r.addSection(sectionType, new ListSection(requestParameter.split("\\n")));
                    case EDUCATION, EXPERIENCE -> {
                        List<Organization> organizations = new ArrayList<>();
                        String[] urls = request.getParameterValues(sectionType.name() + "url");
                        for (int i = 0; i < requestParameterValues.length; i++) {
                            String name = requestParameterValues[i];
                            if (!UtilHtml.isEmpty(name)) {
                                List<Organization.Activity> activities = new ArrayList<>();
                                String pfx = sectionType.name() + i;
                                String[] startDates = request.getParameterValues(pfx + "startDate");
                                String[] endDates = request.getParameterValues(pfx + "endDate");
                                String[] titles = request.getParameterValues(pfx + "title");
                                String[] descriptions = request.getParameterValues(pfx + "description");
                                for (int j = 0; j < titles.length; j++) {
                                    if (!UtilHtml.isEmpty(titles[j])) {
                                        activities.add(
                                                new Organization.Activity(
                                                        UtilDates.parse(startDates[j]),
                                                        UtilDates.parse(endDates[j]),
                                                        titles[j],
                                                        descriptions[j]
                                                )
                                        );
                                    }
                                }
                                organizations.add(new Organization(new Link(name, urls[i]), activities));
                            }
                        }
                        r.addSection(sectionType, new OrganizationSection(organizations));
                    }
                }
            }
        }
        if (isUuidEmpty) {
            storage.save(r);
        } else {
            storage.update(r);
        }
        response.sendRedirect("resumes?theme=" + getTheme(request));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        request.setAttribute("theme", getTheme(request));

        if (action == null) {
            request.setAttribute("resumeList", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete" -> {
                storage.delete(uuid);
                response.sendRedirect("resumes");
                return;
            }
            case "add" -> {
                r = Resume.EMPTY;
            }
            case "view" -> {
                r = storage.get(uuid);
            }
            case "edit" -> {
                r = storage.get(uuid);
                for (SectionType sectionType : SectionType.values()) {
                    AbstractSection section = r.getSection(sectionType);
                    switch (sectionType) {
                        case OBJECTIVE, PERSONAL -> {
                            if (section == null) {
                                section = TextSection.EMPTY;
                            }
                        }
                        case ACHIEVEMENTS, QUALIFICATIONS -> {
                            if (section == null) {
                                section = ListSection.EMPTY;
                            }
                        }
                        case EXPERIENCE, EDUCATION -> {
                            // Turn the existing organization section into the one that is nearly
                            // the same as the original, except for:
                            //   1) an empty organization is added at the beginning
                            //      of the section's organizations list;
                            //   2) for each organization on the section's organizations list
                            //      an empty activity is added at the beginning
                            //      of the organization's activities list.
                            List<Organization> orgListWithEmptyFirstOrganization = new ArrayList<>();
                            orgListWithEmptyFirstOrganization.add(Organization.EMPTY);
                            OrganizationSection orgSection = (OrganizationSection) section;
                            if (orgSection != null) {
                                for (Organization org : orgSection.getOrganizations()) {
                                    List<Organization.Activity> activityListWithEmptyFirstActivity = new ArrayList<>();
                                    activityListWithEmptyFirstActivity.add(Organization.Activity.EMPTY);
                                    activityListWithEmptyFirstActivity.addAll(org.getActivities());
                                    orgListWithEmptyFirstOrganization.add(new Organization(org.getHomePage(), activityListWithEmptyFirstActivity));
                                }
                            }
                            section = new OrganizationSection(orgListWithEmptyFirstOrganization);
                        }
                    }
                    r.addSection(sectionType, section);
                }
            }
            default -> throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request
                .getRequestDispatcher((action.equals("view") ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp"))
                .forward(request, response);
    }

    private String getTheme(HttpServletRequest request) {
        String theme = request.getParameter("theme");
        return themes.contains(theme) ? theme : THEME.light.name();
    }
}

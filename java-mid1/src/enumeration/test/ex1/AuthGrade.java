package enumeration.test.ex1;

public enum AuthGrade {
    GUEST(1, "손님", new String[]{"메인 화면"}),
    LOGIN(2, "로그인 회원", new String[]{"메인 화면", "이메일 관리 화면"}),
    ADMIN(3, "관리자", new String[]{"메인 화면", "이메일 관리 화면", "관리자 화면"});

    private final int level;
    private final String description;
    private final String[] menus;

    AuthGrade(int level, String description, String[] menus) {
        this.level = level;
        this.description = description;
        this.menus = menus;
    }

    public int getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    public void showMenus() {
        System.out.println("==메뉴 목록==");
        for (String menu : menus) {
            System.out.println("- " + menu);
        }
    }
}

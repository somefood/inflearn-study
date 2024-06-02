package nested.test.ex1;

public class Library {

    private int offset = 0;
    private final int capacity;
    private Book[] books;

    public Library(int capacity) {
        this.capacity = capacity;
        books = new Book[capacity];
    }

    public void addBook(String title, String author) {
//        if (offset < capacity  && books[offset] == null) {
//            books[offset++] = new Book(title, author);
//        } else {
//            System.out.println("도서관 저장 공간이 부족합니다.");
//        }

        // 검증 로직을 다 처리하고
        if (offset >= capacity) {
            System.out.println("도서관 저장 공간이 부족합니다.");
            return;
        }

        // 정상 로직을 처리
        books[offset++] = new Book(title, author);
    }

    public void showBooks() {
        System.out.println("== 책 목록 출력 ==");
        for (int i = 0; i < offset; i++) {
            System.out.println(books[i]);
        }
    }

    private static class Book {

        private final String title;
        private final String author;

        public Book(String title, String author) {
            this.title = title;
            this.author = author;
        }

        @Override
        public String toString() {
            return "도서 제목: " + this.title + " , 저자: " + this.author;
        }
    }
}

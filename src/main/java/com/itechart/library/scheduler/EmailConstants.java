package com.itechart.library.scheduler;

public class EmailConstants {
    public static final String LIBRARY_EMAIL = "booklibrary2004@gmail.com";
    public static final String LIBRARY_EMAIL_PASSWORD = "rerernjnfv";
    public static final String DUE_DATE_MSG_TEXT = """
                    Hey, <readerName>. It has been some time since you borrowed a book from our library, \
                    and we just want to remind you about the due date.
                    «<bookTitle>» was expected to be returned on <dueDate>. Hope to see you soon.
                                        
                    Contact the library:
                    Tel: 8-800-555-35-35
                    Web site: localhost:8080""";
    public static final String OVERDUE_MSG_TEXT = """
                    Hey, <readerName>. It has been a long time since you borrowed a book from our library and \
                    we sure that you had an extremely good reason not to return it, \
                    but nevertheless we look forward to its return. If you return it soon, we promise not to do anything with you.
                    «<bookTitle>» was expected to be returned on <dueDate>. Hope to see you soon.
                                        
                    Contact the library:
                    Tel: 8-800-555-35-35
                    Web site: localhost:8080""";
    public static final String READER_NAME_PARAM = "readerName";
    public static final String BOOK_TITLE_PARAM = "bookTitle";
    public static final String DUE_DATE_PARAM = "dueDate";
    public static final String EMAIL_PARAM = "email";
    public static final String DUE_DATE_MSG_SUBJECT = "Book library. Reminding to return book";
    public static final String OVERDUE_MSG_SUBJECT = "Book library. You have unreturned book.";
}

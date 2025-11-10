package aboodZaidLibrary;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;

public class clsReminderService {

    // ===== ملفات النظام =====
    private static final String SEP = "#//#";
    private static final String USERS_FILE = "Users.txt";   // username#//#...#//#email#//#!...
    private static final String LOANS_FILE = "Loans.txt";   // isbn#//#username#//#borrow#//#due#//#returned

    // ===== إعدادات SMTP (عدّلهم عند الإرسال الفعلي) =====
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int    SMTP_PORT = 587; // TLS
    private static final String SMTP_USER = "s12218431@stu.najah.edu"; // <-- عدّل
    private static final String SMTP_PASS = "kfomkyghpzhlrufs";      // <-- عدّل (App Password)

    // أثناء التطوير: اطبع بدل ما ترسل
    private static final boolean DRY_RUN = false;

    // ===== واجهة الاستدعاء من قائمة الأدمن =====
    public static void sendOverdueRemindersNow() {
        Map<String, String> userEmails = loadUserEmails();           // key = username (lowercase)
        Map<String, List<LoanRow>> overdueByUser = loadOverduesGroupedByUser(); // key = username (lowercase)

        if (overdueByUser.isEmpty()) {
            System.out.println("✅ No overdue items found. Nothing to send.");
            return;
        }

        for (Map.Entry<String, List<LoanRow>> e : overdueByUser.entrySet()) {
            String usernameKey = e.getKey();                 // lowercase
            String to = userEmails.get(usernameKey);         // ابحث بنفس الـ key
            if (to == null || to.trim().isEmpty()) {
                System.out.println("⚠️ No email for user: " + usernameKey + " (skipping)");
                continue;
            }

            // للعرض الجميل في الإيميل: أول حرف كبير فقط (اختياري)
            String displayName = usernameKey.isEmpty() ? "User"
                    : usernameKey.substring(0, 1).toUpperCase() + usernameKey.substring(1);

            String subject = "Library Overdue Reminder";
            String body = buildEmailBody(displayName, e.getValue());

            try {
                if (DRY_RUN) {
                    System.out.println("----- DRY RUN -----");
                    System.out.println("To: " + to);
                    System.out.println("Subject: " + subject);
                    System.out.println(body);
                    System.out.println("-------------------");
                } else {
                    sendEmail(to, subject, body);
                    System.out.println("📧 Sent reminder to: " + to);
                }
            } catch (Exception ex) {
                System.out.println("❌ Failed to send to " + to + ": " + ex.getMessage());
            }
        }
    }

    // ===== تحميل الإيميلات من Users.txt (بدون حساسية حالة) =====
    private static Map<String, String> loadUserEmails() {
        Map<String, String> map = new HashMap<>();
        Path f = Paths.get(USERS_FILE);
        if (!Files.exists(f)) return map;

        try {
            for (String line : Files.readAllLines(f)) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(Pattern.quote(SEP), -1);
                // التنسيق الفعلي: firstName | lastName | email | phone | userName | encPass | permissions
                if (p.length >= 5) {
                    String email = p[2].trim();
                    String usernameKey = p[4].trim().toLowerCase();
                    if (!usernameKey.isEmpty() && email.contains("@")) {
                        map.put(usernameKey, email);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ Error reading " + USERS_FILE + ": " + e.getMessage());
        }
        return map;
    }


    // ===== تجميع القروض المتأخرة لكل مستخدم (بدون حساسية حالة + يتحمّل حقول زايدة) =====
    private static Map<String, List<LoanRow>> loadOverduesGroupedByUser() {
        Path f = Paths.get(LOANS_FILE);
        Map<String, List<LoanRow>> res = new HashMap<>();
        if (!Files.exists(f)) return res;

        LocalDate today = LocalDate.now();
        try {
            for (String line : Files.readAllLines(f)) {
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(Pattern.quote(SEP), -1);

                // الصيغة الأساس: isbn#//#username#//#borrow#//#due#//#returned
                if (p.length >= 5) {
                    String isbn = safeGet(p, 0).trim();
                    String usernameKey = safeGet(p, 1).trim().toLowerCase();
                    LocalDate due = LocalDate.parse(safeGet(p, 3).trim());

                    // آخر حقل هو returned حتى لو في حقول زيادة
                    boolean returned = Boolean.parseBoolean(safeGet(p, p.length - 1).trim());

                    if (!returned && today.isAfter(due)) {
                        long overdueDays = ChronoUnit.DAYS.between(due, today);
                        res.computeIfAbsent(usernameKey, k -> new ArrayList<>())
                                .add(new LoanRow(isbn, due, overdueDays));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ Error reading " + LOANS_FILE + ": " + e.getMessage());
        } catch (Exception e) {
            System.out.println("⚠️ Parse error in " + LOANS_FILE + ": " + e.getMessage());
        }
        return res;
    }

    private static String safeGet(String[] arr, int idx) {
        return (idx >= 0 && idx < arr.length) ? arr[idx] : "";
    }

    private static String buildEmailBody(String usernameDisplay, List<LoanRow> rows) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello ").append(usernameDisplay).append(",\n\n");
        sb.append("This is a friendly reminder from the Library.\n");
        sb.append("The following borrowed items are overdue:\n\n");
        for (LoanRow r : rows) {
            sb.append("- ISBN: ").append(r.isbn)
                    .append(" | Due: ").append(r.due)
                    .append(" | Overdue: ").append(r.days).append(" day(s)\n");
        }
        sb.append("\nPlease return them as soon as possible. Thank you!\n");
        sb.append("\nRegards,\nLibrary Team");
        return sb.toString();
    }

    // ===== الإرسال عبر SMTP =====
    public static void sendEmail(String to, String subject, String body) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", Integer.toString(SMTP_PORT));

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SMTP_USER));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }

    // ===== نموذج سطر قرض =====
    private static class LoanRow {
        final String isbn;
        final LocalDate due;
        final long days;
        LoanRow(String isbn, LocalDate due, long days) {
            this.isbn = isbn; this.due = due; this.days = days;
        }
    }
    // ===== إرسال رسالة مخصصة لأي بريد (تُستخدم للترحيب وغيره) =====
    public static void sendCustomEmail(String to, String subject, String body) {
        if (DRY_RUN) { // في وضع التطوير: يطبع بدل الإرسال الحقيقي
            System.out.println("----- DRY RUN (Custom Email) -----");
            System.out.println("To: " + to);
            System.out.println("Subject: " + subject);
            System.out.println("Body:\n" + body);
            System.out.println("----------------------------------");
            return;
        }

        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("✅ Email sent successfully to " + to);
        } catch (Exception e) {
            System.out.println("⚠️ Failed to send email: " + e.getMessage());
        }
    }

}

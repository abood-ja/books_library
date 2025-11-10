package aboodZaidLibrary;

import java.util.Scanner;

public class clsLoginScreen extends clsScreen {

    // ====== تسجيل الدخول ======
    private static boolean _Login() {
        int failedLoginCount = 0;

        System.out.print("Enter Username: ");
        String userName = clsInputValidate.readString().trim();

        // التحقق من وجود المستخدم
        while (!clsUser.isUserExist(userName)) {
            failedLoginCount++;
            if (failedLoginCount == 3) {
                System.out.println("\n❌ Locked after 3 failed attempts!\n");
                return false;
            }
            System.out.println("\n❌ Invalid Username! (" + (3 - failedLoginCount) + " tries left)\nEnter another one: ");
            userName = clsInputValidate.readString().trim();
        }

        failedLoginCount = 0;
        System.out.print("Enter Password: ");
        String password = clsInputValidate.readString().trim();

        clsUser user = clsUser.find(userName, password);

        // التحقق من كلمة المرور
        while (user.isEmpty()) {
            failedLoginCount++;
            if (failedLoginCount == 3) {
                System.out.println("\n❌ Locked after 3 failed attempts!\n");
                return false;
            }
            System.out.println("\n⚠️ Wrong Password! (" + (3 - failedLoginCount) + " tries left)\nEnter another one: ");
            password = clsInputValidate.readString().trim();
            user = clsUser.find(userName, password);
        }

        // تم تسجيل الدخول ✅
        clsUserSession.currentUser = user;
        clsUserSession.currentUser.registerLogin();

        System.out.println("\n✅ Welcome back, " + user.getFirstName() + " " + user.getLastName() + "!");

        if (clsUserSession.currentUser.isAdmin())
            clsAdminMainScreen.showMainMenu();
        else
            clsUserMainScreen.showMainMenu();

        return true;
    }

    // ====== تسجيل مستخدم جديد ======
    private static boolean _RegisterNewUser() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        _DrawScreenHeader("\t\tRegister New Account");

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Username: ");
        String username = sc.nextLine().trim();

        if (clsUser.isUserExist(username)) {
            System.out.println("❌ Username already exists!");
            return false;
        }

        System.out.print("Enter First Name: ");
        String firstName = sc.nextLine().trim();

        System.out.print("Enter Last Name: ");
        String lastName = sc.nextLine().trim();

        System.out.print("Enter Email: ");
        String email = sc.nextLine().trim();

        System.out.print("Enter Phone: ");
        String phone = sc.nextLine().trim();

        System.out.print("Enter Password: ");
        String password = sc.nextLine().trim();

        // المستخدم الجديد بدون صلاحيات افتراضيًا
        clsUser newUser = new clsUser(
                clsUser.enMode.AddNewMode,
                firstName,
                lastName,
                email,
                phone,
                username,
                password,
                0 // no admin permissions
        );

        clsUser.enSaveResults result = newUser.save();

        if (result == clsUser.enSaveResults.svSucceeded) {
            System.out.println("\n✅ Account created successfully!");
            // تسجيل دخول تلقائي للمستخدم الجديد
            clsUserSession.currentUser = newUser;
            clsUserSession.currentUser.registerLogin();
            System.out.println("🔐 Logged in as: " + newUser.getFirstName() + " " + newUser.getLastName());

            // إرسال أو طباعة رسالة ترحيب 📧
            _SendWelcomeEmail(newUser);

            // توجيه المستخدم مباشرة إلى قائمته
            clsUserMainScreen.showMainMenu();
            return true;
        } else if (result == clsUser.enSaveResults.svFaildUserExists) {
            System.out.println("\n⚠️ Username already exists!");
        } else {
            System.out.println("\n❌ Failed to create account!");
        }

        return false;
    }

    // ====== إرسال رسالة ترحيب ======
    private static void _SendWelcomeEmail(clsUser user) {
        System.out.println("\n📨 Preparing welcome message for: " + user.getEmail());
        System.out.println("-----------------------------------------------------");
        System.out.println("To: " + user.getEmail());
        System.out.println("Subject: Welcome to Abood & Zaid Library System!");
        System.out.println("Message:");
        System.out.println("-----------------------------------------------------");
        System.out.println("Hello " + user.getFirstName() + ",");
        System.out.println("Welcome to our Library System! 📚");
        System.out.println("We’re glad to have you on board.");
        System.out.println("Your username: " + user.getUserName());
        System.out.println("Enjoy exploring and borrowing books!");
        System.out.println("-----------------------------------------------------");

        try {
            clsReminderService.sendEmail(
                    user.getEmail(),
                    "Welcome to Abood & Zaid Library System!",
                    "Hello " + user.getFirstName() + ",\n\n" +
                            "Welcome to our Library System! 📚\nWe're glad to have you on board.\n\n" +
                            "Your username: " + user.getUserName() + "\nEnjoy exploring and borrowing books!\n\n" +
                            "Best regards,\nLibrary Team"
            );
        } catch (Exception e) {
            System.out.println("⚠️ Email service not active (DRY_RUN mode) — message printed instead.");
        }
    }

    // ====== القائمة الرئيسية ======
    public static boolean showLoginScreen() {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            _DrawScreenHeader("\t\tLogin / Register Screen");
            System.out.println("\tDate: " + clsDate.getSystemDate());
            System.out.println();
            System.out.println("\t[1] Login");
            System.out.println("\t[2] Register New Account");
            System.out.println("\t[3] Exit");
            System.out.print("\n\tChoose [1-3]: ");

            choice = clsInputValidate.readIntNumberBetween(1, 3, "Enter number between 1 and 3:");

            switch (choice) {
                case 1:
                    if (_Login()) return true;
                    break;
                case 2:
                    if (_RegisterNewUser()) return true;
                    break;
                case 3:
                    System.out.println("\n👋 Goodbye!");
                    return false;
                default:
                    System.out.println("❌ Invalid choice!");
                    break;
            }

            System.out.println("\nPress any key to continue...");
            sc.nextLine();
        } while (choice != 3);

        return false;
    }
}

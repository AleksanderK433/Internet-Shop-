import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "AdminPassword123";
        String rawPassword2 = "TestPassword123";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("AdminPassword123:"+encodedPassword);
        String encodedPassword1 = encoder.encode(rawPassword2);
        System.out.println("TestPassword123:"+encodedPassword1);
    }
}
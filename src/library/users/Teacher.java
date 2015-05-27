package library.users;

import java.util.Date;

/**
 * Created by Marcio on 24/05/2015.
 */
public class Teacher extends UniversityMember {
    public Teacher(String firstName, String lastName, String email, long userId) {
        super(firstName, lastName, email, userId);
    }

    public Teacher(String firstName, String lastName, String email, long userId, boolean blocked, Date date) {
        super(firstName, lastName, email, userId, blocked, date);
    }

    @Override
    public String getGroup() {
        return "TEACHER";
    }
}

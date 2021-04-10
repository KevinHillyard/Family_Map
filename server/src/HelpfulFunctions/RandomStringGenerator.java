package HelpfulFunctions;

import Model.Person;

import java.sql.Timestamp;
import java.util.Date;

public class RandomStringGenerator {
    /**
     * Generates a random string with a time_stamp for the ID or token.
     * @return
     */
    public String generateRandomID(Person person) {
        if (person ==  null) {
            // chose a Character random from this String
            String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "0123456789"
                    + "abcdefghijklmnopqrstuvxyz";

            // create StringBuffer size of AlphaNumericString
            StringBuilder sb = new StringBuilder(15);
            String ID = null;

            for (int i = 0; i < 15; i++) {

                // generate a random number between
                // 0 to AlphaNumericString variable length
                int index
                        = (int) (AlphaNumericString.length()
                        * Math.random());

                // add Character one by one in end of sb
                sb.append(AlphaNumericString
                        .charAt(index));
            }
            int i = (int) (new Date().getTime() / 1000);
            ID = sb.toString() + i;

            return ID;
        }
        String ID = person.getFirst_name() + "_" + person.getLast_name();
        return ID;
    }
}

package emmyb.flush.Profiles;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by EmmyB on 12/1/17.
 */

public class ShouldAverageRatings {

    private double expected = 3;

    @Test
    public void calcAverageRating(){
        Profile testProfile = new Profile("123.456", "456.789");

        testProfile.setRating(4);
        double resultingRating = testProfile.calcRating(2, 4);

        assertEquals(resultingRating, expected);
    }


}

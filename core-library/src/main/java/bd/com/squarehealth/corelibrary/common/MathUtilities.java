package bd.com.squarehealth.corelibrary.common;

public final class MathUtilities {

    /**
     * Calculates great-circle distance. The shortest distance between
     * any two points on the sphere surface is the Great Circle distance.
     * Historically, the Great circle is also called as an Orthodrome or Romanian Circle.
     * Note: great-circle distance is being calculated using the law of cosines.
     * @see <a href="https://introcs.cs.princeton.edu/java/12types/GreatCircle.java.html">Original Implementation</a>
     * @param latitudeA
     * @param longitudeA
     * @param latitudeB
     * @param longitudeB
     * @return Returns the great-circle distance (in kilometers) between the two locations.
     */
    public static double calculateGreatCircleDistance(double latitudeA, double longitudeA, double latitudeB, double longitudeB) {
        double latitudeAInRadians = Math.toRadians(latitudeA);
        double longitudeAInRadians = Math.toRadians(longitudeA);
        double latitudeBInRadians = Math.toRadians(latitudeB);
        double longitudeBInRadians = Math.toRadians(longitudeB);

        // calculating angle in radians...
        double angleInRadians = Math.acos(Math.sin(latitudeAInRadians) * Math.sin(latitudeBInRadians)
                + Math.cos(latitudeAInRadians) * Math.cos(latitudeBInRadians) * Math.cos(longitudeAInRadians - longitudeBInRadians));
        // convert back to degrees...
        angleInRadians = Math.toDegrees(angleInRadians);

        // each degree on a great circle of Earth is 60 nautical miles...
        double distanceInNauticalMiles = 60 * angleInRadians;
        // converting to kilometers...
        double distanceInKilometers = distanceInNauticalMiles * 1.852;

        return distanceInKilometers;
    }
}

package lib.frc254.geometry;

import lib.frc254.util.CSVWritable;
import lib.frc254.util.Interpolable;

public interface State<S> extends Interpolable<S>, CSVWritable {
    double distance(final S other);

    S add(S other);

    boolean equals(final Object other);

    String toString();

    String toCSV();
}

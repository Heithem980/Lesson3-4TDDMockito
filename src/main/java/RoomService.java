import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class RoomService {
    private List<String> availableRooms = new ArrayList<>(Arrays.asList("Hiya", "hey", "Dessa strängar kommer inte användas."));

    public List<String> getAvailableRooms() {
        //Hämtar ut ur databasen.
        //Gör en check ifall de är tillgängliga.
        return availableRooms;
    }

    public String getFirstAvailableRoom() {
        if (countAvailableRooms() == 0) {
            throw new IllegalStateException();
        }
        String room = availableRooms.get(0);
        availableRooms.remove(room);
        return room;
    }

    public String getFirstAvailableRoomAlwaysReturns5(int number, int number2) {
        if (countAvailableRooms() == 0) {
            throw new IllegalStateException();
        }
        String room = availableRooms.get(0);
        availableRooms.remove(room);
        return room;
    }

    public String getFirstAvailableRoom(int availableRoomCount) {
        if (availableRoomCount == 0) {
            throw new IllegalStateException();
        }
        String room = availableRooms.get(0);
        availableRooms.remove(room);
        return room;
    }

    public final int countAvailableRooms() {
        return availableRooms.size();
    }

    public BookingRequest getBookingOfRoom(int index) {
        return new BookingRequest("", 0, 0, availableRooms.get(index), false);
    }
}

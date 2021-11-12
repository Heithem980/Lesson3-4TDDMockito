public class BookingRequest {
    private String userID;
    private int guestCount;
    private int daysStaying;
    private String roomId;
    private boolean prepaid;

    public BookingRequest(String userID, int guestCount, int daysStaying, String roomId, boolean prepaid) {
        this.userID = userID;
        this.guestCount = guestCount;
        this.daysStaying = daysStaying;
        this.roomId = roomId;
        this.prepaid = prepaid;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(int guestCount) {
        this.guestCount = guestCount;
    }

    public int getDaysStaying() {
        return daysStaying;
    }

    public void setDaysStaying(int daysStaying) {
        this.daysStaying = daysStaying;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public boolean isPrepaid() {
        return prepaid;
    }

    public void setPrepaid(boolean prepaid) {
        this.prepaid = prepaid;
    }
}

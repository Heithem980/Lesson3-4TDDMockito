public class BookingService {
    private PaymentService paymentService;
    private RoomService roomService;
    private MailSender mailSender;

    public BookingService(PaymentService paymentService, RoomService roomService, MailSender mailSender) {
        this.paymentService = paymentService;
        this.roomService = roomService;
        this.mailSender = mailSender;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public RoomService getRoomService() {
        return roomService;
    }

    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    public MailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public static int PRICE_PER_DAY_PER_GUEST = 50;

    public double CalculatePriceInEuro(double guestCount, double daysStaying) {
        double result = guestCount * daysStaying * PRICE_PER_DAY_PER_GUEST;
        return PaymentService.getSekToEuro((double) result);
    }

    public int CalculatePrice(BookingRequest bookingRequest) {
        int result = bookingRequest.getGuestCount() * bookingRequest.getDaysStaying() * PRICE_PER_DAY_PER_GUEST;
        return result;
    }

    public int CalculatePriceSpecial(BookingRequest bookingRequest) {
        int result = bookingRequest.getGuestCount() * bookingRequest.getDaysStaying() * PRICE_PER_DAY_PER_GUEST;

        result -= roomService.countAvailableRooms();

        return result;
    }

    public String bookRoom(BookingRequest bookingRequest) {
        //Add stuff
        if (bookingRequest.isPrepaid()) {
            paymentService.pay(5, CalculatePrice(bookingRequest));
        }
        //More stuff
        return bookingRequest.getRoomId();
    }


}

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Nested
class BookingServiceTest {
    private BookingService bookingService;
    private PaymentService paymentService;
    private RoomService roomService;
    private MailSender mailSender;

    @BeforeEach
    void setup() {
        paymentService = spy(PaymentService.class);
        roomService = mock(RoomService.class);
        mailSender = mock(MailSender.class);

        bookingService = new BookingService(paymentService, roomService, mailSender);
    }

    @Test
    void should_CalculatePrice_When_GotBookingRequestWith2GuestsStaying3Days() {
        BookingRequest bookingRequest = new BookingRequest("1", 2, 3, "1", false);

        int expected = 2 * 3 * 50;
        int actual = bookingService.CalculatePrice(bookingRequest);

        assertEquals(expected, actual);
    }

    @Test
    void should_CalculatePrice_When_GotBookingRequestWith4GuestsStaying5Days() {
        BookingRequest bookingRequest = new BookingRequest("1", 4, 5, "1", false);

        int expected = 4 * 5 * 50;
        int actual = bookingService.CalculatePrice(bookingRequest);

        assertEquals(expected, actual);
    }

    @Test
    void should_GetPrice_when_15AvailableRooms2People2Days() {
        BookingRequest bookingRequest = new BookingRequest("1", 2, 2, "1", false);

        when(roomService.countAvailableRooms()).thenReturn(15);

        int expected = 2 * 2 * 50 - 15;

        int actual = bookingService.CalculatePriceSpecial(bookingRequest);

        assertEquals(expected, actual);
    }

    @Test
    void should_Count1Room_when_1AvailableRoom() {
        when(roomService.getAvailableRooms()).thenReturn(
                new ArrayList<>(Arrays.asList("Hiya."))
        );

        int expected = 1;

        int actual = bookingService.getRoomService().getAvailableRooms().size();

        /*System.out.println("List: " + roomService.getAvailableRooms());
        System.out.println("String: " + roomService.getFirstAvailableRoom());
        System.out.println("Int: " + roomService.countAvailableRooms());
        System.out.println("Object: " + roomService.getBookingOfRoom(0));*/

        assertEquals(expected, actual);
    }

    @Test
    void should_Count1Room_when_5AvailableRooms() {
        List<String> rooms = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5"));

        when(roomService.getAvailableRooms()).thenReturn(rooms);

        int expected = 5;

        int actual = bookingService.getRoomService().getAvailableRooms().size();

        assertEquals(expected, actual);
    }

    @Test
    void should_GetDifferentValuesEachTime_when_CallingGetFirstRoomId() {
        when(roomService.getFirstAvailableRoom())
                .thenReturn("Room 1")
                .thenReturn("Room 2")
                .thenReturn("Room 3")
                .thenReturn(null);

        assertEquals("Room 1", roomService.getFirstAvailableRoom());
        assertEquals("Room 2", roomService.getFirstAvailableRoom());
        assertEquals("Room 3", roomService.getFirstAvailableRoom());
        assertNull(roomService.getFirstAvailableRoom());
        assertNull(roomService.getFirstAvailableRoom());
        assertNull(roomService.getFirstAvailableRoom());
        assertNull(roomService.getFirstAvailableRoom());
        assertNull(roomService.getFirstAvailableRoom());
        assertNull(roomService.getFirstAvailableRoom());
    }

    @Test
    void should_ThrowException_when_ListEmpty() {
        when(roomService.getFirstAvailableRoom())
                .thenThrow(IllegalStateException.class);

        Executable executable = () -> roomService.getFirstAvailableRoom();

        assertThrows(IllegalStateException.class, executable);
    }

    @Test
    void should_ThrowException_when_ListEmptyButNotWith1() {
        when(roomService.getFirstAvailableRoom(0))
                .thenThrow(IllegalStateException.class);
        when(roomService.getFirstAvailableRoom(1))
                .thenReturn("1");

        Executable executable = () -> roomService.getFirstAvailableRoom(0);

        assertThrows(IllegalStateException.class, executable);
        assertEquals("1", roomService.getFirstAvailableRoom(1));
    }

    @Test
    void should_Return5_when_Always() {
        when(roomService.getFirstAvailableRoomAlwaysReturns5(anyInt(), eq(3)))
                .thenReturn("5");

        assertEquals("5", roomService.getFirstAvailableRoomAlwaysReturns5(1, 3));
        assertEquals("5", roomService.getFirstAvailableRoomAlwaysReturns5(22, 3));
        assertEquals("5", roomService.getFirstAvailableRoomAlwaysReturns5(103, 3));
        assertEquals("5", roomService.getFirstAvailableRoomAlwaysReturns5(72, 3));
        assertEquals("5", roomService.getFirstAvailableRoomAlwaysReturns5(234, 3));
    }

    @Test
    void should_Pay_when_PrePaidPaysUpFront() {
        BookingRequest bookingRequest = new BookingRequest(
                "1", 2, 2, "1", true);
        bookingService.bookRoom(bookingRequest);

        verify(paymentService, times(1)).pay(/*5, bookingService.CalculatePrice(bookingRequest)*/ anyInt(), anyInt());
        verifyNoMoreInteractions(paymentService);
    }
    @Test
    void should_NotPay_when_NotPrePaid() {
        BookingRequest bookingRequest = new BookingRequest(
                "1", 2, 2, "1", false);
        bookingService.bookRoom(bookingRequest);

        verify(paymentService, never()).pay(anyInt(), anyInt());
    }

    @Test
    void should_BePaid_when_Paid() {
        paymentService.pay(5, 5);

        assertTrue(paymentService.paid);
    }

    @Test
    void should_ThrowException_when_PayingLow() {
        //when(paymentService.pay(anyInt())).thenThrow(IllegalStateException.class);

        doThrow(new IllegalStateException()).when(paymentService).pay(anyInt());
        //doNothing().when(paymentService).pay(anyInt());


        Executable executable = () -> paymentService.pay(54);

        assertThrows(IllegalStateException.class, executable);
        //assertDoesNotThrow(executable);
    }
}
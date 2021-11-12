import net.bytebuddy.description.field.FieldDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;

@Nested
@ExtendWith(MockitoExtension.class)
class BookingServiceTestNextLesson {
    @InjectMocks
    private BookingService bookingService;

    @Mock
    private PaymentService paymentService;
    @Mock
    private RoomService roomService;
    @Mock
    private MailSender mailSender;

    @Captor
    private ArgumentCaptor<Integer> intCaptor;
    @Captor
    private ArgumentCaptor<BookingRequest> bookingRequestCaptor;

    /*@BeforeEach
    void setup() {
        paymentService = mock(PaymentService.class);
        roomService = mock(RoomService.class);
        mailSender = mock(MailSender.class);
        intCaptor = ArgumentCaptor.forClass(Integer.class);

        bookingService = new BookingService(paymentService, roomService, mailSender);
    }*/

    @Nested
    class capturing {
        @Test
        void should_CalculatePrice_When_GotBookingRequestWith2GuestsStaying3Days() {
            //given
            BookingRequest bookingRequest = new BookingRequest("1", 2, 3, "1", false);
            int expected = 2 * 3 * 50;

            //when
            int actual = bookingService.CalculatePrice(bookingRequest);

            //then
            assertEquals(expected, actual);
        }

        @Test
        void intCaptor_SingleTarget() {
            BookingRequest bookingRequest = new BookingRequest(
                    "1", 2, 2, "1", true);
            bookingService.bookRoom(bookingRequest);

            verify(paymentService, times(1)).pay(anyInt(), intCaptor.capture());
            verifyNoMoreInteractions(paymentService);
            int calculatedValue = intCaptor.getValue();
            assertEquals(200, calculatedValue);
        }

        @Test
        void intCaptor_MultipleTargets() {
            BookingRequest bookingRequest = new BookingRequest(
                    "1", 2, 2, "1", true);
            BookingRequest bookingRequest2 = new BookingRequest(
                    "1", 5, 45, "1", true);
            bookingService.bookRoom(bookingRequest);
            bookingService.bookRoom(bookingRequest2);

            verify(paymentService, times(2)).pay(anyInt(), intCaptor.capture());
            verifyNoMoreInteractions(paymentService);
            List<Integer> expected = Arrays.asList(200, 11250);
            List<Integer> calculatedValue = intCaptor.getAllValues();
            assertEquals(expected, calculatedValue);
        }
    }

    @Nested
    class giventhen {

        @Test
        void should_GetPrice_when_15AvailableRooms2People2Days() {
            //given
            BookingRequest bookingRequest = new BookingRequest("1", 2, 2, "1", false);
            given(roomService.countAvailableRooms()).willReturn(15);
            when(roomService.countAvailableRooms()).thenReturn(15);

            //when
            int expected = 2 * 2 * 50 - 15;
            int actual = bookingService.CalculatePriceSpecial(bookingRequest);

            //then
            assertEquals(expected, actual);
        }

        @Test
        void should_Pay_when_PrePaidPaysUpFront() {
            //given
            BookingRequest bookingRequest = new BookingRequest(
                    "1", 2, 2, "1", true);

            //when
            bookingService.bookRoom(bookingRequest);

            //then
            //verify(paymentService, times(1)).pay(anyInt(), anyInt());
            then(paymentService).should(times(1)).pay(anyInt(), anyInt());
            verifyNoMoreInteractions(paymentService);
        }
    }

    @Nested
    class staticTotally {
        @Test
        void should_Pay_when_PrePaidPaysUpFront_StrictStubbing() {
            //given
            BookingRequest bookingRequest = new BookingRequest(
                    "1", 2, 2, "1", false);
            lenient().when(paymentService.pay(anyInt(), anyInt())).thenReturn(true);

            //when
            bookingService.bookRoom(bookingRequest);
        }

    /*@Test
    void should_CalculatePriceInEuros() {
        //given
        MockedStatic<PaymentService> mockedStaticConverter = mockStatic(PaymentService.class);
        BookingRequest bookingRequest = new BookingRequest(
                "1", 2, 2, "1", false);

        //when
        mockedStaticConverter.when(() -> PaymentService.getSekToEuro(anyDouble())).thenReturn(100.0);
        double whatWeGot = bookingService.CalculatePriceInEuro(2, 2);

        //then
        assertEquals(100.0, whatWeGot);
    }*/

        @Test
        void should_ActuallyCalculatePriceInEuros() {
            //given
            MockedStatic<PaymentService> mockedStaticConverter = mockStatic(PaymentService.class);
            BookingRequest bookingRequest = new BookingRequest(
                    "1", 2, 2, "1", false);

            //when
            mockedStaticConverter.when(() -> PaymentService.getSekToEuro(anyDouble()))
                    .thenAnswer(invocation -> (double) invocation.getArgument(0) * 0.1);

            double whatWeGot = bookingService.CalculatePriceInEuro(2, 2);

            //then
            assertEquals(20.0, whatWeGot);
        }

        @Test
        void should_GetPrice_when_15AvailableRooms2People2Days_Final() {
            BookingRequest bookingRequest = new BookingRequest("1", 2, 2, "1", false);

            when(roomService.countAvailableRooms()).thenReturn(15);

            int expected = 2 * 2 * 50 - 15;

            int actual = bookingService.CalculatePriceSpecial(bookingRequest);

            assertEquals(expected, actual);
        }
    }

    @ParameterizedTest(name = "Testing {0}")
    @DisplayName("Testing multiple data entries")
    @ValueSource(ints = {2, 65, 34, 22, 8, -1})
    void should_GetPrice_when_15AvailableRoomsMultiplePeople2Days_Multiple(int guestCount) {
        BookingRequest bookingRequest = new BookingRequest("1", guestCount, 2, "1", false);

        when(roomService.countAvailableRooms()).thenReturn(15);

        int expected = guestCount * 2 * 50 - 15;

        int actual = bookingService.CalculatePriceSpecial(bookingRequest);

        assertEquals(expected, actual);
    }
}
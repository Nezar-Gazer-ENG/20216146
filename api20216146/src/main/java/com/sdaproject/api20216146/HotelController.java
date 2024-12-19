@RestController
@RequestMapping("/api/hotels")
public class HotelController {
    @GetMapping
    public String getHotels() {
        return "List of hotels";
    }
}

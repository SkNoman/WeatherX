import Foundation
import Shared

@MainActor
final class HomeViewModel: ObservableObject {

    @Published private(set) var isLoading = false
    @Published private(set) var location: LocationData?
    @Published private(set) var weather: Weather?
    @Published private(set) var errorMessage: String?

    private let facade: WeatherFacade

    init(facade: WeatherFacade = WeatherProvider.shared.weatherFacade()) {
        self.facade = facade
    }
    
    var locationText: String {
        guard let location else { return "Location: not available yet" }
        return "Your location\nLatitude: \(location.latitude)\nLongitude: \(location.longitude)"
    }

    var weatherText: String {
        guard let current = weather?.current else { return "No weather data yet" }
        return "Temperature: \(current.temperature2m)\n"
            + "Humidity: \(current.relativeHumidity2m)%\n"
            + "Wind: \(current.windSpeed10m)\n"
            + "Timezone: \(weather?.timezone ?? "")"
    }
    
    var weatherInfo: Weather? {
        weather
    }

    func loadWeather() {
        guard !isLoading else { return }

        isLoading = true
        errorMessage = nil

        Task {
            do {
                let snapshot = try await facade.loadWeather()
                if let loaded = snapshot.weather {
                    location = snapshot.location
                    weather = loaded
                } else {
                    errorMessage = snapshot.error
                }
            } catch {
                errorMessage = error.localizedDescription
            }
            isLoading = false
        }
    }
}

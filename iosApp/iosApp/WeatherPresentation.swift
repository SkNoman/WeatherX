//
//  WeatherPresentation.swift
//  iosApp
//
//  Created by Abdullah Al Noman on 29/9/26.
//

import Foundation
import SwiftUI
import Shared

enum WeatherCondition {
    case clear
    case cloudy
    case rain
    case windy

    init(current: CurrentWeather) {
        if current.precipitation > 0.1 || current.rain > 0.1 {
            self = .rain
        } else if current.windSpeed10m >= 38 {
            self = .windy
        } else if current.relativeHumidity2m >= 85 {
            self = .cloudy
        } else {
            self = .clear
        }
    }

    var title: String {
        switch self {
        case .clear: return "Clear Sky"
        case .cloudy: return "Overcast"
        case .rain: return "Rain"
        case .windy: return "Windy"
        }
    }

    var symbol: String {
        switch self {
        case .clear: return "sun.max.fill"
        case .cloudy: return "cloud.fill"
        case .rain: return "cloud.rain.fill"
        case .windy: return "wind"
        }
    }

    var tint: Color {
        switch self {
        case .clear: return .yellow
        case .cloudy: return .blue
        case .rain: return .indigo
        case .windy: return .teal
        }
    }
}

enum WeatherPresentation {

    // Open-Meteo defaults for this endpoint: °C, km/h, mm, metres.
    static func temperature(_ value: Double) -> String {
        "\(Int(value.rounded()))°"
    }

    static func temperatureWithUnit(_ value: Double) -> String {
        "\(Int(value.rounded()))°C"
    }

    static func measurement(_ value: Double, unit: String) -> String {
        "\(value.formatted(.number.precision(.fractionLength(0...1)))) \(unit)"
    }

    static func percentage(_ value: Int32) -> String {
        "\(value)%"
    }

    static func coordinate(_ value: Double) -> String {
        value.formatted(.number.precision(.fractionLength(4)))
    }

    private static let compassPoints = [
        "N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
        "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"
    ]

    static func compassPoint(forDegrees degrees: Int32) -> String {
        let step = Int((Double(degrees) / 360.0 * Double(compassPoints.count)).rounded())
        let index = (step % compassPoints.count + compassPoints.count) % compassPoints.count
        return compassPoints[index]
    }

    static func utcOffset(_ seconds: Int32) -> String {
        let sign = seconds < 0 ? "-" : "+"
        let total = abs(seconds)
        return String(format: "GMT%@%02d:%02d", sign, total / 3600, (total % 3600) / 60)
    }

    static func generationTime(_ milliseconds: Double) -> String {
        "\(Int(milliseconds.rounded())) ms"
    }

    private static let readableFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "EEE d MMM · HH:mm"
        return formatter
    }()

    static func observationTime(_ iso: String) -> String {
        let trimmed = iso.count > 16 ? String(iso.prefix(16)) : iso
        let parser = DateFormatter()
        parser.locale = Locale(identifier: "en_US_POSIX")
        parser.dateFormat = "yyyy-MM-dd'T'HH:mm"
        guard let date = parser.date(from: trimmed) else { return iso }
        return readableFormatter.string(from: date)
    }

    static func skyGradient(_ condition: WeatherCondition, isDay: Bool) -> LinearGradient {
        let colors: [Color]
        switch (condition, isDay) {
        case (.rain, _): colors = [.indigo.opacity(0.55), .blue.opacity(0.35)]
        case (.cloudy, _): colors = [.gray.opacity(0.5), .blue.opacity(0.3)]
        case (.windy, _): colors = [.teal.opacity(0.5), .cyan.opacity(0.3)]
        case (.clear, true): colors = [.orange.opacity(0.45), .blue.opacity(0.4)]
        case (.clear, false): colors = [.indigo.opacity(0.6), .purple.opacity(0.35)]
        }
        return LinearGradient(colors: colors, startPoint: .topLeading, endPoint: .bottomTrailing)
    }
}

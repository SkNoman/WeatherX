//
//  HomeTab.swift
//  iosApp
//
//  Created by Abdullah Al Noman on 29/9/26.
//

import Foundation


import SwiftUI
import Shared

struct HomeTab: View {

    @StateObject private var viewModel = HomeViewModel()

    private let columns = [
        GridItem(.flexible(), spacing: 12),
        GridItem(.flexible(), spacing: 12)
    ]

    var body: some View {

        NavigationStack {
            ScrollView {
                VStack(spacing: 20) {

                    hero

                    if let weather = viewModel.weatherInfo {
                        currentConditions(weather.current)
                        details(weather)
                    } else if viewModel.isLoading {
                        placeholder
                    } else {
                        emptyState
                    }

                    if let errorMessage = viewModel.errorMessage {
                        errorBanner(errorMessage)
                    }
                }
                .padding(.horizontal, 16)
                .padding(.bottom, 32)
            }
            .background(Color(.systemGroupedBackground))
            .navigationTitle("WeatherX")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    Button {
                        viewModel.loadWeather()
                    } label: {
                        Image(systemName: "arrow.clockwise")
                    }
                    .disabled(viewModel.isLoading)
                    .accessibilityLabel("Refresh weather")
                }

                ToolbarItem(placement: .topBarTrailing) {
                    Button {

                    } label: {
                        Image(systemName: "magnifyingglass")
                    }
                    .accessibilityLabel("Search")
                }

                ToolbarItem(placement: .topBarTrailing) {
                    Button {

                    } label: {
                        Image(systemName: "person.circle")
                    }
                    .accessibilityLabel("Profile")
                }
            }
            .refreshable {
                viewModel.loadWeather()
            }
            .task {
                if viewModel.weatherInfo == nil {
                    viewModel.loadWeather()
                }
            }
        }
    }

    private var hero: some View {
        Group {
            if let weather = viewModel.weatherInfo {
                let current = weather.current
                let condition = WeatherCondition(current: current)

                VStack(spacing: 6) {
                    HStack(spacing: 8) {
                        Image(systemName: current.isDay ? "sun.max.fill" : "moon.stars.fill")
                            .font(.subheadline)
                            .foregroundStyle(.secondary)
                            .symbolRenderingMode(.hierarchical)

                        Text(WeatherPresentation.observationTime(current.time))
                            .font(.subheadline)
                            .foregroundStyle(.secondary)
                    }

                    Text(WeatherPresentation.temperature(current.temperature2m))
                        .font(.system(size: 96, weight: .thin, design: .rounded))
                        .monospacedDigit()
                        .minimumScaleFactor(0.5)
                        .lineLimit(1)

                    HStack(spacing: 6) {
                        Image(systemName: condition.symbol)
                            .symbolRenderingMode(.hierarchical)
                            .foregroundStyle(condition.tint)

                        Text(condition.title)
                            .font(.title3.weight(.medium))
                    }

                    Text("\(weather.timezoneAbbreviation) · \(WeatherPresentation.utcOffset(weather.utcOffsetSeconds))")
                        .font(.caption)
                        .foregroundStyle(.secondary)
                }
                .frame(maxWidth: .infinity)
                .padding(.vertical, 28)
                .padding(.horizontal, 16)
                .background {
                    WeatherPresentation.skyGradient(condition, isDay: current.isDay)
                        .clipShape(RoundedRectangle(cornerRadius: 24, style: .continuous))
                }
                .overlay(alignment: .bottomTrailing) {
                    Text("\(WeatherPresentation.coordinate(weather.latitude)), \(WeatherPresentation.coordinate(weather.longitude))")
                        .font(.caption2.monospacedDigit())
                        .foregroundStyle(.secondary)
                        .padding(12)
                }
                .accessibilityElement(children: .combine)
            } else {
                RoundedRectangle(cornerRadius: 24, style: .continuous)
                    .fill(Color(.secondarySystemGroupedBackground))
                    .frame(height: 200)
                    .overlay {
                        ProgressView()
                    }
            }
        }
    }

    private func currentConditions(_ current: CurrentWeather) -> some View {
        VStack(alignment: .leading, spacing: 12) {
            sectionTitle("Current Conditions", symbol: "square.grid.2x2")

            LazyVGrid(columns: columns, spacing: 12) {
                WeatherMetricCard(
                    title: "Humidity",
                    value: WeatherPresentation.percentage(current.relativeHumidity2m),
                    symbol: "humidity.fill",
                    tint: .blue
                )

                WeatherMetricCard(
                    title: "Wind",
                    value: current.windSpeed10m.formatted(.number.precision(.fractionLength(0...1))),
                    unit: "km/h",
                    symbol: "wind",
                    tint: .teal
                )

                WeatherMetricCard(
                    title: "Gusts",
                    value: current.windGusts10m.formatted(.number.precision(.fractionLength(0...1))),
                    unit: "km/h",
                    symbol: "speedometer",
                    tint: .mint
                )

                WeatherMetricCard(
                    title: "Direction",
                    value: WeatherPresentation.compassPoint(forDegrees: current.windDirection10m),
                    unit: "\(current.windDirection10m)°",
                    symbol: "location.north.line.fill",
                    tint: .indigo
                )

                WeatherMetricCard(
                    title: "Precipitation",
                    value: current.precipitation.formatted(.number.precision(.fractionLength(0...1))),
                    unit: "mm",
                    symbol: "drop.fill",
                    tint: .blue
                )

                WeatherMetricCard(
                    title: "Rain",
                    value: current.rain.formatted(.number.precision(.fractionLength(0...1))),
                    unit: "mm",
                    symbol: "cloud.rain.fill",
                    tint: .indigo
                )
            }
        }
    }

    private func details(_ weather: Weather) -> some View {
        VStack(alignment: .leading, spacing: 12) {
            sectionTitle("Location & Metadata", symbol: "info.circle")

            VStack(spacing: 14) {
                WeatherDetailRow(
                    title: "Timezone",
                    value: weather.timezone,
                    symbol: "clock"
                )

                WeatherDetailRow(
                    title: "UTC offset",
                    value: WeatherPresentation.utcOffset(weather.utcOffsetSeconds),
                    symbol: "globe.americas.fill"
                )

                WeatherDetailRow(
                    title: "Elevation",
                    value: WeatherPresentation.measurement(weather.elevation, unit: "m"),
                    symbol: "mountain.2.fill"
                )

                WeatherDetailRow(
                    title: "Latitude",
                    value: WeatherPresentation.coordinate(weather.latitude),
                    symbol: "arrow.up.and.down"
                )

                WeatherDetailRow(
                    title: "Longitude",
                    value: WeatherPresentation.coordinate(weather.longitude),
                    symbol: "arrow.left.and.right"
                )

                WeatherDetailRow(
                    title: "Day / night",
                    value: weather.current.isDay ? "Daytime" : "Night",
                    symbol: weather.current.isDay ? "sun.max.fill" : "moon.fill"
                )

                WeatherDetailRow(
                    title: "Sampling interval",
                    value: "\(weather.current.interval) min",
                    symbol: "timer"
                )

                WeatherDetailRow(
                    title: "Generated in",
                    value: WeatherPresentation.generationTime(weather.generationTimeMs),
                    symbol: "bolt.horizontal"
                )
            }
            .padding(16)
            .background(.regularMaterial, in: RoundedRectangle(cornerRadius: 16, style: .continuous))
        }
    }

    private var placeholder: some View {
        VStack(spacing: 12) {
            ProgressView()
            Text("Loading weather…")
                .font(.footnote)
                .foregroundStyle(.secondary)
        }
        .frame(maxWidth: .infinity)
        .padding(.vertical, 40)
    }

    private var emptyState: some View {
        ContentUnavailableView {
            Label("No Weather Yet", systemImage: "cloud.sun")
        } description: {
            Text("Pull to refresh or tap the reload button to fetch the latest conditions.")
        }
        .padding(.vertical, 40)
    }

    private func errorBanner(_ message: String) -> some View {
        HStack(spacing: 10) {
            Image(systemName: "exclamationmark.triangle.fill")
                .foregroundStyle(.orange)

            Text(message)
                .font(.footnote)
                .foregroundStyle(.primary)

            Spacer(minLength: 0)

            Button("Retry") {
                viewModel.loadWeather()
            }
            .font(.footnote.weight(.semibold))
        }
        .padding(14)
        .background(
            RoundedRectangle(cornerRadius: 14, style: .continuous)
                .fill(Color.orange.opacity(0.14))
        )
    }

    private func sectionTitle(_ text: String, symbol: String) -> some View {
        Label(text, systemImage: symbol)
            .font(.headline)
            .frame(maxWidth: .infinity, alignment: .leading)
    }
}

#Preview {
    HomeTab()
}

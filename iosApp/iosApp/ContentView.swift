//
//  ContentView.swift
//  iosApp
//
//  Created by Abdullah Al Noman on 29/9/26.
//

import Foundation


import SwiftUI

struct ContentView: View {
    var body: some View {
        TabView {

            HomeTab()
                .tabItem {
                    Image("cloudyTab")
                    Text("Weather")
                }

            SearchView()
                .tabItem {
                    Image("radarTab")
                    Text("Radar")
                }

            SettingsView()
                .tabItem {
                    Image("cityscapeTab")
                    Text("Cities")
                }
            SettingsView()
                .tabItem {
                    Image(systemName: "gearshape")
                    Text("Settings")
                }
        }
    }
}

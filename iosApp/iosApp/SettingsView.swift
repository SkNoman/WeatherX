//
//  SettingsView.swift
//  iosApp
//
//  Created by Abdullah Al Noman on 29/9/26.
//

import Foundation


import SwiftUI

struct SettingsView: View {

    var body: some View {
        NavigationStack {
            Form {
                Section {
                    Text("General")
                    Text("About")
                }
            }
            .navigationTitle("Settings")
        }
    }
}

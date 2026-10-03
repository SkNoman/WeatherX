//
//  SearchView.swift
//  iosApp
//
//  Created by Abdullah Al Noman on 29/9/26.
//

import Foundation


import SwiftUI

struct SearchView: View {

    @State private var searchText = ""
    
    let users = [
        ("Abdullah Al Noman", "IOS Developer"),
        ("John Doe", "Android Developer"),
        ("Jane Smith", "UI/UX Designer"),
        ("Alex Johnson", "Backend Developer"),
        ("Sarah Wilson", "Product Designer"),
        ("Sarah Wilson", "Product Designer"),
        ("Sarah Wilson", "Product Designer"),
        ("Sarah Wilson", "Product Designer"),
        ("Sarah Wilson", "Product Designer"),
        ("Sarah Wilson", "Product Designer"),
        ("Sarah Wilson", "Product Designer"),
        ("Sarah Wilson", "Product Designer"),
        ("Sarah Wilson", "Product Designer"),
        ("Sarah Wilson", "Product Designer"),
        ("Sarah Wilson", "Product Designer"),
        ("Sarah Wilson", "Product Designer"),
        ("Sarah Wilson", "Product Designer")
    ]
    
    var filteredUsers: [(String, String)]{
        if searchText.isEmpty{
            return users
        }
        
        return users.filter{user in
            user.0.localizedCaseInsensitiveContains(searchText) ||
            user.1.localizedCaseInsensitiveContains(searchText)
        }
    }

    var body: some View {
        NavigationStack {
            List(filteredUsers, id: \.0) { user in

                HStack(spacing: 12) {

                    Image(systemName: "person.circle.fill")
                        .font(.system(size: 40))
                        .foregroundColor(.blue)

                    VStack(alignment: .leading, spacing: 4) {

                        Text(user.0)
                            .font(.headline)

                        Text(user.1)
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                    }
                }
                .padding(.vertical, 4)
            }.searchable(
                text: $searchText,
                prompt: "Search Users"
            )
            .navigationTitle("Users")
        }
    }
}

import SwiftUI
import Shared
@main
struct iOSApp: App {

    init(){
        KoinDiSetupKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

import SwiftUI
import GoogleSignIn
import Firebase
import FirebaseCore
import FirebaseAuth
import FirebaseMessaging
import ComposeApp
import local

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    var body: some Scene {
        WindowGroup {
            ContentView()
                .ignoresSafeArea()
                .onOpenURL(perform: { url in
                    print("Received URI: \(url)")
                    if GIDSignIn.sharedInstance.handle(url) {return}
                    
                    guard let component = URLComponents(url: url, resolvingAgainstBaseURL: true),
                          let queryItems = component.queryItems else {return}
                    let success = queryItems.first(where: {$0.name == "success"})?.value == "true"
                    let cancel = queryItems.first(where: {$0.name == "cancel"})?.value == "true"
                    let token = queryItems.first(where: {$0.name == "token"})?.value
                    
                    print(
                        "isCompleted: \(success) | cancel: \(cancel) | token: \(token ?? "nil")"
                    )
                    let errorMessage =
                        cancel ? "Payment was cancelled" :
                        (!success ? "Payment failed" : nil)
                    
                    PaymentDataSourceHelper().paymentDataSource.setPaymentData(
                        isSuccess: success ? KotlinBoolean(value: true) : nil,
                        errorMessage:errorMessage,
                                            token: token
                                        )
                            
                       })
        }
    }
}

class AppDelegate: NSObject, UIApplicationDelegate {
    
    func application(
          _ application: UIApplication,
          didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
      ) -> Bool {
          FirebaseApp.configure()
          NotifierManager.shared.initialize(configuration: NotificationPlatformConfigurationIos(
                      showPushNotification: true,
                      askNotificationPermissionOnStart: true,
                      notificationSoundName: nil
                    )
                )
          return true
      }
    
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
           Messaging.messaging().apnsToken = deviceToken
     }

}

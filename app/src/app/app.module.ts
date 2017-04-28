// ionic imports
import { BrowserModule } from '@angular/platform-browser';
import { ErrorHandler, NgModule } from '@angular/core';
import { IonicApp, IonicErrorHandler, IonicModule } from 'ionic-angular';
import { SplashScreen } from '@ionic-native/splash-screen';
import { StatusBar } from '@ionic-native/status-bar';

// 3rd party imports
import { AngularFireModule, AuthProviders, AuthMethods } from 'angularfire2';

// service imports
import { GlobalVars } from '../providers/global-vars'
import { AuthService } from '../providers/auth-service'

// page imports
import { Main } from './app.component';
import { HomePage } from '../pages/home/home';
import { Wordcloud } from '../pages/wordcloud/wordcloud';

// firebase configs
const firebaseConfig = {
  apiKey: "AIzaSyDRQ0ZtlSE1xp1O70-7BLAd6cMidFFgKzw",
  authDomain: "is470-65442.firebaseapp.com",
  databaseURL: "https://is470-65442.firebaseio.com",
  // projectId: "is470-65442",
  storageBucket: "is470-65442.appspot.com",
  messagingSenderId: "464363411002"
};

const firebaseAuthConfig = {
  provider: AuthProviders.Google,
  method: AuthMethods.Redirect
};

@NgModule({
  declarations: [
    Main,
    HomePage,
    Wordcloud
  ],
  imports: [
    BrowserModule,
    IonicModule.forRoot(Main),
    AngularFireModule.initializeApp(firebaseConfig, firebaseAuthConfig)
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    Main,
    HomePage,
    Wordcloud
  ],
  providers: [
    StatusBar,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    GlobalVars,
    AuthService
  ]
})
export class AppModule {}

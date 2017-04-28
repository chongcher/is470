import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { GlobalVars } from '../../providers/global-vars'
import { AuthService } from '../../providers/auth-service'
import { Wordcloud } from '../wordcloud/wordcloud'
import { FirebaseAuthState } from 'angularfire2';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  constructor(public navCtrl: NavController, public auth$: AuthService, public global: GlobalVars) {
    var state: FirebaseAuthState;
    if (this.global.logging) {
      if ( state == undefined ) {
        setTimeout( function() { getState( auth$ ) }, 1000 );
      } else {
        console.log( "auth: ", auth$.authentication);
      }
    }
    function getState( auth$: AuthService ) {
      if( auth$.authentication == undefined ){
        setTimeout( function() { getState( auth$ ) }, 1000);
      } else {
        console.log( "auth: ", auth$.authentication);
      }
    }
  }

  goToWordcloud(event, item) {
    this.navCtrl.push(Wordcloud, {
    });
  }

  login() {
    if (this.global.logging) {
      console.log( "login" );
    }
    this.auth$.signIn()
  }

  logout() {
    if (this.global.logging) {
      console.log( "logout" );
    }
    this.auth$.signOut()
  }

}
import { Injectable } from '@angular/core';
// import { Http } from '@angular/http';
import 'rxjs/add/operator/map';
import { AngularFireAuth, FirebaseAuthState } from 'angularfire2';

/*
  Generated class for the AuthService provider.

  See https://angular.io/docs/ts/latest/guide/dependency-injection.html
  for more info on providers and Angular 2 DI.
*/
@Injectable()
export class AuthService {
  private authState: FirebaseAuthState;

  constructor(public auth$: AngularFireAuth) {
    // this.authState = auth$.getAuth();
    auth$.subscribe((state: FirebaseAuthState) => {
      console.log("authstate: ", state);
      this.authState = state;
    });
  }

  get authentication(): FirebaseAuthState {
    return this.authState
  }

  signIn() {
    this.auth$.login()
  }

  // signInWithFacebook(): firebase.Promise<FirebaseAuthState> {
  //   return this.auth$.login({
  //     provider: AuthProviders.Facebook,
  //     method: AuthMethods.Popup
  //   });
  // }

  signOut(): void {
    this.auth$.logout();
    location.reload();
  }

  // displayName(): string {
  //   if (this.authState != null) {
  //     return this.authState.facebook.displayName;
  //   } else {
  //     return '';
  //   }
  // }
}

